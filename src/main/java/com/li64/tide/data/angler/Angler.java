package com.li64.tide.data.angler;

import com.li64.tide.Tide;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.journal.FishRarity;
import com.li64.tide.data.player.FishStats;
import com.li64.tide.data.player.TidePlayerData;
import com.li64.tide.network.messages.AnglerSyncMsg;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.List;

/**
 * The fishing level system: levels 1-99 on RuneScape's XP curve, the stats a level gives, XP for catches,
 * and level-up fanfare. Level {@link #baseline()} (40 by default) fishes exactly like original Tide;
 * lower is harder, higher is easier.
 *
 * Saved per player under "TideAngler" in Tide's player data (copied across death like the journal).
 * The server mirrors each player's level and XP to the scoreboards tide_level / tide_xp for Solstone.
 */
public final class Angler {
    private Angler() {}

    public static final int MAX_LEVEL = 99;
    private static final String TAG = "TideAngler";
    public static final String OBJ_LEVEL = "tide_level";
    public static final String OBJ_XP = "tide_xp";

    // ------------------------------------------------------------------ settings

    public static boolean enabled() {
        return Tide.SERVER_CONFIG == null || Tide.SERVER_CONFIG.leveling.enabled;
    }

    public static int baseline() {
        return Tide.SERVER_CONFIG == null ? 40 : Math.max(1, Math.min(MAX_LEVEL, Tide.SERVER_CONFIG.leveling.baselineLevel));
    }

    private static double xpBase() {
        return Tide.SERVER_CONFIG == null ? 20.0 : Math.max(1.0, Tide.SERVER_CONFIG.leveling.xpPerLevel);
    }

    private static double xpGrowth() {
        return Tide.SERVER_CONFIG == null ? 0.2 : Math.max(0.0, Tide.SERVER_CONFIG.leveling.xpGrowth);
    }

    // ------------------------------------------------------------------ the curve

    private static int softFrom() {
        return Tide.SERVER_CONFIG == null ? 30 : Math.max(1, Tide.SERVER_CONFIG.leveling.softenFromLevel);
    }

    private static double softShare() {
        return Tide.SERVER_CONFIG == null ? 0.5 : Math.max(0.0, Math.min(1.0, Tide.SERVER_CONFIG.leveling.growthAboveSoften));
    }

    private static double squares(long n) {
        return n * (n + 1) * (2 * n + 1) / 6.0;
    }

    /** XP to go from {@code level} to the next: base + growth x level squared, half as steep from level 30 on by default. */
    public static long xpToNext(int level) {
        double g = xpGrowth(), cost = xpBase() + g * level * level;
        int k = softFrom();
        if (level >= k) cost -= g * (1 - softShare()) * ((double) level * level - (double) k * k);
        return Math.round(cost);
    }

    /** Total XP needed to reach a level (level 1 = 0). Defaults: 2 = 20, 30 = ~2,300, 50 = ~7,700, 99 = ~41,000. */
    public static long xpForLevel(int level) {
        level = Math.max(1, Math.min(MAX_LEVEL, level));
        long n = level - 1;
        double g = xpGrowth(), total = xpBase() * n + g * squares(n);
        int k = softFrom();
        if (n >= k) total -= g * (1 - softShare()) * (squares(n) - squares(k - 1) - (double) k * k * (n - k + 1));
        return Math.round(total);
    }

    public static int levelForXp(long xp) {
        int level = 1;
        while (level < MAX_LEVEL && xp >= xpForLevel(level + 1)) level++;
        return level;
    }

    /** How far through the current level, 0..1 (1 at the cap). */
    public static float progress(long xp) {
        int level = levelForXp(xp);
        if (level >= MAX_LEVEL) return 1f;
        long from = xpForLevel(level), to = xpForLevel(level + 1);
        return (float) (xp - from) / Math.max(1, to - from);
    }

    public static String title(int level) {
        if (level >= 99) return "Legendary Angler";
        if (level >= 90) return "Grandmaster Angler";
        if (level >= 80) return "Master Angler";
        if (level >= 70) return "Veteran Angler";
        if (level >= 60) return "Expert Angler";
        if (level >= 50) return "Seasoned Angler";
        if (level >= 40) return "Skilled Angler";
        if (level >= 30) return "Angler";
        if (level >= 20) return "Hobbyist Angler";
        if (level >= 10) return "Apprentice Angler";
        return "Novice Angler";
    }

    // ------------------------------------------------------------------ stats

    /** Level 1 -> v1, the baseline -> 1.0, level 99 -> v99, straight lines between. */
    static double curve(int level, double v1, double v99) {
        int base = baseline();
        if (level <= base) return v1 + (1.0 - v1) * (level - 1) / Math.max(1.0, base - 1);
        return 1.0 + (v99 - 1.0) * (level - base) / Math.max(1.0, MAX_LEVEL - base);
    }

    /** What a level does. All multipliers are against original Tide (1.0). */
    public record Stats(int level, double castDistance, double fightSpeed, double biteWait, int luck, double hookZones) {
        public static Stats of(int level) {
            level = Math.max(1, Math.min(MAX_LEVEL, level));
            int luck = level < 20 ? -1 : level < 50 ? 0 : level < 80 ? 1 : 2;
            return new Stats(level,
                    curve(level, 0.5, 1.25),
                    curve(level, 1.25, 0.85),
                    curve(level, 1.3, 0.75),
                    luck,
                    curve(level, 0.6, 1.25));
        }
    }

    /** The level a player fishes at: their own, the baseline when levels are off, or the baseline in fair play. */
    public static int effectiveLevel(ServerPlayer player) {
        if (!enabled()) return baseline();
        Data d = Data.of(player);
        return d.fair ? baseline() : levelForXp(d.xp);
    }

    public static Stats stats(ServerPlayer player) {
        return Stats.of(effectiveLevel(player));
    }

    /** How good a fish is at fighting, by rarity: the level at which it fights "normally". */
    public static int fishLevel(FishRarity rarity) {
        return switch (rarity) {
            case COMMON -> 1;
            case UNCOMMON -> 15;
            case RARE -> 35;
            case VERY_RARE -> 55;
            case LEGENDARY -> 75;
        };
    }

    /** Minigame speed multiplier: the player's general skill, plus a penalty for fish above their level. */
    public static double fightSpeed(int level, FishRarity rarity) {
        double gap = Math.max(0, fishLevel(rarity) - level);
        // a high level calms common fish, but rare, very rare and legendary fish fight at least as hard as in original
        // Tide however good you are: only tackle makes them easier
        double skill = rareOrRarer(rarity) ? Math.max(1.0, Stats.of(level).fightSpeed()) : Stats.of(level).fightSpeed();
        return Math.min(2.4, skill * (1.0 + gap * 0.012));
    }

    /** Rare, very rare or legendary. */
    public static boolean rareOrRarer(FishRarity rarity) {
        return rarity.ordinal() >= FishRarity.RARE.ordinal();
    }

    /** How much the level widens the hook zones for this fish (for rare and rarer fish, never past original Tide's). */
    public static double hookZones(int level, FishRarity rarity) {
        double z = Stats.of(level).hookZones();
        return rareOrRarer(rarity) ? Math.min(1.0, z) : z;
    }

    // ------------------------------------------------------------------ the minigame bar

    /** Zone edges on the minigame bar, as a fraction of its half-width out from the centre. */
    public record Zones(float perfect, float great, float good, float bad) {
        /** {@code area}: the fish's catch area (1 - strength, after line bonuses), as in original Tide. */
        public static Zones of(float area, double hookZones) {
            float k = (float) hookZones;
            float good = Math.min(area * k, 0.92f);
            float perfect = Math.min(0.1f * k, good * 0.6f);
            float great = perfect + (good - perfect) * 0.5f;
            float bad = Math.min(good + 0.3f, 0.97f);
            return new Zones(perfect, great, good, bad);
        }
    }

    public enum Hook {
        PERFECT(1.0), GREAT(1.0), GOOD(0.9), BAD(0.3), TERRIBLE(0.0);

        public final double landChance;

        Hook(double landChance) {
            this.landChance = landChance;
        }

        public static Hook of(float accuracy, Zones z) {
            if (accuracy < z.perfect()) return PERFECT;
            if (accuracy < z.great()) return GREAT;
            if (accuracy < z.good()) return GOOD;
            if (accuracy < z.bad()) return BAD;
            return TERRIBLE;
        }
    }

    // ------------------------------------------------------------------ XP

    public static int rarityXp(FishRarity rarity) {
        return switch (rarity) {
            case COMMON -> 10;
            case UNCOMMON -> 20;
            case RARE -> 40;
            case VERY_RARE -> 80;
            case LEGENDARY -> 200;
        };
    }

    /** Call before the catch goes into the journal (so first catches and records can be spotted). */
    public static long onFishCaught(ServerPlayer player, ItemStack stack, boolean perfect) {
        return onFishCaught(player, stack, perfect, 0.0);
    }

    /** {@code xpBoost}: from the rod's bobber (0.10 = +10%). */
    public static long onFishCaught(ServerPlayer player, ItemStack stack, boolean perfect, double xpBoost) {
        if (!enabled()) return 0;
        FishData data = FishData.get(stack).orElse(null);
        if (data == null) return 0;
        FishRarity rarity = data.profile().rarity();
        double xp = rarityXp(rarity);
        String extra = "";
        // the XP, as parts that add up: the fish, then each bonus in whole points (the card shows them)
        java.util.List<String> parts = new java.util.ArrayList<>();
        // (the fish's own XP is the base; only what was added on top is listed)

        TidePlayerData journal = TidePlayerData.getOrCreate(player);
        FishStats before = journal.getDataFor(stack.getItem()).flatMap(f -> f.stats).orElse(null);
        if (before == null || before.getAmountCaught() == 0) {
            xp += NEW_SPECIES_XP;
            parts.add("+" + NEW_SPECIES_XP + " new species");
            extra = " · new species!";
        } else {
            // your heaviest of the kind (fish from before weights: the longest)
            double grams = com.li64.tide.data.item.TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0);
            double length = com.li64.tide.data.item.TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0);
            boolean record = grams > 0 && before.getHeaviestCatch() > 0 ? FishWeight.visiblyHeavier(grams, before.getHeaviestCatch())
                    : length > 0 && length > before.getLargestCatch();
            if (record) {
                xp += RECORD_XP;
                parts.add("+" + RECORD_XP + " your record");
                extra = " · new record!";
            }
        }
        Integer q = com.li64.tide.data.item.TideItemData.FISH_QUALITY.getOrDefault(stack, null);
        if (q != null && CatchQuality.byId(q).bonusXp() > 0) {
            xp += CatchQuality.byId(q).bonusXp();
            parts.add("+" + CatchQuality.byId(q).bonusXp() + " " + CatchQuality.byId(q).label);
        }
        long sum = Math.round(xp);
        if (perfect) {
            long add = Math.round(xp * 1.25) - sum;
            xp *= 1.25;
            sum += add;
            if (add > 0) parts.add("+" + add + " perfect hook");
        }
        if (xpBoost > 0) {
            long add = Math.round(xp * (1.0 + xpBoost)) - sum;
            xp *= 1.0 + xpBoost;
            sum += add;
            if (add > 0) parts.add("+" + add + " tackle");
        }
        // the village rod teaches: a catch near another player holding one is worth 10% more XP
        boolean taught = !player.level().getEntitiesOfClass(net.minecraft.world.entity.player.Player.class, player.getBoundingBox().inflate(16),
                p -> p != player && (p.getMainHandItem().is(com.li64.tide.registries.TideItems.VILLAGE_FISHING_ROD)
                        || p.getOffhandItem().is(com.li64.tide.registries.TideItems.VILLAGE_FISHING_ROD))).isEmpty();
        if (taught) {
            long add = Math.round(xp * 1.10) - sum;
            xp *= 1.10;
            sum += add;
            if (add > 0) parts.add("+" + add + " fishing buddy");
        }
        long total = sum;
        int levelBefore = levelForXp(Data.of(player).xp);
        addXp(player, total, extra, false);
        int levelAfter = levelForXp(Data.of(player).xp);
        banner(player, stack, total, parts, perfect, extra, levelBefore, levelAfter);
        return total;
    }

    /**
     * One line per caught fish in the server log, for outside tools (Solstone reads it):
     * {@code [Tide catch] {"player":"Tyler","fish":"tide:bluegill","quality":2,...}}
     */
    public static void logCatch(ServerPlayer player, ItemStack stack, Hook hook, long xp, boolean newSpecies) {
        com.google.gson.JsonObject o = new com.google.gson.JsonObject();
        o.addProperty("player", player.getScoreboardName());
        o.addProperty("uuid", player.getStringUUID());
        o.addProperty("fish", net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        FishData.get(stack).ifPresent(d -> o.addProperty("rarity", d.profile().rarity().getSerializedName()));
        Integer q = com.li64.tide.data.item.TideItemData.FISH_QUALITY.getOrDefault(stack, null);
        if (q != null) {
            o.addProperty("quality", q);
            o.addProperty("quality_name", CatchQuality.byId(q).label);
        }
        double len = com.li64.tide.data.item.TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0);
        double grams = com.li64.tide.data.item.TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0);
        if (len > 0) o.addProperty("length_cm", Math.round(len * 10) / 10.0);
        if (grams > 0) o.addProperty("weight_g", Math.round(grams * 10) / 10.0);
        o.addProperty("level", effectiveLevel(player));
        o.addProperty("fair", enabled() && Data.of(player).fair);
        o.addProperty("hook", hook.name().toLowerCase(java.util.Locale.ROOT));
        o.addProperty("xp", xp);
        o.addProperty("new_species", newSpecies);
        o.addProperty("dim", player.level().dimension().identifier().toString());
        o.addProperty("x", player.getBlockX());
        o.addProperty("y", player.getBlockY());
        o.addProperty("z", player.getBlockZ());
        Tide.LOG.info("[Tide catch] {}", o);
    }

    public static final int XP_VERSION = 2;
    public static final int NEW_SPECIES_XP = 20;
    public static final int RECORD_XP = 5;

    /** XP a journal entry is worth: every catch at its rarity, quality bonuses where known, and the first-catch bonus. */
    static long backfillXp(FishStats stats, FishRarity rarity) {
        long xp = (long) stats.getAmountCaught() * rarityXp(rarity) + NEW_SPECIES_XP;
        for (CatchQuality q : CatchQuality.values()) xp += (long) stats.getQualityCount(q) * q.bonusXp();
        return xp;
    }

    /** Re-derive a player's XP from their journal (after a curve or XP change). Returns the new XP. */
    public static long recalculate(ServerPlayer player) {
        long xp = 0;
        TidePlayerData journal = TidePlayerData.getOrCreate(player);
        for (var entry : journal.fishPlayerData.entrySet()) {
            FishStats stats = entry.getValue().stats.orElse(null);
            if (stats == null || stats.getAmountCaught() <= 0) continue;
            FishRarity rarity = FishData.get(entry.getKey().value()).map(f -> f.profile().rarity()).orElse(FishRarity.COMMON);
            xp += backfillXp(stats, rarity);
        }
        Data d = Data.of(player);
        d.xp = xp;
        d.backfilled = true;
        d.version = XP_VERSION;
        d.save(player);
        sync(player);
        return xp;
    }

    public static void onCrateCaught(ServerPlayer player) {
        if (enabled()) addXp(player, 15, "");
    }

    public static void onItemCaught(ServerPlayer player) {
        if (enabled()) addXp(player, 3, "");
    }

    public static void addXp(ServerPlayer player, long amount, String extra) {
        addXp(player, amount, extra, true);
    }

    private static final java.util.Set<java.util.UUID> TWIN = java.util.concurrent.ConcurrentHashMap.newKeySet();

    private static final java.util.Map<java.util.UUID, java.util.List<String>> EXTRAS = new java.util.concurrent.ConcurrentHashMap<>();

    /** Something else that came up with the fish (a honeycomb, a sapling, cooked on the line...), for the catch card. */
    public static void markExtra(ServerPlayer player, String what) {
        EXTRAS.computeIfAbsent(player.getUUID(), k -> new java.util.ArrayList<>()).add(what);
    }

    private static final java.util.Map<java.util.UUID, java.util.List<String>> EFFECTS = new java.util.concurrent.ConcurrentHashMap<>();

    /** Something a rod did when the fish came in (the hex rod's potion, the sickle rod's crops), for the catch card. */
    public static void markEffect(ServerPlayer player, String what) {
        EFFECTS.computeIfAbsent(player.getUUID(), k -> new java.util.ArrayList<>()).add(what);
    }

    /** The hook says a second fish came up with this one (shown on the catch banner). */
    private static final java.util.Set<java.util.UUID> SERVER_RECORD = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /** This catch beat the server's record (shown on the card and the chat line). */
    public static void markServerRecord(ServerPlayer player) {
        SERVER_RECORD.add(player.getUUID());
    }

    private static final java.util.Map<java.util.UUID, java.util.List<String>> BUMPS = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Set<java.util.UUID> PERFECT_BUMPED = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /** Forget bumps from a catch that never landed (called when a new fish bites). */
    public static void clearBumps(ServerPlayer player) {
        BUMPS.remove(player.getUUID());
        PERFECT_BUMPED.remove(player.getUUID());
    }

    /** The catch was brought up a quality tier by {@code source} ("Perfect hook", "Tackle"...), for the catch card. */
    public static void markBump(ServerPlayer player, String source, CatchQuality from, CatchQuality to) {
        BUMPS.computeIfAbsent(player.getUUID(), k -> new java.util.ArrayList<>()).add(source + ": " + from.label + " to " + to.label + "!");
        if (source.equals("Perfect hook")) PERFECT_BUMPED.add(player.getUUID());
    }

    public static void markTwin(ServerPlayer player) {
        TWIN.add(player.getUUID());
    }

    /** The card above the hotbar for a catch: what it was, how big, what was special, the XP (and a small level-up). */
    private static void banner(ServerPlayer player, ItemStack stack, long xp, java.util.List<String> parts, boolean perfect, String extra, int before, int after) {
        Integer qi = com.li64.tide.data.item.TideItemData.FISH_QUALITY.getOrDefault(stack, null);
        CatchQuality q = qi == null ? null : CatchQuality.byId(qi);
        String name = Component.translatable(stack.getItem().getDescriptionId()).getString();
        String title = q == null || q == CatchQuality.AVERAGE ? name : q.label + " " + name;
        java.util.List<String> bits = new java.util.ArrayList<>();
        double grams = com.li64.tide.data.item.TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0);
        if (extra.contains("new species")) bits.add("New species!");
        boolean setRecord = SERVER_RECORD.remove(player.getUUID());
        if (setRecord) bits.add("Server record!");
        else if (extra.contains("new record")) bits.add("Your record!");
        if (TWIN.remove(player.getUUID())) bits.add("Two on the hook!");
        // (a perfect hook that also raised the quality is one purple line below, not two)
        boolean perfectBumped = PERFECT_BUMPED.remove(player.getUUID());
        if (perfect && !perfectBumped) bits.add("Perfect hook");
        java.util.List<String> extras = EXTRAS.remove(player.getUUID());
        java.util.List<String> effects = EFFECTS.remove(player.getUUID());
        java.util.List<String> bumps = BUMPS.remove(player.getUUID());
        if (bumps != null) {
            effects = effects == null ? new java.util.ArrayList<>() : new java.util.ArrayList<>(effects);
            effects.addAll(0, bumps);
        }
        // the XP and where it came from (the level is on the rod panel; a level-up gets its own line)
        String footer = "+" + xp + " Fishing XP" + (!parts.isEmpty() ? " (" + String.join(", ", parts) + ")" : "");
        if (after > before) footer += "\nLevel up! Fishing level " + after;
        // one line in chat, for after the card has gone: the fish (in its quality's colour), its weight, what was
        // special and the XP; hovering it shows the rest of the card (what came up with it, what the rod did)
        java.util.List<String> hover = new java.util.ArrayList<>();
        if (extras != null) hover.add("Also caught: " + String.join(", ", extras));
        if (effects != null) hover.addAll(effects);       // (the bumps are in there too)
        hover.add(footer);
        // how it compares with the server's record for the species (in the chat hover, to keep the card short)
        net.minecraft.server.MinecraftServer srv = player.level().getServer();
        var rec = srv == null || grams <= 0 || setRecord ? null
                : ServerRecords.get(srv, net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (rec != null) hover.add("Server record: " + FishWeight.imperial(rec.grams()) + " by " + rec.player()
                + (grams < rec.grams() ? " (this catch: " + Math.round(grams / rec.grams() * 100) + "% of it)" : ""));
        net.minecraft.network.chat.MutableComponent line = Component.literal(title).withStyle(s -> s.withColor(q == null ? 0xFFFFFF : q.color));
        if (grams > 0) line.append(Component.literal(" " + FishWeight.imperial(grams)).withStyle(ChatFormatting.GOLD));
        java.util.List<String> notes = new java.util.ArrayList<>(bits);
        notes.add("+" + xp + " XP");
        line.append(Component.literal(" · " + String.join(" · ", notes)).withStyle(ChatFormatting.GRAY));
        line.withStyle(s -> s.withHoverEvent(new net.minecraft.network.chat.HoverEvent.ShowText(Component.literal(String.join("\n", hover)))));
        player.sendSystemMessage(line);
        // the card's lines: what was special (white), what came up with it (~, yellow), what the rod did (*, purple)
        java.util.List<String> cardLines = new java.util.ArrayList<>();
        if (!bits.isEmpty()) cardLines.add(String.join(" · ", bits));
        if (extras != null) cardLines.add("~" + String.join(" · ", extras));
        if (effects != null) cardLines.add("*" + String.join(" · ", effects));
        Tide.NETWORK.sendToPlayer(new com.li64.tide.network.messages.CatchBannerMsg(
                net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), title,
                q == null ? 0xFFFFFF : q.color, String.join("\n", cardLines), footer, false,
                grams > 0 ? FishWeight.imperial(grams) : ""), player);
    }

    private static void addXp(ServerPlayer player, long amount, String extra, boolean overlay) {
        Data d = Data.of(player);
        int before = levelForXp(d.xp);
        d.xp = Math.max(0, d.xp + amount);
        d.save(player);
        int after = levelForXp(d.xp);

        if (overlay && amount > 0) player.sendOverlayMessage(Component.literal("+" + amount + " Fishing XP")      // (the level is on the rod panel)
                .withStyle(ChatFormatting.AQUA).append(Component.literal(extra).withStyle(ChatFormatting.GRAY)));
        if (after > before) levelUp(player, after, overlay);
        sync(player);
    }

    public static void setLevel(ServerPlayer player, int level) {
        Data d = Data.of(player);
        d.xp = xpForLevel(level);
        d.save(player);
        sync(player);
    }

    /** Back to a brand-new angler: level 1, no XP, fair play off. With {@code everything}, also an empty fish
     * journal (catches, records, quality counts) and tackle log. Kept "backfilled" so XP isn't recounted. */
    public static void reset(ServerPlayer player, boolean everything) {
        Data d = Data.of(player);
        d.xp = 0;
        d.fair = false;
        d.backfilled = true;
        d.version = XP_VERSION;
        d.save(player);
        if (everything) {
            com.li64.tide.data.player.TidePlayerData data = com.li64.tide.data.player.TidePlayerData.getOrCreate(player);
            data.fishPlayerData.clear();
            data.syncTo(player);
            com.li64.tide.data.tackle.TackleLog.clear(player);
        }
        sync(player);
        Tide.LOG.info("[Tide reset] {} {}", player.getScoreboardName(), everything ? "everything" : "level");
    }

    public static void setFair(ServerPlayer player, boolean fair) {
        Data d = Data.of(player);
        d.fair = fair;
        d.save(player);
        sync(player);
    }

    private static void levelUp(ServerPlayer player, int level, boolean bannerIfSmall) {
        String title = title(level);
        boolean newTitle = !title.equals(title(level - 1));
        if (newTitle) {     // the big on-screen title is saved for a new fishing title
            player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 70, 20));
            player.connection.send(new ClientboundSetTitleTextPacket(
                    Component.literal("Fishing level " + level).withStyle(ChatFormatting.AQUA)));
            player.connection.send(new ClientboundSetSubtitleTextPacket(
                    Component.literal("You are now a " + title + "!").withStyle(ChatFormatting.GOLD)));
        } else if (bannerIfSmall) {   // a plain level-up gets the small card (on a catch, the catch card says it)
            Tide.NETWORK.sendToPlayer(new com.li64.tide.network.messages.CatchBannerMsg("tide:fishing_journal",
                    "Level up! Fishing level " + level, 0x7FE0FF, title, "", false, ""), player);
        }
        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.2f);
        if (newTitle || level % 10 == 0) firework(player, level >= 90);

        MinecraftServer server = player.level().getServer();
        if (server != null && (Tide.SERVER_CONFIG == null || Tide.SERVER_CONFIG.leveling.announceLevelUps)) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("🎣 ").withStyle(ChatFormatting.AQUA)
                    .append(player.getDisplayName().copy().withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" reached fishing level " + level + (newTitle ? " — " + title + "!" : "!"))
                            .withStyle(ChatFormatting.AQUA)), false);
        }
    }

    private static void firework(ServerPlayer player, boolean big) {
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        FireworkExplosion burst = new FireworkExplosion(big ? FireworkExplosion.Shape.LARGE_BALL : FireworkExplosion.Shape.BURST,
                IntList.of(0x3FA9F5, 0x7FE0FF, 0xFFD24A), IntList.of(0xFFFFFF), true, big);
        rocket.set(DataComponents.FIREWORKS, new Fireworks(1, List.of(burst)));
        player.level().addFreshEntity(new FireworkRocketEntity(player.level(), player.getX(), player.getY() + 1, player.getZ(), rocket));
    }

    // ------------------------------------------------------------------ joining, syncing, scoreboards

    /** On join: count up XP from the journal the first time (players who fished before levels existed). */
    public static void onJoin(ServerPlayer player) {
        Data d = Data.of(player);
        if (d.backfilled && d.version < XP_VERSION) {
            // XP rules changed (v2: new curve, smaller first-catch bonus): recount from the journal
            recalculate(player);
            return;
        }
        if (!d.backfilled) {
            long xp = 0;
            TidePlayerData journal = TidePlayerData.getOrCreate(player);
            for (var entry : journal.fishPlayerData.entrySet()) {
                FishStats stats = entry.getValue().stats.orElse(null);
                if (stats == null || stats.getAmountCaught() <= 0) continue;
                FishRarity rarity = FishData.get(entry.getKey().value()).map(f -> f.profile().rarity()).orElse(FishRarity.COMMON);
                xp += backfillXp(stats, rarity);
            }
            d.xp = Math.max(d.xp, xp);
            d.backfilled = true;
            d.version = XP_VERSION;
            d.save(player);
        }
        sync(player);
    }

    public static void sync(ServerPlayer player) {
        Data d = Data.of(player);
        Tide.NETWORK.sendToPlayer(new AnglerSyncMsg(d.xp, d.fair, enabled(), baseline()), player);
        MinecraftServer server = player.level().getServer();
        if (server == null) return;
        Scoreboard board = server.getScoreboard();
        Objective lvl = objective(board, OBJ_LEVEL, "Fishing level");
        Objective xp = objective(board, OBJ_XP, "Fishing XP");
        ScoreHolder holder = ScoreHolder.forNameOnly(player.getScoreboardName());
        board.getOrCreatePlayerScore(holder, lvl).set(levelForXp(d.xp));
        board.getOrCreatePlayerScore(holder, xp).set((int) Math.min(Integer.MAX_VALUE, d.xp));
    }

    private static Objective objective(Scoreboard board, String name, String display) {
        Objective o = board.getObjective(name);
        if (o == null) o = board.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(display),
                ObjectiveCriteria.RenderType.INTEGER, true, null);
        return o;
    }

    // ------------------------------------------------------------------ storage

    public static final class Data {
        public long xp;
        public boolean fair;
        public boolean backfilled;
        /** Bumped when XP rules change, so existing players get recounted from their journal. */
        public int version;

        public static Data of(ServerPlayer player) {
            CompoundTag tag = Tide.PLATFORM.getPlayerData(player).getCompoundOrEmpty(TAG);
            Data d = new Data();
            d.xp = tag.getLongOr("xp", 0L);
            d.fair = tag.getBooleanOr("fair", false);
            d.backfilled = tag.getBooleanOr("backfilled", false);
            d.version = tag.getIntOr("version", 1);
            return d;
        }

        public void save(ServerPlayer player) {
            CompoundTag tag = new CompoundTag();
            tag.putLong("xp", xp);
            tag.putBoolean("fair", fair);
            tag.putBoolean("backfilled", backfilled);
            tag.putInt("version", version);
            Tide.PLATFORM.getPlayerData(player).put(TAG, tag);
        }
    }
}
