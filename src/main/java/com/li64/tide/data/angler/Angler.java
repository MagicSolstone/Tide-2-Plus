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

    /** XP to go from {@code level} to the next: base + growth x level squared (20 + 0.2L^2 by default). */
    public static long xpToNext(int level) {
        return Math.round(xpBase() + xpGrowth() * level * level);
    }

    /** Total XP needed to reach a level (level 1 = 0). Defaults: 2 = 20, 30 = ~2,300, 50 = ~8,000, 99 = ~66,000. */
    public static long xpForLevel(int level) {
        level = Math.max(1, Math.min(MAX_LEVEL, level));
        long n = level - 1;
        return Math.round(xpBase() * n + xpGrowth() * n * (n + 1) * (2 * n + 1) / 6.0);
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
        return Math.min(2.4, Stats.of(level).fightSpeed() * (1.0 + gap * 0.012));
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
        java.util.List<String> parts = new java.util.ArrayList<>();
        parts.add(net.minecraft.network.chat.Component.translatable(stack.getItem().getDescriptionId()).getString() + " " + Math.round(xp));

        TidePlayerData journal = TidePlayerData.getOrCreate(player);
        FishStats before = journal.getDataFor(stack.getItem()).flatMap(f -> f.stats).orElse(null);
        if (before == null || before.getAmountCaught() == 0) {
            xp += NEW_SPECIES_XP;
            parts.add("new species +" + NEW_SPECIES_XP);
            extra = " · new species!";
        } else {
            double length = com.li64.tide.data.item.TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0);
            if (length > 0 && length > before.getLargestCatch()) {
                xp += RECORD_XP;
                parts.add("new record +" + RECORD_XP);
                extra = " · new record!";
            }
        }
        Integer q = com.li64.tide.data.item.TideItemData.FISH_QUALITY.getOrDefault(stack, null);
        if (q != null && CatchQuality.byId(q).bonusXp() > 0) {
            xp += CatchQuality.byId(q).bonusXp();
            parts.add(CatchQuality.byId(q).label + " +" + CatchQuality.byId(q).bonusXp());
        }
        if (perfect) {
            xp *= 1.25;
            parts.add("perfect hook x1.25");
        }
        if (xpBoost > 0) {
            xp *= 1.0 + xpBoost;
        }
        long total = Math.round(xp);
        int levelBefore = levelForXp(Data.of(player).xp);
        addXp(player, total, extra, false);
        int levelAfter = levelForXp(Data.of(player).xp);
        banner(player, stack, total, perfect, extra, levelBefore, levelAfter);
        // just the total (gear bonuses are already in it), and whether the hook was perfect
        player.sendSystemMessage(Component.literal("+" + total + " fishing XP" + (perfect ? " · perfect hook!" : ""))
                .withStyle(ChatFormatting.DARK_GRAY));
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

    /** The hook says a second fish came up with this one (shown on the catch banner). */
    public static void markTwin(ServerPlayer player) {
        TWIN.add(player.getUUID());
    }

    /** The card above the hotbar for a catch: what it was, how big, what was special, the XP (and a small level-up). */
    private static void banner(ServerPlayer player, ItemStack stack, long xp, boolean perfect, String extra, int before, int after) {
        Integer qi = com.li64.tide.data.item.TideItemData.FISH_QUALITY.getOrDefault(stack, null);
        CatchQuality q = qi == null ? null : CatchQuality.byId(qi);
        String name = Component.translatable(stack.getItem().getDescriptionId()).getString();
        String title = q == null || q == CatchQuality.AVERAGE ? name : q.label + " " + name;
        java.util.List<String> bits = new java.util.ArrayList<>();
        double len = com.li64.tide.data.item.TideItemData.FISH_LENGTH.getOrDefault(stack, 0.0);
        double grams = com.li64.tide.data.item.TideItemData.FISH_WEIGHT.getOrDefault(stack, 0.0);
        if (len > 0) bits.add(FishWeight.imperialLength(len) + (grams > 0 ? " · " + FishWeight.imperial(grams) : ""));
        if (extra.contains("new species")) bits.add("New species!");
        if (extra.contains("new record")) bits.add("New record!");
        if (TWIN.remove(player.getUUID())) bits.add("Two on the hook!");
        if (perfect) bits.add("Perfect hook");
        String footer = "+" + xp + " fishing XP · Lv " + after + " (" + Math.round(progress(Data.of(player).xp) * 100) + "%)";
        if (after > before && title(after).equals(title(before))) footer = "+" + xp + " fishing XP · Level up! Fishing level " + after;
        Tide.NETWORK.sendToPlayer(new com.li64.tide.network.messages.CatchBannerMsg(
                net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), title,
                q == null ? 0xFFFFFF : q.color, String.join(" · ", bits), footer, false), player);
    }

    private static void addXp(ServerPlayer player, long amount, String extra, boolean overlay) {
        Data d = Data.of(player);
        int before = levelForXp(d.xp);
        d.xp = Math.max(0, d.xp + amount);
        d.save(player);
        int after = levelForXp(d.xp);

        if (overlay && amount > 0) player.sendOverlayMessage(Component.literal("+" + amount + " Fishing XP")
                .withStyle(ChatFormatting.AQUA)
                .append(Component.literal("  Lv " + after + " · " + Math.round(progress(d.xp) * 100) + "%" + extra)
                        .withStyle(ChatFormatting.GRAY)));
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
                    "Level up! Fishing level " + level, 0x7FE0FF, title, "", false), player);
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
