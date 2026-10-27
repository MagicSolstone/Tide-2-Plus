package com.li64.tide.data.rods;

import com.li64.tide.registries.items.FishingBobberItem;
import com.li64.tide.registries.items.FishingHookItem;
import com.li64.tide.registries.items.FishingLineItem;
import com.li64.tide.registries.items.FishingLureItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * What a rod, its line, hook, bobber and lure, and the angler's gear add to fishing. Every line gives
 * casting range and every hook a wider hook window (so even plain ones are worth attaching); the rest
 * have their own perks, some only in a place or at a time ({@link When}). Everything stacks; bite speed
 * gives half value past +50% so a big stack is fast but never instant.
 *
 * All fractions: 0.10 = +10%. {@code fight} below zero makes fish easier; {@code land} adds to the
 * chance that a Good or Bad hook still lands the fish; {@code twin} is the chance of a second fish of
 * the same kind; {@code cook} the chance the fish comes up already cooked; {@code speed} is original Tide's
 * whole-number fishing speed (like the Lure enchantment: each point bites about 5 seconds sooner) and
 * {@code ease} lowers catch difficulty (a bigger catch area in the minigame). {@code quality} shifts the odds
 * toward better fish smoothly (there is no separate "raise the tier" chance any more).
 *
 * <p>HOW TO CHANGE THINGS HERE (see CODE_GUIDE.md): each item's always-on perks are one line in the tables below
 * ({@code RODS}, {@code HOOKS}, {@code LINES}, {@code BOBBERS}, {@code LURES}, {@code BAITS}), written with helpers
 * like {@code bite(0.10)}; perks that only work somewhere or sometime are in {@code SITUATIONAL}; the helpers
 * ({@code bite}, {@code window}, {@code luck}...) are defined below the gear table. Rigs are in {@link RigSets}.
 */
public record RodEffects(double cast, double bite, double window, double fight, int luck, double quality,
                         double land, double xp, double twin, double cook, int speed, double ease) {
    public RodEffects(double cast, double bite, double window, double fight, int luck, double quality, double land, double xp) {
        this(cast, bite, window, fight, luck, quality, land, xp, 0, 0, 0, 0);
    }

    public RodEffects(double cast, double bite, double window, double fight, int luck, double quality, double land, double xp, double twin, double cook) {
        this(cast, bite, window, fight, luck, quality, land, xp, twin, cook, 0, 0);
    }

    /** Each point of "speed" (the Lure enchantment, bait, a rod's own) is worth this much bite speed. */
    public static final double SPEED_POINT = 0.06;

    /**
     * Stacked bite speed (and rare finds) with diminishing returns instead of a hard cap: every point counts in full
     * up to +50%, half as much from +50% to +100%, and a quarter as much past that. So the best possible rig is still
     * noticeably better than a good one, but never runs away.
     */
    public static double softenBite(double raw) {
        if (raw <= 0.5) return raw;
        if (raw <= 1.0) return 0.5 + (raw - 0.5) * 0.5;
        return 0.75 + (raw - 1.0) * 0.25;
    }

    /**
     * Each point of luck is this much "rare finds": every rare fish becomes that share more likely than its own normal
     * chance (+15% makes a 2-in-100 fish about 2.3-in-100; it is never a chance of getting a rare fish). Luck points stay
     * whole numbers inside (bait files, the Midas fish's requirement, vanilla loot); players only ever see the percentage.
     */
    public static final double RARE_PER_LUCK = 0.15;

    /** All the luck on a rod and angler, as the rare-finds bonus it really gives (softened like bite speed: no hard cap). */
    public static double rareFinds(int luck) {
        return softenBite(luck * RARE_PER_LUCK);
    }

    public static final RodEffects NONE = new RodEffects(0, 0, 0, 0, 0, 0, 0, 0);

    private static final RodEffects ANY_LINE = cast(0.10);
    private static final RodEffects ANY_HOOK = window(0.05);

    // ------------------------------------------------------------------ rods

    /** Rods' own perks. Tiers climb from nothing (wood, stone) to netherite; the special rods each lean one way. */
    private static final Map<String, RodEffects> RODS = Map.ofEntries(
            Map.entry("iron_fishing_rod", window(0.05)),
            Map.entry("golden_fishing_rod", new RodEffects(0, 0, 0, 0, 1, 0, 0, 0.10)),
            Map.entry("crystal_fishing_rod", new RodEffects(0, 0, 0.10, 0, 0, 0, 0.10, 0)),      // sees what bites (FishCatchMinigame); helps land the hard ones
            Map.entry("diamond_fishing_rod", new RodEffects(0.10, 0, 0.10, 0, 0, 0.10, 0, 0)),
            Map.entry("netherite_fishing_rod", new RodEffects(0.10, 0.05, 0.12, -0.12, 0, 0.15, 0.10, 0)),
            Map.entry("echo_fishing_rod", new RodEffects(0, 0, 0.05, 0, 0, 0, 0, 0.10)),
            Map.entry("prismarine_fishing_rod", window(0.05)),
            Map.entry("village_fishing_rod", xp(0.15)),
            Map.entry("blazing_fishing_rod", new RodEffects(0, 0, 0, -0.08, 0, 0, 0, 0, 0, 0.15)),
            Map.entry("honeycomb_fishing_rod", land(0.15)),
            Map.entry("midas_fishing_rod", new RodEffects(0, 0, 0, 0, 1, 0.10, 0, 0.10)),
            Map.entry("kids_fishing_pole", new RodEffects(0, 0.10, 0.15, -0.20, -1, -0.30, 0.10, 0)),
            Map.entry("bamboo_fishing_rod", cast(0.05)),
            Map.entry("amethyst_fishing_rod", NONE),      // its bite speed follows the sun: see amethyst()
            Map.entry("cherry_fishing_rod", xp(0.15)),
            Map.entry("conduit_fishing_rod", new RodEffects(0, 0, 0.10, -0.05, 0, 0.05, 0, 0)),
            Map.entry("clockwork_fishing_rod", new RodEffects(0, 0, 0.05, 0, 0, 0, 0, 0.10)),
            Map.entry("wayfinder_fishing_rod", new RodEffects(0.10, 0, 0.10, -0.10, 0, 0, 0.05, 0)),     // plus more the farther from spawn: see wayfinder()
            // found rods: each one's real draw is its own trick (PICKS, Specials), the numbers stay modest
            Map.entry("rotten_fishing_rod", xp(0.05)),
            Map.entry("widow_fishing_rod", NONE),
            Map.entry("sickle_fishing_rod", xp(0.10)),
            Map.entry("grove_fishing_rod", window(0.05)),
            Map.entry("drowned_fishing_rod", cast(0.05)),
            Map.entry("hex_fishing_rod", xp(0.10)));

    // ------------------------------------------------------------------ hooks, lines, bobbers, lures

    /** Hooks with their own perk, on top of every hook's wider window. */
    private static final Map<String, RodEffects> HOOKS = Map.ofEntries(
            Map.entry("stone_hook", fight(-0.03)),      // sinks to the bottom: brings up seaweed (PART_DROPS)
            Map.entry("copper_hook", bite(0.05)),
            Map.entry("golden_hook", xp(0.10)),
            Map.entry("diamond_hook", new RodEffects(0, 0, 0.15, 0, 0, 0.05, 0.05, 0)),
            Map.entry("emerald_hook", xp(0.10)),      // and the odd emerald (PART_DROPS)
            Map.entry("amethyst_hook", NONE),
            Map.entry("netherite_hook", new RodEffects(0, 0, 0.10, -0.10, 0, 0.05, 0, 0)),
            Map.entry("prismarine_hook", bite(0.05)),
            Map.entry("glow_hook", bite(0.12)),
            Map.entry("feathered_hook", new RodEffects(0.05, 0.08, 0, 0, 0, 0, 0, 0)),
            Map.entry("barbed_hook", NONE),      // its trick: fish never shake loose, but no perfect hooks (FishCatchMinigame.handleHook)
            Map.entry("treble_hook", new RodEffects(0, 0, 0, 0.05, 0, 0, 0, 0, 0.12, 0)),
            Map.entry("circle_hook", new RodEffects(0, 0, 0.05, -0.05, 0, 0, 0.40, 0)),      // the landing hook: sets deep, so Good and Bad hooks hold and the fish tires sooner
            Map.entry("fish_bone_hook", new RodEffects(0, 0, 0, -0.05, 0, 0, 0.05, 0)),
            Map.entry("lucky_hook", new RodEffects(0, 0, 0, 0, 1, 0, 0, 0)));

    /** Hooks made from raw materials: an upgrade made on one of these keeps its perks (iron is the plain one). */
    public static final java.util.Set<String> BASE_HOOKS = java.util.Set.of(
            "fishing_hook", "wooden_hook", "stone_hook", "copper_hook", "golden_hook", "fish_bone_hook");
    /** Hooks made by adding something to a base hook. */
    public static final java.util.Set<String> UPGRADED_HOOKS = java.util.Set.of(
            "diamond_hook", "emerald_hook", "amethyst_hook", "netherite_hook", "prismarine_hook", "glow_hook",
            "feathered_hook", "barbed_hook", "circle_hook", "lucky_hook", "treble_hook",
            "fiery_hook", "permafrost_hook", "twilight_hook", "lavaproof_hook", "void_hook");

    /** Called when a shapeless recipe makes something: an upgraded hook remembers the base hook it was made on. */
    public static void onHookCrafted(List<ItemStack> inputs, ItemStack result) {
        if (result == null || result.isEmpty() || !isTide(result) || !UPGRADED_HOOKS.contains(path(result))) return;
        for (ItemStack in : inputs)
            if (!in.isEmpty() && isTide(in) && BASE_HOOKS.contains(path(in)) && !path(in).equals("fishing_hook")) {
                result.set(com.li64.tide.data.item.TideDataComponents.BASE_HOOK, path(in));
                return;
            }
    }

    /** 1 for hooks made from raw materials, 2 for hooks made from one of those, 0 for anything else. */
    public static int hookTier(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !isTide(stack)) return 0;
        return BASE_HOOKS.contains(path(stack)) ? 1 : UPGRADED_HOOKS.contains(path(stack)) ? 2 : 0;
    }

    /** The base hook an upgraded hook was made on, or null. */
    public static String baseHook(ItemStack hook) {
        String base = hook == null || hook.isEmpty() ? null : hook.get(com.li64.tide.data.item.TideDataComponents.BASE_HOOK);
        return "bone_hook".equals(base) ? "fish_bone_hook" : base;      // the bone hook was removed in 1.1.1: its upgrades count as made on a fish bone hook
    }

    /** Lines with their own perk, on top of every line's casting range. */
    private static final Map<String, RodEffects> LINES = Map.ofEntries(
            Map.entry("copper_line", fight(-0.05)),
            Map.entry("iron_line", ease(0.14)),
            Map.entry("golden_line", new RodEffects(0, 0, 0, -0.05, 0, 0, 0, 0.10)),
            Map.entry("diamond_line", ease(0.25)),
            Map.entry("honey_line", new RodEffects(0, 0.05, 0, 0, 0, 0, 0.05, 0)),      // the Honey rig works anywhere, so its bite speed does too
            Map.entry("silk_line", bite(0.08)),
            Map.entry("phantom_line", new RodEffects(0.10, 0, 0.05, 0, 0, 0, 0, 0)),
            Map.entry("amethyst_line", NONE),
            Map.entry("netherite_line", new RodEffects(0, 0, 0, -0.08, 0, 0, 0.05, 0)),
            Map.entry("braided_line", new RodEffects(0, 0, 0, -0.05, 0, 0, 0.10, 0)),
            Map.entry("redstone_line", new RodEffects(0, 0, 0.05, 0, 0, 0, 0, 0, 0, 0.20)));      // (took in the old coal line)

    /** Bobbers with their own always-on perk (the plain one has none). */
    private static final Map<String, RodEffects> BOBBERS = Map.ofEntries(
            Map.entry("apple_bobber", bite(0.05)),
            Map.entry("golden_apple_bobber", luck(1)),
            Map.entry("enchanted_golden_apple_bobber", new RodEffects(0, 0, 0, 0, 1, 0.12, 0, 0)),
            Map.entry("iron_bobber", window(0.08)),
            Map.entry("golden_bobber", xp(0.10)),
            Map.entry("diamond_bobber", quality(0.08)),      // diamond is the catch-quality material
            Map.entry("netherite_bobber", new RodEffects(0, 0, 0.05, -0.10, 0, 0, 0, 0)),
                        Map.entry("amethyst_bobber", NONE),
            Map.entry("echo_bobber", xp(0.15)),
            Map.entry("chorus_bobber", cast(0.15)),
            Map.entry("feather_bobber", new RodEffects(0.10, 0.05, 0, 0, 0, 0, 0, 0)),
            Map.entry("lichen_bobber", bite(0.05)),      // glows like the glow hook; strong underground (Cave rig)
            Map.entry("nautilus_bobber", new RodEffects(0, 0, 0.05, -0.05, 0, 0, 0, 0)),
            Map.entry("pearl_bobber", quality(0.10)),      // one ender pearl: its big quality is on a full moon
            Map.entry("heart_bobber", land(0.10)),      // and heals three hearts with each fish (TideFishingHook)
            Map.entry("grassy_bobber", bite(0.08)),
            Map.entry("duck_bobber", xp(0.05)),
            Map.entry("slime_bobber", land(0.10)),
            Map.entry("cherry_bobber", new RodEffects(0, 0.05, 0, 0, 0, 0, 0, 0.05)),
            Map.entry("honey_bobber", xp(0.10)),
            Map.entry("bell_bobber", window(0.05)),
            Map.entry("ender_eye_bobber", NONE));

    /** Lures' always-on perks (what fish they draw in is in {@link Lures}). One lure slot, so each one is a real choice. */
    private static final Map<String, RodEffects> LURES = Map.ofEntries(
            Map.entry("spinner_lure", cast(0.10)),
            Map.entry("golden_spoon_lure", xp(0.10)),
            Map.entry("fly_lure", bite(0.10)),
            Map.entry("jig_lure", new RodEffects(0, 0, 0, -0.05, 0, 0, 0.08, 0)),
            Map.entry("echo_lure", bite(-0.10)),
            Map.entry("magnet_lure", luck(1)));

    /** Bait's extra perks, on top of the speed and luck in its bait data (counted once per kind of bait loaded). */
    private static final Map<String, RodEffects> BAITS = Map.ofEntries(
            Map.entry("golden_bait", quality(0.23)),
            Map.entry("chum", luck(-1)),
            // the session baits: XP for levelling, an easier landing (and so more perfect hooks), a second fish
            Map.entry("glistering_bait", xp(0.25)),
            Map.entry("shiner_bait", new RodEffects(0, 0, 0.10, 0, 0, 0, 0.15, 0)),
            Map.entry("feast_bait", new RodEffects(0, 0, 0, 0, 0, 0, 0, 0, 0.10, 0)));

    // ------------------------------------------------------------------ situational

    /** Where or when a perk applies (judged where the angler stands). */
    public enum When {
        DAY("by day"), NIGHT("at night"), DAWN_DUSK("at dawn and dusk"), RAIN("in the rain"), THUNDER("in a thunderstorm"),
        FULL_MOON("on a full moon"), NEW_MOON("on a new moon"),
        UNDERGROUND("underground"), NETHER("in the Nether"), END("in the End"),
        OCEAN("in oceans"), RIVER("in rivers"), SWAMP("in swamps"), JUNGLE("in jungles"),
        COLD("in cold places"), DRY("in deserts and badlands"), BEACH("on beaches"), MUSHROOM("on mushroom islands"),
        LAVA("when fishing in lava"), FOREST("in forests"),
        ALWAYS("");      // (a rig that works anywhere, any time)

        public final String text;

        When(String text) { this.text = text; }

        public boolean test(net.minecraft.world.entity.player.Player player) {
            net.minecraft.world.level.Level level = player.level();
            net.minecraft.core.BlockPos pos = player.blockPosition();
            var biome = level.getBiome(pos);
            String name = biome.unwrapKey().map(k -> k.identifier().getPath()).orElse("");
            boolean sky = level.dimensionType().hasSkyLight();
            return switch (this) {
                case DAY -> sky && level.isBrightOutside();
                case DAWN_DUSK -> {      // the clock is synced, so the client's stats panel agrees with the server
                    long t = level.getOverworldClockTime() % 24000;
                    yield sky && (t >= 22000 || t < 1500 || (t >= 11000 && t < 14000));      // about 3 minutes each, dawn and dusk
                }
                case THUNDER -> level.isThundering() && level.canSeeSky(pos.above());
                case NEW_MOON -> sky && level.isDarkOutside() && com.li64.tide.util.Mc26.moonPhase(level, pos) == 4;
                case NETHER -> level.dimension() == net.minecraft.world.level.Level.NETHER;
                case END -> level.dimension() == net.minecraft.world.level.Level.END;
                case RIVER -> biome.is(net.minecraft.tags.BiomeTags.IS_RIVER);
                case NIGHT -> sky && level.isDarkOutside();
                case RAIN -> level.isRaining() && level.canSeeSky(pos.above());
                case FULL_MOON -> sky && level.isDarkOutside() && com.li64.tide.util.Mc26.moonPhase(level, pos) == 0;
                case UNDERGROUND -> !level.canSeeSky(pos.above()) && pos.getY() < level.getSeaLevel();
                case JUNGLE -> biome.is(net.minecraft.tags.BiomeTags.IS_JUNGLE);
                case OCEAN -> biome.is(net.minecraft.tags.BiomeTags.IS_OCEAN);
                case SWAMP -> name.contains("swamp");
                case COLD -> biome.value().getBaseTemperature() < 0.15f;
                case DRY -> name.contains("desert") || biome.is(net.minecraft.tags.BiomeTags.IS_BADLANDS);
                case BEACH -> biome.is(net.minecraft.tags.BiomeTags.IS_BEACH);
                case MUSHROOM -> name.contains("mushroom");
                case FOREST -> biome.is(net.minecraft.tags.BiomeTags.IS_FOREST);
                case ALWAYS -> true;
                case LAVA -> {      // judged by where the angler's hook is, not where they stand
                    var hook = com.li64.tide.registries.entities.misc.fishing.HookAccessor.getHook(player);
                    yield hook != null && hook.getCurrentMedium() == com.li64.tide.data.fishing.mediums.FishingMedium.LAVA;
                }
            };
        }
    }

    public record Perk(When when, RodEffects fx) {}

    /** Perks that only count in a place or at a time, by item path (rods, lines, hooks, bobbers, lures, bait). */
    private static final Map<String, List<Perk>> SITUATIONAL = Map.ofEntries(
            // rods (prismarine and sunflower: original Tide's rain and sunlight perks)
            Map.entry("bamboo_fishing_rod", List.of(new Perk(When.JUNGLE, new RodEffects(0, 0.25, 0, 0, 1, 0, 0, 0)))),
            Map.entry("bone_fishing_rod", List.of(new Perk(When.NIGHT, bite(0.25)))),
            Map.entry("copper_fishing_rod", List.of(new Perk(When.RAIN, new RodEffects(0, 0.20, 0, 0, 0, 0.10, 0, 0)),
                    new Perk(When.THUNDER, new RodEffects(0, 0.10, 0, 0, 1, 0, 0, 0)))),
            Map.entry("echo_fishing_rod", List.of(new Perk(When.UNDERGROUND, quality(0.15)))),
            Map.entry("prismarine_fishing_rod", List.of(new Perk(When.RAIN, new RodEffects(0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0)),
                    new Perk(When.OCEAN, bite(0.20)))),
            Map.entry("sunflower_fishing_rod", List.of(new Perk(When.DAY, new RodEffects(0, 0.15, 0, 0, 1, 0, 0, 0)))),
            Map.entry("blazing_fishing_rod", List.of(new Perk(When.NETHER, bite(0.20)), new Perk(When.LAVA, quality(0.10)))),
            Map.entry("netherite_fishing_rod", List.of(new Perk(When.LAVA, new RodEffects(0, 0.10, 0, 0, 1, 0, 0, 0)))),
            Map.entry("mangrove_fishing_rod", List.of(new Perk(When.SWAMP, new RodEffects(0, 0.20, 0, 0, 1, 0, 0, 0)))),
            Map.entry("cherry_fishing_rod", List.of(new Perk(When.DAWN_DUSK, new RodEffects(0, 0.40, 0, 0, 0, 0.10, 0, 0)))),
            Map.entry("frost_fishing_rod", List.of(new Perk(When.COLD, new RodEffects(0, 0.20, 0, 0, 0, 0, 0, 0, 0.15, 0)))),
            Map.entry("cactus_fishing_rod", List.of(new Perk(When.DRY, new RodEffects(0, 0.25, 0, 0, 1, 0, 0, 0)))),
            Map.entry("widow_fishing_rod", List.of(new Perk(When.NIGHT, new RodEffects(0, 0.25, 0, 0, 0, 0, 0, 0.20, 0.15, 0)),
                    new Perk(When.DAY, bite(-0.15)))),
            Map.entry("grove_fishing_rod", List.of(new Perk(When.JUNGLE, luck(1)))),      // a leafy rod: joins the Jungle rig
            Map.entry("drowned_fishing_rod", List.of(new Perk(When.OCEAN, bite(0.10)))),
            Map.entry("conduit_fishing_rod", List.of(new Perk(When.OCEAN, new RodEffects(0, 0.15, 0, 0, 1, 0, 0, 0)))),
            // lines
            Map.entry("slime_line", List.of(new Perk(When.SWAMP, new RodEffects(0, 0.20, 0.05, 0, 1, 0, 0, 0)))),
            Map.entry("kelp_line", List.of(new Perk(When.OCEAN, bite(0.15)))),
            Map.entry("vine_line", List.of(new Perk(When.JUNGLE, new RodEffects(0, 0.10, 0.05, 0, 0, 0, 0, 0)))),
            Map.entry("netherite_line", List.of(new Perk(When.NETHER, bite(0.15)), new Perk(When.LAVA, window(0.10)))),
            Map.entry("copper_line", List.of(new Perk(When.RAIN, bite(0.10)))),
            Map.entry("phantom_line", List.of(new Perk(When.NIGHT, bite(0.15)))),
            Map.entry("prismarine_line", List.of(new Perk(When.OCEAN, window(0.10)))),
            Map.entry("spider_line", List.of(new Perk(When.UNDERGROUND, new RodEffects(0, 0.10, 0.10, 0, 0, 0, 0, 0)))),
            // hooks
                        Map.entry("lavaproof_hook", List.of(new Perk(When.LAVA, new RodEffects(0, 0.20, 0, -0.05, 1, 0, 0, 0.15)))),
            Map.entry("void_hook", List.of(new Perk(When.END, luck(1)))),
            Map.entry("fiery_hook", List.of(new Perk(When.NETHER, bite(0.15)), new Perk(When.LAVA, quality(0.10)))),
            Map.entry("permafrost_hook", List.of(new Perk(When.COLD, bite(0.15)))),
            Map.entry("feathered_hook", List.of(new Perk(When.DAWN_DUSK, quality(0.10)))),
            Map.entry("fish_bone_hook", List.of(new Perk(When.DRY, bite(0.15)))),
            Map.entry("lichen_bobber", List.of(new Perk(When.UNDERGROUND, bite(0.20)))),
            // bobbers
            Map.entry("glow_berry_bobber", List.of(new Perk(When.NIGHT, bite(0.15)))),
            Map.entry("pumpkin_bobber", List.of(new Perk(When.NIGHT, xp(0.15)))),
            Map.entry("lily_pad_bobber", List.of(new Perk(When.SWAMP, bite(0.20)))),
            Map.entry("copper_bobber", List.of(new Perk(When.RAIN, bite(0.15)))),
            Map.entry("feather_bobber", List.of(new Perk(When.RIVER, bite(0.15)))),
            Map.entry("grassy_bobber", List.of(new Perk(When.DAWN_DUSK, bite(0.25)))),
            Map.entry("cherry_bobber", List.of(new Perk(When.DAWN_DUSK, xp(0.15)))),
            Map.entry("pearl_bobber", List.of(new Perk(When.FULL_MOON, new RodEffects(0, 0, 0, 0, 1, 0.15, 0, 0)))),
            Map.entry("slime_bobber", List.of(new Perk(When.SWAMP, bite(0.15)))),
            Map.entry("apple_bobber", List.of(new Perk(When.JUNGLE, bite(0.15)))),
            Map.entry("golden_bobber", List.of(new Perk(When.DAY, xp(0.10)))),
            Map.entry("honey_bobber", List.of(new Perk(When.DAY, bite(0.10)))),
            Map.entry("sculk_bobber", List.of(new Perk(When.UNDERGROUND, quality(0.15)))),
            Map.entry("prismarine_bobber", List.of(new Perk(When.OCEAN, luck(1)))),
            Map.entry("snowball_bobber", List.of(new Perk(When.COLD, bite(0.20)))),
            Map.entry("cactus_bobber", List.of(new Perk(When.DRY, bite(0.20)))),
            Map.entry("turtle_bobber", List.of(new Perk(When.OCEAN, bite(0.15)))),
            Map.entry("mushroom_bobber", List.of(new Perk(When.NIGHT, bite(0.10)))),
            Map.entry("chorus_bobber", List.of(new Perk(When.END, bite(0.20)))),
            Map.entry("nautilus_bobber", List.of(new Perk(When.OCEAN, bite(0.10)))),
            Map.entry("echo_bobber", List.of(new Perk(When.UNDERGROUND, quality(0.10)))),
            Map.entry("netherite_bobber", List.of(new Perk(When.LAVA, bite(0.15)))),
            // lures
            Map.entry("popper_lure", List.of(new Perk(When.DAWN_DUSK, new RodEffects(0, 0.40, 0, 0, 0, 0, 0, 0.10)))),
            Map.entry("fly_lure", List.of(new Perk(When.RIVER, bite(0.25)))),
            Map.entry("prismarine_hook", List.of(new Perk(When.OCEAN, luck(1)), new Perk(When.RAIN, bite(0.10)))),
            Map.entry("duck_bobber", List.of(new Perk(When.RAIN, xp(0.20)))),
            Map.entry("jig_lure", List.of(new Perk(When.UNDERGROUND, bite(0.15)))),
            Map.entry("glow_lure", List.of(new Perk(When.NIGHT, bite(0.15)))),
            Map.entry("magma_lure", List.of(new Perk(When.NETHER, new RodEffects(0, 0.25, 0, 0, 0, 0.10, 0, 0)),
                    new Perk(When.LAVA, new RodEffects(0, 0, 0, 0, 0, 0, 0, 0, 0.10, 0)))),
            Map.entry("spinner_lure", List.of(new Perk(When.DAY, bite(0.10)))),
            Map.entry("chorus_lure", List.of(new Perk(When.END, new RodEffects(0, 0.25, 0, 0, 1, 0, 0, 0)))),
            Map.entry("storm_lure", List.of(new Perk(When.RAIN, bite(0.15)),
                    new Perk(When.THUNDER, new RodEffects(0, 0.10, 0, 0, 1, 0.10, 0, 0)))),
            Map.entry("ink_lure", List.of(new Perk(When.NIGHT, new RodEffects(0, 0.10, 0.10, 0, 0, 0, 0, 0)))),
            Map.entry("frog_lure", List.of(new Perk(When.SWAMP, bite(0.20)))),
            Map.entry("squid_lure", List.of(new Perk(When.OCEAN, new RodEffects(0, 0.15, 0.05, 0, 0, 0, 0, 0)))),
            Map.entry("ice_jig_lure", List.of(new Perk(When.COLD, new RodEffects(0, 0.20, 0, 0, 0, 0.10, 0, 0)))),
            // bait
            Map.entry("glow_worms", List.of(new Perk(When.NIGHT, bite(0.15)))),
            Map.entry("cave_grubs", List.of(new Perk(When.UNDERGROUND, bite(0.15)))),
            Map.entry("dough_ball", List.of(new Perk(When.RIVER, bite(0.10)))),
            Map.entry("moon_bait", List.of(new Perk(When.FULL_MOON, luck(1)))));

    // ------------------------------------------------------------------ bonus drops

    /** Something extra that sometimes comes up with a fish, from a rod. */
    public record Drop(String item, double chance, int minQuality, String text) {}

    private static final Map<String, List<Drop>> DROPS = Map.of(
            "honeycomb_fishing_rod", List.of(new Drop("minecraft:honey_bottle", 0.5, 3, "50% chance of a honey bottle with Great or better fish"),
                    new Drop("minecraft:honeycomb", 0.10, 0, "10% chance of a honeycomb with any fish")),
            "mangrove_fishing_rod", List.of(new Drop("minecraft:slime_ball", 0.20, 0, "20% chance of a slime ball with any fish")),
            "cherry_fishing_rod", List.of(new Drop("minecraft:pink_petals", 0.25, 0, "25% chance of pink petals with any fish")));

    /** One item picked by weight ("#tag" picks any item in the tag). */
    public record Weighted(String item, int weight) {}

    /** A chance, with each fish, of one extra item picked from a list. */
    public record Pick(double chance, String text, List<Weighted> items) {
        public ItemStack roll(net.minecraft.util.RandomSource random) {
            int total = items.stream().mapToInt(Weighted::weight).sum(), r = random.nextInt(total);
            for (Weighted w : items) {
                if ((r -= w.weight()) >= 0) continue;
                if (!w.item().startsWith("#")) return new ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(w.item())));
                var tag = net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.parse(w.item().substring(1)));
                List<net.minecraft.world.item.Item> all = new ArrayList<>();
                net.minecraft.core.registries.BuiltInRegistries.ITEM.getTagOrEmpty(tag).forEach(h -> all.add(h.value()));
                return all.isEmpty() ? ItemStack.EMPTY : new ItemStack(all.get(random.nextInt(all.size())));
            }
            return ItemStack.EMPTY;
        }
    }

    private static List<Weighted> w(Object... pairs) {
        List<Weighted> out = new ArrayList<>();
        for (int i = 0; i < pairs.length; i += 2) out.add(new Weighted((String) pairs[i], (Integer) pairs[i + 1]));
        return out;
    }

    private static final Map<String, Pick> PICKS = Map.of(
            "rotten_fishing_rod", new Pick(0.35, "35% chance of a mob drop with each fish (bones, string, gunpowder...)",
                    w("minecraft:rotten_flesh", 30, "minecraft:bone", 20, "minecraft:string", 18, "minecraft:arrow", 8, "minecraft:spider_eye", 10,
                            "minecraft:gunpowder", 10, "minecraft:carrot", 4, "minecraft:potato", 4, "minecraft:iron_ingot", 3)),
            "grove_fishing_rod", new Pick(0.35, "35% chance of tree drops with each fish (saplings, sticks, apples...)",
                    w("#minecraft:saplings", 35, "minecraft:stick", 35, "minecraft:apple", 25, "minecraft:golden_apple", 1)),
            "drowned_fishing_rod", new Pick(0.35, "35% chance of sunken loot with each fish (kelp, copper, prismarine...)",
                    w("minecraft:kelp", 50, "minecraft:copper_ingot", 40, "minecraft:ink_sac", 30, "minecraft:prismarine_shard", 20, "minecraft:prismarine_crystals", 16,
                            "minecraft:nautilus_shell", 16, "minecraft:sea_pickle", 12, "minecraft:glow_ink_sac", 10, "minecraft:trident", 1)),
            "sickle_fishing_rod", new Pick(0.25, "25% chance of seeds with each fish",
                    w("minecraft:wheat_seeds", 40, "minecraft:beetroot_seeds", 20, "minecraft:pumpkin_seeds", 15, "minecraft:melon_seeds", 15)));

    /** This rod's pick-one bonus, if any. */
    public static Pick pick(ItemStack rod) {
        return rod == null || rod.isEmpty() || !isTide(rod) ? null : PICKS.get(path(rod));
    }

    /** What the sickle and hex rods do besides their numbers (their skills, Green Thumb and Hex). */
    public static final double SICKLE_GROW = 0.35, HEX_EFFECT = 0.20, TWIN_CAP = 0.35;
    /** Things a line, hook or bobber brings up with a fish. */
    private static final Map<String, List<Drop>> PART_DROPS = Map.of(
            "stone_hook", List.of(new Drop("minecraft:kelp", 0.15, 0, "15% chance of kelp with any fish (it sinks to the bottom)"),
                    new Drop("minecraft:seagrass", 0.10, 0, "10% chance of seagrass with any fish")),
            "emerald_hook", List.of(new Drop("minecraft:emerald", 0.03, 0, "3% chance of an emerald with any fish")));

    /** Bonus drops from a rod: its own, what's on it, and its sets (every rig's, wherever you are). */
    public static List<Drop> drops(ItemStack rod) {
        return drops(null, rod);
    }

    /** Bonus drops from a rod where the player is now (a rig only gives its drops where and when it works). */
    public static List<Drop> drops(net.minecraft.world.entity.player.Player player, ItemStack rod) {
        if (rod == null || rod.isEmpty()) return List.of();
        List<Drop> out = new ArrayList<>(DROPS.getOrDefault(path(rod), List.of()));
        for (ItemStack part : parts(rod)) if (isTide(part)) out.addAll(PART_DROPS.getOrDefault(path(part), List.of()));
        out.addAll(RigSets.drops(player, rod));
        return out;
    }

    /** Amethyst's catch quality on a full moon (rod, hook, line, bobber), scaled by {@link #moonlight}. Skills (Moonlit) says these numbers. */
    private static final Map<String, Double> AMETHYST_QUALITY = Map.of(
            "amethyst_fishing_rod", 0.08, "amethyst_hook", 0.06, "amethyst_line", 0.04, "amethyst_bobber", 0.05);

    /** How bright the moon is tonight: 1 on a full moon down to 0 on a new moon; 0 by day (and where there is no sky). */
    public static double moonlight(net.minecraft.world.entity.player.Player player) {
        net.minecraft.world.level.Level level = player.level();
        if (!level.dimensionType().hasSkyLight() || !level.isDarkOutside()) return 0;
        int phase = com.li64.tide.util.Mc26.moonPhase(level, player.blockPosition());
        return 1.0 - Math.min(phase, 8 - phase) / 4.0;      // 0 full, 4 new
    }

    /** How high the sun is: 0 at sunrise, 1 at noon, 0 again at sunset, 0 all night (and where there is no sky). */
    public static double noon(net.minecraft.world.entity.player.Player player) {
        net.minecraft.world.level.Level level = player.level();
        if (!level.dimensionType().hasSkyLight()) return 0;
        long t = level.getOverworldClockTime() % 24000;
        return t < 12000 ? Math.sin(Math.PI * t / 12000.0) : 0;
    }

    /** The amethyst pieces' catch quality where and when the player is (it follows the moon). */
    public static RodEffects amethyst(net.minecraft.world.entity.player.Player player, ItemStack rod) {
        double sum = 0;
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(parts(rod));
        for (ItemStack s : all) if (isTide(s)) sum += AMETHYST_QUALITY.getOrDefault(path(s), 0.0);
        return sum == 0 ? NONE : quality(sum * moonlight(player));
    }

    // ------------------------------------------------------------------ gear

    /** Fishing gear, worn (and the full set on top). */
    private static final Map<String, RodEffects> GEAR = Map.of(
            "angler_hat", bite(0.10),
            "fishing_vest", luck(1),
            "waders", window(0.05),
            "wading_boots", cast(0.10));
    public static final RodEffects FULL_SET = new RodEffects(0, 0, 0, 0, 0, 0.10, 0, 0.10);
    private static final net.minecraft.world.entity.EquipmentSlot[] SLOTS = {
            net.minecraft.world.entity.EquipmentSlot.HEAD, net.minecraft.world.entity.EquipmentSlot.CHEST,
            net.minecraft.world.entity.EquipmentSlot.LEGS, net.minecraft.world.entity.EquipmentSlot.FEET};

    private static RodEffects cast(double v) { return new RodEffects(v, 0, 0, 0, 0, 0, 0, 0); }
    private static RodEffects bite(double v) { return new RodEffects(0, v, 0, 0, 0, 0, 0, 0); }
    private static RodEffects window(double v) { return new RodEffects(0, 0, v, 0, 0, 0, 0, 0); }
    private static RodEffects fight(double v) { return new RodEffects(0, 0, 0, v, 0, 0, 0, 0); }
    private static RodEffects luck(int v) { return new RodEffects(0, 0, 0, 0, v, 0, 0, 0); }
    private static RodEffects quality(double v) { return new RodEffects(0, 0, 0, 0, 0, v, 0, 0); }
    private static RodEffects land(double v) { return new RodEffects(0, 0, 0, 0, 0, 0, v, 0); }
    private static RodEffects xp(double v) { return new RodEffects(0, 0, 0, 0, 0, 0, 0, v); }
    private static RodEffects speed(int v) { return new RodEffects(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, v, 0); }
    private static RodEffects ease(double v) { return new RodEffects(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, v); }

    /** These effects at a share of their strength (luck rounds to whole points). */
    public RodEffects times(double k) {
        if (k >= 1) return this;
        return new RodEffects(cast * k, bite * k, window * k, fight * k, (int) Math.round(luck * k), quality * k, land * k, xp * k,
                twin * k, cook * k, (int) Math.round(speed * k), ease * k);
    }

    public RodEffects plus(RodEffects o) {
        return new RodEffects(cast + o.cast, bite + o.bite, window + o.window, fight + o.fight, luck + o.luck,
                quality + o.quality, land + o.land, xp + o.xp, twin + o.twin, cook + o.cook, speed + o.speed, ease + o.ease);
    }

    public boolean isEmpty() {
        return this.equals(NONE);
    }

    private static String path(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
    }

    private static boolean isTide(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("tide");
    }

    /** One accessory's or gear piece's always-on effects (nothing for anything else). */
    public static RodEffects of(ItemStack accessory) {
        if (accessory == null || accessory.isEmpty()) return NONE;
        String path = path(accessory);
        if (GEAR.containsKey(path) && isTide(accessory)) return GEAR.get(path);
        if (accessory.getItem() instanceof FishingLineItem) return LINES.containsKey(path) ? ANY_LINE.plus(LINES.get(path)) : ANY_LINE;
        if (accessory.getItem() instanceof FishingHookItem) {
            RodEffects e = HOOKS.containsKey(path) ? ANY_HOOK.plus(HOOKS.get(path)) : ANY_HOOK;
            String base = baseHook(accessory);
            return base != null ? e.plus(HOOKS.getOrDefault(base, NONE)) : e;
        }
        if (accessory.getItem() instanceof FishingLureItem) return LURES.getOrDefault(path, NONE);
        if (BAITS.containsKey(path) && isTide(accessory)) return BAITS.get(path);
        if (accessory.getItem() instanceof FishingBobberItem || path.endsWith("_bobber")) {
            return BOBBERS.getOrDefault(path, NONE);      // the plain bobber does nothing extra
        }
        return NONE;
    }

    /** What's attached to a rod: line, hook, bobber and lure. */
    public static List<ItemStack> parts(ItemStack rod) {
        List<ItemStack> parts = new ArrayList<>();
        if (CustomRodManager.hasLine(rod)) parts.add(CustomRodManager.getLine(rod));
        if (CustomRodManager.hasHook(rod)) parts.add(CustomRodManager.getHook(rod));
        if (CustomRodManager.hasBobber(rod)) parts.add(CustomRodManager.getBobber(rod));
        if (CustomRodManager.hasLure(rod)) parts.add(CustomRodManager.getLure(rod));
        java.util.Set<net.minecraft.world.item.Item> baits = new java.util.HashSet<>();
        for (ItemStack bait : com.li64.tide.util.BaitUtils.getBaitItems(rod))
            if (baits.add(bait.getItem())) parts.add(bait);       // each kind of bait once
        return parts;
    }

    /** Everything attached to a rod, added up (always-on perks). */
    public static RodEffects accessories(ItemStack rod) {
        if (rod == null || rod.isEmpty()) return NONE;
        RodEffects e = NONE;
        for (ItemStack part : parts(rod)) e = e.plus(of(part));
        return e;
    }

    /** The rod's own perk plus its accessories (always-on ones). */
    /** A rod item's own perks (not what's attached). */
    public static RodEffects ownRod(ItemStack rod) {
        return rod == null || rod.isEmpty() ? NONE : RODS.getOrDefault(path(rod), NONE);
    }

    public static RodEffects ofRod(ItemStack rod) {
        if (rod == null || rod.isEmpty()) return NONE;
        return RODS.getOrDefault(path(rod), NONE).plus(accessories(rod));
    }

    /** This item's situational perks (empty if none). */
    public static List<Perk> situational(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return List.of();
        List<Perk> own = SITUATIONAL.getOrDefault(path(stack), List.of());
        String base = baseHook(stack);
        if (base == null || !SITUATIONAL.containsKey(base)) return own;
        List<Perk> both = new ArrayList<>(own);
        both.addAll(SITUATIONAL.get(base));
        return both;
    }

    /** Situational perks of the rod and what's on it that apply where the player is now. */
    public static RodEffects situational(net.minecraft.world.entity.player.Player player, ItemStack rod) {
        if (player == null || rod == null || rod.isEmpty()) return NONE;
        RodEffects e = NONE;
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(parts(rod));
        for (ItemStack part : all)
            for (Perk perk : situational(part))
                if (perk.when().test(player)) e = e.plus(perk.fx());
        if (isTide(rod) && path(rod).equals("wayfinder_fishing_rod")) e = e.plus(wayfinder(player));
        e = e.plus(amethyst(player, rod));
        return e.plus(RigSets.active(player, rod));
    }

    /** The wayfinder rod's distance bonus at full strength (5,000 blocks or more from world spawn). */
    public static final RodEffects WAYFINDER_FAR = new RodEffects(0, 0, 0, 0, 2, 0.15, 0, 0.10);
    public static final double WAYFINDER_START = 500, WAYFINDER_FULL = 5000;

    /**
     * How far along the wayfinder rod's bonus is, 0..1: nothing within 500 blocks of world spawn, growing evenly to
     * full at 5,000. A block in the Nether counts as 8. Works on both sides (the client knows the spawn), so the rod
     * panel shows it.
     */
    public static double wayfinderReach(net.minecraft.world.entity.player.Player player) {
        net.minecraft.core.BlockPos spawn = player.level().getRespawnData().globalPos().pos();
        double scale = player.level().dimension() == net.minecraft.world.level.Level.NETHER ? 8 : 1;
        double d = Math.hypot(player.getX() * scale - spawn.getX(), player.getZ() * scale - spawn.getZ());
        return Math.max(0, Math.min(1, (d - WAYFINDER_START) / (WAYFINDER_FULL - WAYFINDER_START)));
    }

    /** The wayfinder rod's bonus where the player stands now. */
    public static RodEffects wayfinder(net.minecraft.world.entity.player.Player player) {
        double k = wayfinderReach(player);
        return new RodEffects(0, 0, 0, 0, (int) Math.round(WAYFINDER_FAR.luck * k), WAYFINDER_FAR.quality * k, 0, WAYFINDER_FAR.xp * k);
    }

    /**
     * Each place, time or weather the rod and what's on it care about, with everything they add there together
     * (a bamboo rod, a vine line and an apple bobber: +50% bite speed in jungles), in the order they first appear.
     */
    public static java.util.LinkedHashMap<When, RodEffects> situationalTotals(ItemStack rod) {
        java.util.LinkedHashMap<When, RodEffects> out = new java.util.LinkedHashMap<>();
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(parts(rod));
        for (ItemStack part : all)
            for (Perk perk : situational(part)) out.merge(perk.when(), perk.fx(), RodEffects::plus);
        return out;
    }

    /**
     * What this rod gives with every condition its pieces and rigs care about met at once: the rod and everything on
     * it, every situational perk, its rigs' bonuses at full strength (noon, a full moon), amethyst on a full moon and the
     * wayfinder far from spawn. Not the angler's level or worn gear. (Day and night perks are both counted, so it is
     * the most each part can add, not one real moment.)
     */
    public static RodEffects everythingActive(ItemStack rod) {
        if (rod == null || rod.isEmpty()) return NONE;
        RodEffects e = ofRod(rod);
        for (RodEffects fx : situationalTotals(rod).values()) e = e.plus(fx);
        for (RigSets.RigSet set : RigSets.carried(rod)) e = e.plus(set.bonus(RigSets.pieces(set, rod)));
        double am = 0;
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(parts(rod));
        for (ItemStack s : all) if (isTide(s)) am += AMETHYST_QUALITY.getOrDefault(path(s), 0.0);
        if (am > 0) e = e.plus(quality(am));
        if (isTide(rod) && path(rod).equals("wayfinder_fishing_rod")) e = e.plus(WAYFINDER_FAR);
        return e;
    }

    /**
     * A rod's perk lines. A bare rod: its own perks. A rod with tackle on it: the totals of the rod and everything on
     * it (always-on, then each place or time with all that the pieces add there), so the numbers are what it fishes with.
     */
    public static List<Component> describeRod(ItemStack rod) {
        List<Component> out = new ArrayList<>(Lures.describe(rod));
        if (parts(rod).isEmpty()) {
            out.addAll(RODS.getOrDefault(path(rod), NONE).describe(ChatFormatting.DARK_AQUA));
            for (Perk perk : situational(rod)) out.addAll(perk.fx().describe(ChatFormatting.LIGHT_PURPLE, " " + perk.when().text));
        } else {
            out.add(line(ChatFormatting.GRAY, "With its tackle, in all:"));
            out.addAll(ofRod(rod).describe(ChatFormatting.DARK_AQUA));
            for (var t : situationalTotals(rod).entrySet()) out.addAll(t.getValue().describe(ChatFormatting.LIGHT_PURPLE, " " + t.getKey().text));
        }
        List<Skills.Skill> skills = parts(rod).isEmpty() ? Skills.of(rod) : Skills.ofRod(rod);
        // which rigs the rod itself belongs to, then any rig it (with what's on it) already counts toward:
        // "Jungle rig Tier 1: +8% Quality in jungles"
        Component member = RigSets.memberLine(rod);
        if (member != null && isTide(rod)) out.add(member);
        List<RigSets.RigSet> carried = RigSets.carried(rod);
        for (RigSets.RigSet set : RigSets.SETS) {
            int n = RigSets.pieces(set, rod);
            if (n < RigSets.HALF) continue;
            int c = RigSets.color(set);
            if (carried.contains(set))
                out.add(Component.literal(set.name() + " " + RigSets.tier(n) + ": " + (n >= RigSets.PIECES ? set.bonusText() : set.twoText()))
                        .withStyle(st -> st.withColor(c)));
            else out.add(line(ChatFormatting.DARK_GRAY, set.name() + ": not counted (a rod carries three rigs)"));
        }
        out.addAll(Unlocks.describe(rod));
        out.addAll(Skills.explanations(skills, rod));
        return out;
    }

    /** Tooltip for a line, hook, bobber, lure or gear piece: always-on perks, then situational ones. */
    public static List<Component> describeItem(ItemStack stack) {
        List<Component> out = new ArrayList<>();
        String base = baseHook(stack);
        int tier = hookTier(stack);
        if (tier > 0) out.add(line(ChatFormatting.GOLD, "Tier " + tier + " hook"));
        if (base != null) out.add(Component.literal("Made on a ").withStyle(ChatFormatting.GRAY)
                .append(Component.translatable("item.tide." + base).withStyle(ChatFormatting.GRAY))
                .append(Component.literal(" (its perks count too)").withStyle(ChatFormatting.GRAY)));
        out.addAll(Lures.describe(stack));
        out.addAll(of(stack).describe(ChatFormatting.DARK_AQUA));
        for (Perk perk : situational(stack)) out.addAll(perk.fx().describe(ChatFormatting.LIGHT_PURPLE, " " + perk.when().text));
        List<Skills.Skill> skills = Skills.of(stack);
        out.addAll(Unlocks.describe(stack));
        Component rigs = RigSets.memberLine(stack);
        if (rigs != null && isTide(stack)) out.add(rigs);
        if (tier == 1)
            out.add(line(ChatFormatting.GRAY, path(stack).equals("fishing_hook") ? "Used to make Tier 2 hooks"
                    : "Used to make Tier 2 hooks, which keep these perks"));
        if (tier == 2 && base == null)
            out.add(line(ChatFormatting.GRAY, path(stack).equals("treble_hook") ? "Made from three plain hooks"
                    : "Made from a Tier 1 hook, and keeps that hook's perks"));
        out.addAll(Skills.explanations(skills, stack));
        return out;
    }

    /** Fishing gear the player is wearing, plus the full-set bonus. */
    public static RodEffects worn(net.minecraft.world.entity.player.Player player) {
        if (player == null) return NONE;
        RodEffects e = NONE;
        int pieces = 0;
        for (net.minecraft.world.entity.EquipmentSlot slot : SLOTS) {
            ItemStack worn = player.getItemBySlot(slot);
            if (!worn.isEmpty() && GEAR.containsKey(path(worn)) && isTide(worn)) {
                e = e.plus(GEAR.get(path(worn)));
                pieces++;
            }
        }
        return pieces == SLOTS.length ? e.plus(FULL_SET) : e;
    }

    /** Everything that helps this cast: the rod and what's on it, what the angler's wearing, and where they are. */
    public static RodEffects of(net.minecraft.world.entity.player.Player player, ItemStack rod) {
        return ofRod(rod).plus(worn(player)).plus(situational(player, rod));
    }

    public static boolean isGear(ItemStack stack) {
        return !stack.isEmpty() && GEAR.containsKey(path(stack)) && isTide(stack);
    }

    /** Bite speed after stacking (see {@link #softenBite}). */
    public double effectiveBite() {
        return softenBite(bite);
    }

    /**
     * The one bite-speed number: the gear's bite speed plus {@code speedPoints} of lure/bait/rod speed, all added
     * together, then softened. This is what the rod panel shows (as +%) and what the wait is divided by.
     */
    public double biteSpeed(int speedPoints) {
        return softenBite(bite + speedPoints * SPEED_POINT);
    }

    /** Multiply the bite wait by this (bite speed +10% -> waits 1/1.1 as long). */
    public double biteWaitFactor(int speedPoints) {
        return 1.0 / Math.max(0.5, 1.0 + biteSpeed(speedPoints));
    }

    public double biteWaitFactor() {
        return biteWaitFactor(0);
    }

    /** Tooltip lines: "+10% Casting", "+8% Stability"... (each stat in its own colour) */
    public List<Component> describe(ChatFormatting color) {
        return describe(color, "");
    }

    public List<Component> describe(ChatFormatting color, String when) {
        List<Component> out = new ArrayList<>();
        if (cast != 0) out.add(stat(color, pct(cast), Stat.CASTING, when));
        if (bite != 0) out.add(stat(color, pct(bite), Stat.ATTRACTION, when));
        if (window != 0) out.add(stat(color, pct(window), Stat.PRECISION, when));
        if (fight != 0) out.add(stat(color, pct(-fight), Stat.STABILITY, when));      // an easier fight is more Stability
        if (ease != 0) out.add(stat(color, pct(ease), Stat.CONTROL, when));
        if (luck != 0) out.add(stat(color, pct(luck * RARE_PER_LUCK), Stat.FORTUNE, when));
        if (quality != 0) out.add(stat(color, pct(quality), Stat.QUALITY, when + (quality < 0 ? " (smaller, plainer fish)" : "")));
        if (land != 0) out.add(stat(color, pct(land), Stat.FINESSE, when));
        if (xp != 0) out.add(stat(color, pct(xp), Stat.EXPERIENCE, when));
        if (twin != 0) out.add(stat(color, pct(twin), Stat.DOUBLE_CATCH, when));
        if (speed != 0) out.add(stat(color, pct(speed * SPEED_POINT), Stat.ATTRACTION, when));
        // a chance to come up cooked is a skill (Hot Line): see Skills
        return out;
    }

    /** "+25% Attraction in jungles": the value in the line's colour, the stat in its own, where it works in purple. */
    private static Component stat(ChatFormatting color, String value, Stat stat, String when) {
        var line = Component.literal(value + " ").withStyle(color)
                .append(Component.literal(stat.label).withStyle(st -> st.withColor(stat.color)));
        if (!when.isEmpty()) line.append(Component.literal(when).withStyle(ChatFormatting.LIGHT_PURPLE));
        return line;
    }

    private static Component line(ChatFormatting color, String text) {
        return Component.literal(text).withStyle(color);
    }

    private static String pct(double v) {
        return String.format("%+d%%", Math.round(v * 100));
    }
}
