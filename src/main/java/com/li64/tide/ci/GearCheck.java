package com.li64.tide.ci;

import com.li64.tide.Tide;
import com.li64.tide.compat.seasons.SeasonsCompat;
import com.li64.tide.data.TideData;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.angler.Angler;
import com.li64.tide.data.angler.CatchQuality;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.selector.CrateSelector;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.journal.FishRarity;
import com.li64.tide.data.rods.BaitContents;
import com.li64.tide.data.rods.CustomRodManager;
import com.li64.tide.data.rods.Lures;
import com.li64.tide.data.rods.RodEffects;
import com.li64.tide.data.rods.Unlocks;
import com.li64.tide.data.tackle.TackleLog;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import com.li64.tide.registries.items.TideFishingRodItem;
import com.li64.tide.util.BaitUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CI self-check of all the fishing gear, run once when the test server starts (-Dtide.ci=true). It goes
 * through every rod, line, hook (and every hook on every base hook), bobber, lure, bait and outfit with the
 * mod's real code and looks for combinations that break the game's numbers: fish that stop moving, odds that
 * don't add up, chances over 100%, crates crowding out fish, recipes that clash, tooltips that throw.
 * Problems are logged as "TIDE-CI gear problem: ..." (the workflow fails on them); a summary of the extremes
 * found is logged as "TIDE-CI gear: ...".
 */
public final class GearCheck {
    private GearCheck() {}

    private static final List<String> problems = new ArrayList<>();
    private static final List<String> notes = new ArrayList<>();

    private static void problem(String s) {
        if (problems.size() < 200) problems.add(s);
    }

    private static String id(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    public static void run(MinecraftServer server) {
        long start = System.currentTimeMillis();
        try {
            ServerLevel level = server.overworld();
            Map<TackleLog.Category, List<Item>> kinds = new EnumMap<>(TackleLog.Category.class);
            for (Item item : BuiltInRegistries.ITEM) {
                TackleLog.Category c = TackleLog.category(item);
                if (c != null) kinds.computeIfAbsent(c, k -> new ArrayList<>()).add(item);
            }
            List<Item> rods = kinds.getOrDefault(TackleLog.Category.RODS, List.of());
            List<Item> lines = kinds.getOrDefault(TackleLog.Category.LINES, List.of());
            List<Item> hooks = kinds.getOrDefault(TackleLog.Category.HOOKS, List.of());
            List<Item> bobbers = kinds.getOrDefault(TackleLog.Category.BOBBERS, List.of());
            List<Item> lures = kinds.getOrDefault(TackleLog.Category.LURES, List.of());
            List<Item> baits = kinds.getOrDefault(TackleLog.Category.BAIT, List.of());
            notes.add(String.format("%d rods, %d lines, %d hooks, %d bobbers, %d lures, %d bait", rods.size(), lines.size(), hooks.size(),
                    bobbers.size(), lures.size(), baits.size()));

            benchCheck(server);
            rodRepairCheck(server);

            String guide = com.li64.tide.data.commands.FishingCommand.guide(server);
            var parsed = com.google.gson.JsonParser.parseString(guide).getAsJsonObject();
            notes.add("guide: " + parsed.getAsJsonArray("items").size() + " items, " + parsed.getAsJsonArray("rigs").size()
                    + " rigs, " + guide.length() + " characters; first item " + parsed.getAsJsonArray("items").get(0));
            // a wild fish killed for its drop gets a quality (never above Great) and a weight
            for (String id : List.of("minecraft:cod", "tide:bluegill")) {
                ItemStack fish = new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(id)));
                int best = 0;
                for (int i = 0; i < 400; i++) {
                    ItemStack f = fish.copy();
                    com.li64.tide.data.loot.ApplyFishEntityLengthFunction.grade(f, level.getRandom(), 99, 0);
                    Integer q = com.li64.tide.data.item.TideItemData.FISH_QUALITY.getOrDefault(f, null);
                    double g = com.li64.tide.data.item.TideItemData.FISH_WEIGHT.getOrDefault(f, 0.0);
                    if (q == null || g <= 0) { problem("killed " + id + " has no quality or weight"); break; }
                    if (q > com.li64.tide.data.angler.CatchQuality.GREAT.ordinal()) { problem("killed " + id + " came out above Great"); break; }
                    best = Math.max(best, q);
                }
                notes.add("killed " + id + ": graded, best of 400 at level 99 = " + com.li64.tide.data.angler.CatchQuality.byId(best).label);
            }

            // fish crafted into resources give more for a better fish (through the real recipe, so the mixin is tested)
            {
                ItemStack tetra = new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse("tide:iron_tetra")));
                List<String> got = new ArrayList<>();
                for (com.li64.tide.data.angler.CatchQuality q : com.li64.tide.data.angler.CatchQuality.values()) {
                    ItemStack f = tetra.copy();
                    com.li64.tide.data.angler.CatchQuality.mark(f, q);
                    var input = net.minecraft.world.item.crafting.CraftingInput.of(1, 1, List.of(f));
                    var out = server.getRecipeManager().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING, input, level)
                            .map(h -> h.value().assemble(input)).orElse(ItemStack.EMPTY);
                    got.add(q.label + " " + out.getCount());
                    int want = com.li64.tide.data.angler.FishResources.amount(2, q.ordinal());
                    if (!out.is(net.minecraft.world.item.Items.RAW_IRON) || out.getCount() != want)
                        problem("iron tetra (" + q.label + ") crafted " + out + ", expected " + want + " raw iron");
                }
                notes.add("iron tetra -> raw iron: " + String.join(", ", got));
            }

            // what /fishing describe sends Solstone, for a few kinds of item
            for (String id : List.of("tide:golden_hook", "tide:bamboo_fishing_rod", "tide:golden_bait", "minecraft:diamond_sword"))
                notes.add("describe: " + com.li64.tide.data.commands.FishingCommand.tooltip(
                        new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(id))), server.registryAccess())
                        .replace("\n", " | "));

            // every hook, and every upgraded hook on every base hook
            List<ItemStack> hookStacks = new ArrayList<>();
            for (Item h : hooks) {
                hookStacks.add(new ItemStack(h));
                if (RodEffects.UPGRADED_HOOKS.contains(id(h)))
                    for (String base : RodEffects.BASE_HOOKS) {
                        if (base.equals("fishing_hook")) continue;
                        ItemStack s = new ItemStack(h);
                        s.set(com.li64.tide.data.item.TideDataComponents.BASE_HOOK, base);
                        hookStacks.add(s);
                    }
            }

            checkEachItem(rods, lines, hookStacks, bobbers, lures, baits);
            checkCombinations(level, rods, lines, hookStacks, bobbers, lures, baits);
            checkFishPools(level, rods, lures, baits);
            checkRecipes(server, kinds);
            checkData(level, kinds);
            checkCooking(level);
        } catch (Throwable t) {
            problem("the gear check itself crashed: " + t);
            Tide.LOG.error("TIDE-CI gear check crashed", t);
        }
        for (String n : notes) Tide.LOG.info("TIDE-CI gear: {}", n);
        for (String p : problems) Tide.LOG.error("TIDE-CI gear problem: {}", p);
        Tide.LOG.info("TIDE-CI gear check done in {} ms: {} problem(s)", System.currentTimeMillis() - start, problems.size());
    }

    // ------------------------------------------------------------------ one item at a time

    /** Each accessory counts exactly once on a rod, in the slot it's meant for, and its tooltip builds. */
    private static void checkEachItem(List<Item> rods, List<Item> lines, List<ItemStack> hooks, List<Item> bobbers, List<Item> lures, List<Item> baits) {
        int checked = 0;
        for (Item item : lines) checked += slotAdds(new ItemStack(item), CustomRodManager::setLine);
        for (ItemStack h : hooks) checked += slotAdds(h, CustomRodManager::setHook);
        for (Item item : bobbers) checked += slotAdds(new ItemStack(item), CustomRodManager::setBobber);
        for (Item item : lures) checked += slotAdds(new ItemStack(item), CustomRodManager::setLure);
        for (Item item : rods) {
            ItemStack rod = new ItemStack(item);
            tooltip(rod, true);
            if (!(item instanceof TideFishingRodItem) && !item.equals(Items.FISHING_ROD)) problem(id(item) + " is in the rods tag but isn't a fishing rod");
        }
        for (Item item : baits) {
            tooltip(new ItemStack(item), false);
            if (!BaitUtils.isBait(new ItemStack(item))) problem(id(item) + " is tagged as bait but has no bait data (can't go in a rod)");
        }
        notes.add(checked + " accessories each counted once on a rod");
    }

    private interface Setter { void set(ItemStack rod, ItemStack accessory); }

    private static int slotAdds(ItemStack accessory, Setter setter) {
        ItemStack rod = new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("stone_fishing_rod")));
        RodEffects bare = RodEffects.ofRod(rod);
        setter.set(rod, accessory.copy());
        RodEffects on = RodEffects.ofRod(rod);
        RodEffects expected = bare.plus(RodEffects.of(accessory));
        if (!close(on, expected))
            problem(describe(accessory) + " doesn't add up on a rod: expected " + expected + ", got " + on);
        tooltip(accessory, false);
        return 1;
    }

    private static void tooltip(ItemStack stack, boolean rod) {
        try {
            List<Component> lines = rod ? RodEffects.describeRod(stack) : RodEffects.describeItem(stack);
            for (Component c : lines) c.getString();
        } catch (Throwable t) {
            problem("tooltip for " + describe(stack) + " throws " + t);
        }
    }

    // ------------------------------------------------------------------ whole loadouts

    private static RodEffects allSituations(ItemStack rod) {
        RodEffects e = RodEffects.NONE;
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(RodEffects.parts(rod));
        for (ItemStack part : all) for (RodEffects.Perk p : RodEffects.situational(part)) e = e.plus(p.fx());
        return e;
    }

    private static RodEffects outfit(boolean full) {
        if (!full) return RodEffects.NONE;
        RodEffects e = RodEffects.FULL_SET;
        for (String g : List.of("angler_hat", "fishing_vest", "waders", "wading_boots"))
            e = e.plus(RodEffects.of(new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource(g)))));
        return e;
    }

    /**
     * Random full loadouts (rod, line, hook on a base, bobber, lure, a few kinds of bait in every slot, the full
     * outfit or none), with every place-and-time perk switched on at once: the most any stack could ever add up to.
     */
    private static void checkCombinations(ServerLevel level, List<Item> rods, List<Item> lines, List<ItemStack> hooks,
                                          List<Item> bobbers, List<Item> lures, List<Item> baits) {
        RandomSource rng = RandomSource.create(42);
        int n = 60_000;
        double minWait = 9, maxCast = 0, minFight = 9, maxLand = 0, maxTwin = 0, maxCook = 0, minBoost = 9, maxBoost = -9;
        int maxLuck = -99, minLuck = 99, maxSpeed = 0;
        String worstFight = "", worstWait = "", luckiest = "";
        Angler.Stats top = Angler.Stats.of(Angler.MAX_LEVEL), bottom = Angler.Stats.of(1);
        for (int i = 0; i < n; i++) {
            ItemStack rod = new ItemStack(rods.get(rng.nextInt(rods.size())));
            CustomRodManager.setLine(rod, new ItemStack(lines.get(rng.nextInt(lines.size()))));
            CustomRodManager.setHook(rod, hooks.get(rng.nextInt(hooks.size())).copy());
            CustomRodManager.setBobber(rod, new ItemStack(bobbers.get(rng.nextInt(bobbers.size()))));
            if (rng.nextInt(5) > 0) CustomRodManager.setLure(rod, new ItemStack(lures.get(rng.nextInt(lures.size()))));
            int slots = rod.getItem() instanceof TideFishingRodItem r ? r.getBaitSlots() : 0;
            List<ItemStack> bait = new ArrayList<>();
            for (int s = 0; s < slots; s++) if (rng.nextInt(4) > 0) bait.add(new ItemStack(baits.get(rng.nextInt(baits.size())), 16));
            if (!bait.isEmpty()) TideItemData.BAIT_CONTENTS.set(rod, new BaitContents(bait));

            RodEffects e;
            try {
                e = RodEffects.ofRod(rod).plus(allSituations(rod)).plus(outfit(rng.nextBoolean()));
            } catch (Throwable t) {
                problem("a loadout throws: " + loadout(rod) + ": " + t);
                continue;
            }
            if (!finite(e)) { problem("non-number in " + loadout(rod) + ": " + e); continue; }

            // bite time: the gear's factor, at both ends of the fishing level
            // (with Lure V and the most bait speed a rod can use)
            double wait = e.biteWaitFactor(5 + TideFishingRodItem.BAIT_SPEED_CAP + e.speed()) * top.biteWait();
            if (wait < minWait) { minWait = wait; worstWait = loadout(rod); }
            // how hard the fish fights: at max level, a common fish, the easiest it can get
            double fight = (1.0 + e.fight()) * Angler.fightSpeed(Angler.MAX_LEVEL, FishRarity.COMMON);
            if (1.0 + e.fight() < minFight) { minFight = 1.0 + e.fight(); worstFight = loadout(rod); }
            if (1.0 + e.fight() <= 0.15) problem("fish almost stop moving (fight x" + fmt(1 + e.fight()) + ") with " + loadout(rod));
            // hook zones: in order and on the bar, for the easiest and hardest fish
            for (double area : new double[]{0.05, 0.5, 1.0})
                for (CatchQuality q : CatchQuality.values())
                    for (Angler.Stats st : new Angler.Stats[]{top, bottom}) {
                        Angler.Zones z = Angler.Zones.of((float) Math.max(0.05, area * (1 - Math.min(0.9, e.ease()))), st.hookZones() * (1 + e.window()) * q.zoneScale());
                        if (!(z.perfect() >= 0 && z.perfect() <= z.great() && z.great() <= z.good() && z.good() <= z.bad() && z.bad() < 1))
                            problem("hook zones out of order " + z + " with " + loadout(rod));
                    }
            // catch quality odds with this boost, at level 1 and 99
            double boost = e.quality();
            minBoost = Math.min(minBoost, boost);
            maxBoost = Math.max(maxBoost, boost);
            for (int lv : new int[]{1, Angler.MAX_LEVEL}) {
                double[] p = CatchQuality.odds(lv, boost);
                double sum = Arrays.stream(p).sum();
                if (Math.abs(sum - 1) > 1e-6 || Arrays.stream(p).anyMatch(x -> !(x >= 0)))
                    problem("quality odds broken (sum " + sum + ") at level " + lv + " with boost " + fmt(boost) + ": " + loadout(rod));
            }
            maxLand = Math.max(maxLand, e.land());
            maxTwin = Math.max(maxTwin, e.twin());
            maxCook = Math.max(maxCook, e.cook());
            maxCast = Math.max(maxCast, (1 + e.cast()) * top.castDistance());
            int baitSpeed = Math.min(BaitUtils.getCombinedSpeed(rod), TideFishingRodItem.BAIT_SPEED_CAP);
            maxSpeed = Math.max(maxSpeed, baitSpeed + e.speed());
            int luck = e.luck() + BaitUtils.getCombinedLuck(rod) + top.luck() + 3;   // + Luck of the Sea III
            if (luck > maxLuck) { maxLuck = luck; luckiest = loadout(rod); }
            minLuck = Math.min(minLuck, e.luck() + BaitUtils.getCombinedLuck(rod) + bottom.luck());
            if (e.twin() > 1 || e.cook() > 1) problem("a chance over 100% with " + loadout(rod) + ": " + e);
        }
        double[] best = CatchQuality.odds(Angler.MAX_LEVEL, maxBoost), worst = CatchQuality.odds(1, minBoost);
        notes.add(String.format("%,d random loadouts, every place-and-time perk on at once", n));
        notes.add(String.format("fastest bites: waits x%s of normal (%s)", fmt(minWait), worstWait));
        notes.add(String.format("easiest fight: fish move at x%s (%s)", fmt(minFight), worstFight));
        notes.add(String.format("catch quality boost %s to %s: Mythic %s at level 99 best, %s at level 1 worst; Poor %s at level 1 worst",
                pct(minBoost), pct(maxBoost), pct(best[5]), pct(worst[5]), pct(worst[0])));
        notes.add(String.format("most fishing speed points %d; luck %d to %d (%s); cast range up to x%s", maxSpeed, minLuck, maxLuck, luckiest, fmt(maxCast)));
        notes.add(String.format("most: land %s, second fish %s, cooked %s", pct(maxLand), pct(maxTwin), pct(maxCook)));
        if (best[5] > 0.10) problem("Mythic chance reaches " + pct(best[5]) + " at level 99 with the best gear (should stay rare)");
        if (maxCast > 3.5) problem("cast range can reach x" + fmt(maxCast) + ", the bobber may fly out of sight");
    }

    // ------------------------------------------------------------------ which fish come

    /** Real fish selection near spawn: lures, bait and unlocks never give a broken weight, and luck doesn't let crates take over. */
    private static void checkFishPools(ServerLevel level, List<Item> rods, List<Item> lures, List<Item> baits) {
        BlockPos pos = level.getRespawnData().pos();
        Holder<Biome> biome = level.getBiome(pos);
        Collection<FishData> fish = TideData.FISH.get().values();
        Function<ItemStack, FishingContext> ctx = rod -> new FishingContext(level, null, rod, RandomSource.create(1), Vec3.atCenterOf(pos), pos,
                0, "water", biome, biome, level.dimension(), 0.5f, 0, SeasonsCompat.getSeason(level));
        ItemStack plain = new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("iron_fishing_rod")));
        double baseTotal = pool(ctx.apply(plain), fish, null);
        if (baseTotal <= 0) notes.add("no fish bite at the test spawn with a plain rod (fine for a dry spawn)");

        // every lure, every bait, every rod, alone
        double maxFactor = 0; String maxWho = "";
        List<ItemStack> singles = new ArrayList<>();
        for (Item l : lures) { ItemStack r = plain.copy(); CustomRodManager.setLure(r, new ItemStack(l)); singles.add(r); }
        for (Item b : baits) { ItemStack r = plain.copy(); TideItemData.BAIT_CONTENTS.set(r, new BaitContents(List.of(new ItemStack(b, 16)))); singles.add(r); }
        for (Item rodItem : rods) singles.add(new ItemStack(rodItem));
        for (ItemStack r : singles) {
            try {
                FishingContext c = ctx.apply(r);
                pool(c, fish, loadout(r));
                for (FishData d : fish) {
                    double f = Lures.fishFactor(c, d);
                    if (!(f > 0) || Double.isInfinite(f)) problem("fish factor " + f + " for " + d.fish().getRegisteredName() + " with " + loadout(r));
                    if (f > maxFactor) { maxFactor = f; maxWho = loadout(r) + " on " + d.fish().getRegisteredName(); }
                }
            } catch (Throwable t) {
                problem("picking a fish throws with " + loadout(r) + ": " + t);
            }
        }
        // everything that pulls at once: the strongest pull a single fish can get
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("wayfinder_fishing_rod")));
        CustomRodManager.setLure(stack, new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("echo_lure"))));
        CustomRodManager.setHook(stack, new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("amethyst_hook"))));
        TideItemData.BAIT_CONTENTS.set(stack, new BaitContents(baits.stream().limit(6).map(b -> new ItemStack(b, 16)).toList()));
        FishingContext all = ctx.apply(stack);
        for (FishData d : fish) maxFactor = Math.max(maxFactor, Lures.fishFactor(all, d));
        notes.add("strongest pull on one fish: x" + fmt(maxFactor) + " (" + maxWho + ")");
        if (maxFactor > 60) problem("one fish can be pulled x" + fmt(maxFactor) + ", it would crowd out everything else");

        // what you reel in at each luck: Tide picks fish (85 - luck), crates (4 + luck) or junk and treasure first
        CrateSelector crates = new CrateSelector();
        com.li64.tide.data.fishing.selector.FishSelector fishSel = new com.li64.tide.data.fishing.selector.FishSelector();
        for (int luck : new int[]{0, 5, 10, 17}) {
            FishingContext c = new FishingContext(level, null, plain, RandomSource.create(1), Vec3.atCenterOf(pos), pos,
                    luck, "water", biome, biome, level.dimension(), 0.5f, 0, SeasonsCompat.getSeason(level));
            double f = fishSel.weight(c), cr = crates.weight(c), loot = 0;
            for (var l : TideData.FISHING_LOOT.get().values()) if (l.shouldKeep(c)) loot += l.weight(c);
            double total = f + cr + loot;
            notes.add(String.format("luck %d at spawn: %s fish, %s crates, %s junk and treasure", luck, pct(f / total), pct(cr / total), pct(loot / total)));
            if (luck <= 10 && f / total < 0.5) problem("at luck " + luck + " fewer than half of bites are fish");
        }
        oddsReport(level.getServer(), fish);
        // unlocks resolve to real biome tags
        for (Item item : BuiltInRegistries.ITEM) {
            for (Component c : Unlocks.describe(new ItemStack(item))) {
                if (c.getString().isBlank()) problem(id(item) + " has an unlock with no text");
            }
        }
        List<String> fishJson = fish.stream().map(d -> FishData.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, d).result().map(Object::toString).orElse("")).toList();
        for (String tag : List.of("has_jungle_fish", "has_swamp_fish", "has_desert_fish", "has_frozen_fish", "has_mushroom_fish", "has_cherry_grove_fish",
                "has_coastal_fish", "has_dripstone_fish", "has_deep_dark_fish")) {
            TagKey<Biome> key = TagKey.create(Registries.BIOME, Tide.resource(tag));
            if (level.registryAccess().lookupOrThrow(Registries.BIOME).get(key).map(h -> h.size() == 0).orElse(true))
                problem("regional bait points at #tide:" + tag + ", which has no biomes");
            boolean used = fishJson.stream().anyMatch(js -> js.contains("#tide:" + tag));
            if (!used) problem("regional bait for #tide:" + tag + " unlocks nothing (no fish need that region)");
        }
    }

    /**
     * The real chances for the family to look at: catch quality by level, and each legendary fish in the best
     * place for it (every biome, depth and dimension tried; time, weather and moon taken as right for it).
     */
    /** A loadout in a place: what it adds up to (its own stats, the perks for those places and times, full rigs). */
    private static RodEffects inPlace(ItemStack rod, java.util.Set<RodEffects.When> whens) {
        RodEffects e = RodEffects.ofRod(rod);
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(RodEffects.parts(rod));
        for (ItemStack part : all)
            for (RodEffects.Perk perk : RodEffects.situational(part))
                if (whens.contains(perk.when())) e = e.plus(perk.fx());
        for (var set : com.li64.tide.data.rods.RigSets.SETS)
            if (whens.contains(set.when())) e = e.plus(set.bonus(com.li64.tide.data.rods.RigSets.counted(set, rod)));
        return e;
    }

    private static ItemStack kit(String rod, String line, String hook, String bobber, String lure) {
        ItemStack r = new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource(rod)));
        CustomRodManager.setLine(r, new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource(line))));
        CustomRodManager.setHook(r, new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource(hook))));
        CustomRodManager.setBobber(r, new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource(bobber))));
        CustomRodManager.setLure(r, new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource(lure))));
        return r;
    }

    /** Quality odds with the gear's quality boost applied. */
    private static String qualityLine(String label, int lv, RodEffects e) {
        double[] p = CatchQuality.odds(lv, e.quality()).clone();
        double good = p[2] + p[3] + p[4] + p[5], great = p[3] + p[4] + p[5];
        return String.format("%s: luck %+d, bite %+.0f%%, quality %+.0f%% -> Good+ %s, Great+ %s, Trophy+ %s, Mythic 1 in %,d",
                label, e.luck(), e.bite() * 100, e.quality() * 100, pct(good), pct(great), pct(p[4] + p[5]), Math.round(1 / p[5]));
    }

    private static void oddsReport(MinecraftServer server, Collection<FishData> fish) {
        // the amethyst pieces against a place's rig, at the baseline level: the rig should win in its place, and only there
        int base = Angler.baseline();
        ItemStack amethyst = kit("amethyst_fishing_rod", "amethyst_line", "amethyst_hook", "amethyst_bobber", "spinner_lure");
        ItemStack jungle = kit("bamboo_fishing_rod", "vine_line", "amethyst_hook", "apple_bobber", "spinner_lure");
        ItemStack river = kit("amethyst_fishing_rod", "silk_line", "wooden_hook", "feather_bobber", "fly_lure");
        var day = RodEffects.When.DAY;
        notes.add("balance " + qualityLine("amethyst kit, jungle by day", base, inPlace(amethyst, java.util.Set.of(RodEffects.When.JUNGLE, day))));
        notes.add("balance " + qualityLine("jungle rig + amethyst hook, jungle by day", base, inPlace(jungle, java.util.Set.of(RodEffects.When.JUNGLE, day))));
        notes.add("balance " + qualityLine("jungle rig + amethyst hook, plains by day", base, inPlace(jungle, java.util.Set.of(day))));
        notes.add("balance " + qualityLine("amethyst kit, river by day", base, inPlace(amethyst, java.util.Set.of(RodEffects.When.RIVER, day))));
        notes.add("balance " + qualityLine("river rig (amethyst rod), river by day", base, inPlace(river, java.util.Set.of(RodEffects.When.RIVER, day))));

        for (int lv : new int[]{1, Angler.baseline(), Angler.MAX_LEVEL})
            for (double boost : new double[]{0, 0.95}) {
                double[] p = CatchQuality.odds(lv, boost);
                notes.add(String.format("quality at level %d%s: Poor %s, Average %s, Good %s, Great %s, Trophy %s, Mythic 1 in %,d", lv,
                        boost > 0 ? " with the best quality gear" : "", pct(p[0]), pct(p[1]), pct(p[2]), pct(p[3]), pct(p[4]), Math.round(1 / p[5])));
            }
        ItemStack anyTime = new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("clockwork_fishing_rod")));
        CustomRodManager.setLure(anyTime, new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("storm_lure"))));
        TideItemData.BAIT_CONTENTS.set(anyTime, new BaitContents(List.of(new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("moon_bait")), 16))));
        var biomes = server.overworld().registryAccess().lookupOrThrow(Registries.BIOME).listElements().toList();
        com.li64.tide.data.fishing.selector.FishSelector fishSel = new com.li64.tide.data.fishing.selector.FishSelector();
        CrateSelector crates = new CrateSelector();
        record Spot(String medium, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim, int[] ys) {}
        List<Spot> spots = List.of(new Spot("water", net.minecraft.world.level.Level.OVERWORLD, new int[]{-50, 20, 64, 120}),
                new Spot("lava", net.minecraft.world.level.Level.OVERWORLD, new int[]{-50}), new Spot("lava", net.minecraft.world.level.Level.NETHER, new int[]{32}),
                new Spot("void", net.minecraft.world.level.Level.END, new int[]{-64}), new Spot("water", net.minecraft.world.level.Level.END, new int[]{60}));
        for (FishData legend : fish) {
            if (legend.profile().rarity() != FishRarity.LEGENDARY) continue;
            for (int luck : new int[]{0, 10}) {
                double best = 0; String where = "";
                for (Spot spot : spots) {
                    ServerLevel lvl = server.getLevel(spot.dim());
                    if (lvl == null) continue;
                    for (var b : biomes)
                        for (int y : spot.ys())
                            for (int moon : new int[]{0, 4}) {
                                BlockPos bp = new BlockPos(0, y, 0);
                                FishingContext c = new FishingContext(lvl, null, anyTime, RandomSource.create(1), Vec3.atCenterOf(bp), bp, luck, spot.medium(),
                                        b, b, spot.dim(), b.value().getBaseTemperature(), moon, SeasonsCompat.getSeason(lvl));
                                if (!legend.shouldKeep(c)) continue;
                                double pool = 0;
                                for (FishData d : fish) if (d.shouldKeep(c)) pool += d.weight(c);
                                double f = fishSel.weight(c), others = crates.weight(c);
                                for (var l : TideData.FISHING_LOOT.get().values()) if (l.shouldKeep(c)) others += l.weight(c);
                                double p = pool > 0 ? legend.weight(c) / pool * f / (f + others) : 0;
                                if (p > best) { best = p; where = spot.medium() + " in " + b.unwrapKey().map(k -> k.identifier().getPath()).orElse("?") + " at y " + y; }
                            }
                }
                double mythic = CatchQuality.odds(Angler.MAX_LEVEL, 0)[5];
                notes.add(String.format("legendary %s, luck %d: %s", legend.fish().getRegisteredName(), luck,
                        best > 0 ? String.format("1 in %,d bites at best (%s); a Mythic one at level 99: 1 in %,d", Math.round(1 / best), where, Math.round(1 / (best * mythic)))
                                : "can't be caught in any spot tried"));
            }
        }
    }

    /** Total weight of the fish that can bite here; problems for broken weights. */
    private static double pool(FishingContext c, Collection<FishData> fish, String who) {
        double total = 0;
        for (FishData d : fish) {
            if (!d.shouldKeep(c)) continue;
            double w = d.weight(c);
            if (!(w >= 0) || Double.isInfinite(w)) problem("fish weight " + w + " for " + d.fish().getRegisteredName() + (who == null ? "" : " with " + who));
            else total += w;
        }
        return total;
    }

    // ------------------------------------------------------------------ rods wear out but don't break

    /** Every Tide rod repairs with string, and wearing one out leaves it broken (one point left), not gone. */
    private static void rodRepairCheck(MinecraftServer server) {
        int n = 0;
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack rod = new ItemStack(item);
            if (!rod.is(com.li64.tide.data.TideTags.Items.FISHING_RODS) || !BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("tide")) continue;
            n++;
            if (!(item instanceof com.li64.tide.registries.items.TideFishingRodItem)) { problem(id(item) + " isn't a Tide rod (it would break for good)"); continue; }
            if (!rod.isValidRepairItem(new ItemStack(Items.STRING))) problem(id(item) + " can't be repaired with string");
            if (!rod.isDamageableItem()) continue;
            rod.setDamageValue(rod.getMaxDamage() - 3);
            rod.hurtAndBreak(50, server.overworld(), null, broken -> {});
            if (rod.isEmpty() || rod.getDamageValue() != rod.getMaxDamage() - 1 || !com.li64.tide.registries.items.TideFishingRodItem.isBroken(rod))
                problem(id(item) + " worn out: " + (rod.isEmpty() ? "gone" : "damage " + rod.getDamageValue() + "/" + rod.getMaxDamage()));
        }
        notes.add("rods: " + n + " Tide rods repair with string and stop at broken instead of breaking");
        // records: a tie on the scale (same weight as shown) keeps the old record
        var fw = com.li64.tide.data.angler.FishWeight.class;
        notes.add("weights: 4.4 g = " + com.li64.tide.data.angler.FishWeight.imperial(4.4) + ", 4.6 g = " + com.li64.tide.data.angler.FishWeight.imperial(4.6)
                + ", 300 g = " + com.li64.tide.data.angler.FishWeight.imperial(300) + ", 2 kg = " + com.li64.tide.data.angler.FishWeight.imperial(2000));
        if (com.li64.tide.data.angler.FishWeight.visiblyHeavier(4.6, 4.4)) problem("4.6 g beats a 4.4 g record though both read " + com.li64.tide.data.angler.FishWeight.imperial(4.6));
        if (!com.li64.tide.data.angler.FishWeight.visiblyHeavier(9, 4.4)) problem("9 g doesn't beat a 4.4 g record");
    }

    // ------------------------------------------------------------------ the angling table's recipes

    /** Fishing gear is made at the Angling Table only: none of it at the crafting or smithing table, all of it at the table. */
    private static void benchCheck(MinecraftServer server) {
        var entries = com.li64.tide.data.tackle.TackleRecipes.entries();
        Map<com.li64.tide.data.tackle.TackleRecipes.Kind, Integer> byKind = new EnumMap<>(com.li64.tide.data.tackle.TackleRecipes.Kind.class);
        for (var e : entries) byKind.merge(e.kind(), 1, Integer::sum);
        notes.add("angling table: " + entries.size() + " recipes " + byKind + ", " + entries.stream().filter(e -> e.smithing()).count() + " of them rod upgrades");
        int left = 0;
        for (RecipeHolder<?> h : server.getRecipeManager().getRecipes()) {
            if (!h.id().identifier().getNamespace().equals("tide")) continue;
            if (!(h.value() instanceof CraftingRecipe) && !(h.value() instanceof net.minecraft.world.item.crafting.SmithingTransformRecipe)) continue;
            for (var e : entries) if (e.holder().id().equals(h.id())) problem("recipe " + h.id().identifier() + " is still at the crafting table too");
            left++;
        }
        notes.add("angling table: " + left + " tide recipes left at the crafting/smithing table");
        for (String must : List.of("tide:diamond_hook", "tide:iron_fishing_rod", "tide:fishing_line", "tide:red_bobber", "tide:bait", "tide:tackle_box", "tide:spinner_lure"))
            if (entries.stream().noneMatch(e -> BuiltInRegistries.ITEM.getKey(e.result()).toString().equals(must))) problem("the angling table can't make " + must);
        for (String not : List.of("tide:angling_table", "minecraft:fishing_rod", "minecraft:raw_iron"))
            if (entries.stream().anyMatch(e -> BuiltInRegistries.ITEM.getKey(e.result()).toString().equals(not))) problem("the angling table makes " + not + ", which belongs at the crafting table");
        // discovery: what string alone opens up, what a little more does, and what a few recipes ask for
        java.util.function.Function<List<Item>, List<String>> by = items -> com.li64.tide.data.tackle.TackleUnlocks.unlockedBy(new HashSet<>(items));
        List<String> stringOnly = by.apply(List.of(Items.STRING));
        notes.add("discovery: string alone unlocks " + stringOnly);
        List<String> jungle = by.apply(List.of(Items.STRING, Items.STICK, Items.BAMBOO));
        if (!jungle.contains("bamboo_fishing_rod")) problem("bamboo doesn't unlock the bamboo rod");
        if (stringOnly.contains("redstone_line") || stringOnly.contains("bamboo_fishing_rod")) problem("string alone unlocks too much: " + stringOnly);
        List<String> hooks = by.apply(List.of(Items.DIAMOND));
        if (hooks.contains("diamond_hook")) problem("a diamond alone unlocks the diamond hook (it should need a Tier 1 hook too)");
        if (!by.apply(List.of(Items.DIAMOND, BuiltInRegistries.ITEM.getValue(Tide.resource("fish_bone_hook")))).contains("diamond_hook")) problem("a fish bone hook and a diamond don't unlock the diamond hook");
        for (var e : entries)
            if (List.of("redstone_line", "bamboo_fishing_rod", "diamond_hook", "iron_rod_smithing", "spinner_lure", "bait", "fishing_line").contains(e.id().getPath()))
                notes.add("discovery: " + e.id().getPath() + " needs " + com.li64.tide.data.tackle.TackleUnlocks.needs(e));
        notes.add("discovery: everything unlocked with all materials: " + by.apply(BuiltInRegistries.ITEM.stream().toList()).size() + "/" + entries.size());

        // a Tier 2 hook made at the table remembers its Tier 1 hook
        var diamond = entries.stream().filter(e -> BuiltInRegistries.ITEM.getKey(e.result()).getPath().equals("diamond_hook")).findFirst().orElse(null);
        if (diamond != null && diamond.holder().value() instanceof ShapelessRecipe r) {
            List<ItemStack> in = new ArrayList<>();
            for (Ingredient i : diamond.ingredients())
                in.add(i.test(new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("fish_bone_hook")))) ? new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("fish_bone_hook")))
                        : new ItemStack(i.items().findFirst().orElseThrow().value()));
            ItemStack out = r.assemble(CraftingInput.of(in.size(), 1, in));
            if (!"fish_bone_hook".equals(RodEffects.baseHook(out))) problem("diamond hook made on a fish bone hook at the table forgot it: " + RodEffects.baseHook(out));
            else notes.add("angling table: diamond hook on a bone hook keeps it");
        }
    }

    // ------------------------------------------------------------------ recipes

    /** Every tackle recipe makes what it says, no two recipes take the same items, and upgrades keep their base hook. */
    private static void checkRecipes(MinecraftServer server, Map<TackleLog.Category, List<Item>> kinds) {
        ServerLevel level = server.overworld();
        List<RecipeHolder<CraftingRecipe>> crafting = new ArrayList<>();
        for (RecipeHolder<?> h : com.li64.tide.data.tackle.TackleRecipes.withMoved(server.getRecipeManager().getRecipes()))
            if (h.value() instanceof CraftingRecipe) {
                @SuppressWarnings("unchecked") RecipeHolder<CraftingRecipe> c = (RecipeHolder<CraftingRecipe>) h;
                crafting.add(c);
            }
        Set<Item> made = new HashSet<>();
        int tried = 0, clashes = 0;
        Set<String> reported = new HashSet<>();
        for (RecipeHolder<CraftingRecipe> h : crafting) {
            if (!h.id().identifier().getNamespace().equals("tide")) continue;
            for (CraftingInput input : inputs(h.value())) {
                tried++;
                ItemStack out = h.value().assemble(input);
                if (out.isEmpty()) { problem("recipe " + h.id().identifier() + " makes nothing"); continue; }
                made.add(out.getItem());
                for (RecipeHolder<CraftingRecipe> other : crafting) {
                    if (other == h || !other.value().matches(input, level)) continue;
                    ItemStack o = other.value().assemble(input);
                    String pair = h.id().identifier().compareTo(other.id().identifier()) < 0 ? h.id().identifier() + "|" + other.id().identifier() : other.id().identifier() + "|" + h.id().identifier();
                    if (!ItemStack.isSameItem(o, out) && reported.add(pair)) {
                        clashes++;
                        problem("recipes clash: " + h.id().identifier() + " and " + other.id().identifier() + " take the same items (" + out.getItem() + " / " + o.getItem() + ")");
                    }
                }
                // an upgraded hook made on a base hook remembers it
                if (RodEffects.UPGRADED_HOOKS.contains(id(out.getItem()))) {
                    String base = input.items().stream().filter(s -> !s.isEmpty() && RodEffects.BASE_HOOKS.contains(id(s.getItem()))).map(s -> id(s.getItem())).findFirst().orElse(null);
                    String kept = RodEffects.baseHook(out);
                    if (base != null && !base.equals("fishing_hook") && !base.equals(kept))
                        problem(id(out.getItem()) + " made on a " + base + " forgets it (got " + kept + ")");
                }
            }
        }
        // every tackle item can be made or found
        for (var set : com.li64.tide.data.rods.RigSets.SETS)
            for (String m : set.members())
                if (BuiltInRegistries.ITEM.getOptional(Tide.resource(m)).isEmpty()) problem(set.name() + " lists " + m + ", which isn't an item");
        Set<String> found = new java.util.HashSet<>(com.li64.tide.data.tackle.TackleLog.FOUND.keySet());
        found.addAll(Set.of("echo_fishing_rod", "blazing_fishing_rod", "prismarine_fishing_rod", "honeycomb_fishing_rod", "sunflower_fishing_rod",
                "village_fishing_rod", "fish_satchel", "magnetic_bait", "abyss_bait", "incandescent_bait", "enchanted_golden_apple_bobber"));
        for (RecipeHolder<?> h : com.li64.tide.data.tackle.TackleRecipes.withMoved(server.getRecipeManager().getRecipes()))
            if (h.value() instanceof net.minecraft.world.item.crafting.SmithingTransformRecipe s) {
                List<Ingredient> ing = s.placementInfo().ingredients();
                if (ing.size() < 3) continue;
                ItemStack[] in = ing.stream().map(i -> i.items().findFirst().map(x -> new ItemStack(x.value())).orElse(ItemStack.EMPTY)).toArray(ItemStack[]::new);
                try {
                    ItemStack out = s.assemble(new net.minecraft.world.item.crafting.SmithingRecipeInput(in[0], in[1], in[2]));
                    if (!out.isEmpty()) made.add(out.getItem());
                } catch (Throwable t) {
                    if (h.id().identifier().getNamespace().equals("tide")) problem("smithing recipe " + h.id().identifier() + " throws " + t);
                }
            }
        for (var e : kinds.entrySet())
            for (Item item : e.getValue()) {
                String p = id(item);
                if (!made.contains(item) && !found.contains(p)) problem(p + " has no recipe and isn't on the list of found items");
            }
        notes.add(tried + " recipe inputs tried against every crafting recipe, " + clashes + " clashes");
    }

    /** Inputs for a recipe: every combination of its ingredients' items, up to a few dozen. */
    private static List<CraftingInput> inputs(CraftingRecipe r) {
        int w, hgt;
        List<Optional<Ingredient>> slots;
        if (r instanceof ShapedRecipe s) { w = s.getWidth(); hgt = s.getHeight(); slots = s.getIngredients(); }
        else if (r instanceof ShapelessRecipe) {
            List<Ingredient> ing = r.placementInfo().ingredients();
            w = Math.min(3, ing.size()); hgt = (ing.size() + 2) / 3;
            slots = new ArrayList<>(ing.stream().map(Optional::of).toList());
            while (slots.size() < w * hgt) slots.add(Optional.empty());
        } else return List.of();
        List<List<Item>> options = slots.stream().map(o -> o.map(i -> i.items().map(Holder::value).toList()).orElse(List.of(Items.AIR))).toList();
        List<CraftingInput> out = new ArrayList<>();
        int combos = 1;
        for (List<Item> o : options) combos = Math.min(64, combos * Math.max(1, o.size()));
        for (int k = 0; k < combos; k++) {
            List<ItemStack> items = new ArrayList<>();
            int rem = k;
            for (List<Item> o : options) {
                Item it = o.get(rem % o.size());
                rem /= Math.max(1, o.size());
                items.add(it == Items.AIR ? ItemStack.EMPTY : new ItemStack(it));
            }
            out.add(CraftingInput.of(w, hgt, items));
        }
        return out;
    }

    // ------------------------------------------------------------------ data and cooking

    private static void checkData(ServerLevel level, Map<TackleLog.Category, List<Item>> kinds) {
        for (Item line : kinds.getOrDefault(TackleLog.Category.LINES, List.of()))
            if (com.li64.tide.data.rods.AccessoryData.get(new ItemStack(line)) == null) problem(id(line) + " has no rod_accessories entry (no line colour)");
        for (Item bait : kinds.getOrDefault(TackleLog.Category.BAIT, List.of()))
            if (!new ItemStack(bait).is(TideTags.Items.BAIT_ITEMS)) problem(id(bait) + " is bait but not in #tide:bait_items");
        for (Item rod : kinds.getOrDefault(TackleLog.Category.RODS, List.of()))
            if (!new ItemStack(rod).is(TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("enchantable/fishing"))))
                problem(id(rod) + " can't take fishing enchantments");
        for (String key : List.of("honeycomb_fishing_rod", "mangrove_fishing_rod", "cherry_fishing_rod"))
            for (RodEffects.Drop d : RodEffects.drops(new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource(key))))) {
                Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(d.item()));
                if (item == Items.AIR) problem(key + " drops " + d.item() + ", which isn't an item");
            }
    }

    /** Every fish cooks on the line (redstone line, blazing rod) into the same thing a furnace makes, keeping its quality. */
    private static void checkCooking(ServerLevel level) {
        int cooked = 0, raw = 0;
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack fish = new ItemStack(item);
            if (!fish.is(TideTags.Items.FISH)) continue;
            CatchQuality.mark(fish, CatchQuality.TROPHY);
            ItemStack out;
            try {
                out = TideFishingHook.cookOnTheLine(level, fish);
            } catch (Throwable t) {
                problem("cooking " + id(item) + " on the line throws " + t);
                continue;
            }
            if (out.isEmpty()) problem("cooking " + id(item) + " gives nothing");
            else if (out.getItem() == item) raw++;
            else {
                cooked++;
                if (TideItemData.FISH_QUALITY.getOrDefault(out, -1) != CatchQuality.TROPHY.ordinal())
                    problem("cooked " + id(item) + " loses its quality");
            }
        }
        notes.add(cooked + " fish cook on the line, " + raw + " stay raw (no furnace recipe)");
    }

    // ------------------------------------------------------------------ helpers

    private static boolean close(RodEffects a, RodEffects b) {
        double[] x = values(a), y = values(b);
        for (int i = 0; i < x.length; i++) if (Math.abs(x[i] - y[i]) > 1e-9) return false;
        return true;
    }

    private static boolean finite(RodEffects e) {
        return Arrays.stream(values(e)).allMatch(Double::isFinite);
    }

    private static double[] values(RodEffects e) {
        return new double[]{e.cast(), e.bite(), e.window(), e.fight(), e.luck(), e.quality(), e.land(), e.xp(), e.twin(), e.cook(), e.speed(), e.ease()};
    }

    private static String describe(ItemStack s) {
        String base = RodEffects.baseHook(s);
        return id(s.getItem()) + (base != null ? " on " + base : "");
    }

    private static String loadout(ItemStack rod) {
        List<String> parts = new ArrayList<>(List.of(id(rod.getItem())));
        for (ItemStack p : RodEffects.parts(rod)) parts.add(describe(p));
        return parts.stream().distinct().collect(Collectors.joining(" + "));
    }

    private static String fmt(double v) {
        return String.format("%.2f", v);
    }

    private static String pct(double v) {
        return v < 0.01 && v > 0 ? String.format("%.3f%%", v * 100) : String.format("%.1f%%", v * 100);
    }
}
