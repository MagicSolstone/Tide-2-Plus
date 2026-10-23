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
 * the same kind; {@code cook} the chance the fish comes up already cooked.
 */
public record RodEffects(double cast, double bite, double window, double fight, int luck, double quality,
                         double land, double xp, double twin, double cook) {
    public RodEffects(double cast, double bite, double window, double fight, int luck, double quality, double land, double xp) {
        this(cast, bite, window, fight, luck, quality, land, xp, 0, 0);
    }

    public static final RodEffects NONE = new RodEffects(0, 0, 0, 0, 0, 0, 0, 0);

    private static final RodEffects ANY_LINE = cast(0.10);
    private static final RodEffects ANY_HOOK = window(0.05);
    private static final RodEffects DYED_BOBBER = bite(0.05);

    // ------------------------------------------------------------------ rods

    /** Rods' own perks. Tiers climb from nothing (wood, stone) to netherite; the special rods each lean one way. */
    private static final Map<String, RodEffects> RODS = Map.ofEntries(
            Map.entry("iron_fishing_rod", window(0.05)),
            Map.entry("golden_fishing_rod", xp(0.10)),
            Map.entry("crystal_fishing_rod", new RodEffects(0, 0, 0.05, 0, 0, 0.05, 0, 0)),
            Map.entry("diamond_fishing_rod", new RodEffects(0.10, 0, 0.10, 0, 0, 0.10, 0, 0)),
            Map.entry("netherite_fishing_rod", new RodEffects(0.10, 0.05, 0.12, -0.12, 0, 0.15, 0.10, 0)),
            Map.entry("echo_fishing_rod", new RodEffects(0, 0, 0.05, 0, 0, 0, 0, 0.10)),
            Map.entry("prismarine_fishing_rod", window(0.05)),
            Map.entry("village_fishing_rod", xp(0.15)),
            Map.entry("blazing_fishing_rod", new RodEffects(0, 0, 0, -0.08, 0, 0, 0, 0, 0, 0.15)),
            Map.entry("honeycomb_fishing_rod", land(0.15)),
            Map.entry("midas_fishing_rod", new RodEffects(0, 0, 0, 0, 0, 0.10, 0, 0.10)),
            Map.entry("kids_fishing_pole", new RodEffects(0, 0.10, 0.15, -0.20, -1, -0.30, 0.10, 0)),
            Map.entry("bamboo_fishing_rod", cast(0.05)));

    // ------------------------------------------------------------------ hooks, lines, bobbers, lures

    /** Hooks with their own perk, on top of every hook's wider window. */
    private static final Map<String, RodEffects> HOOKS = Map.ofEntries(
            Map.entry("stone_hook", fight(-0.03)),
            Map.entry("copper_hook", bite(0.05)),
            Map.entry("golden_hook", new RodEffects(0, 0, 0, 0, 1, 0, 0, 0.05)),
            Map.entry("diamond_hook", new RodEffects(0, 0, 0.12, 0, 0, 0, 0.05, 0)),
            Map.entry("emerald_hook", xp(0.15)),
            Map.entry("amethyst_hook", quality(0.12)),
            Map.entry("netherite_hook", new RodEffects(0, 0, 0.10, -0.10, 0, 0.05, 0, 0)),
            Map.entry("bone_hook", new RodEffects(0, 0, 0, -0.05, 0, 0, 0.05, 0)),
            Map.entry("prismarine_hook", new RodEffects(0, 0.05, 0, 0, 1, 0, 0, 0)),
            Map.entry("glow_hook", bite(0.12)),
            Map.entry("feathered_hook", new RodEffects(0.05, 0.08, 0, 0, 0, 0, 0, 0)),
            Map.entry("barbed_hook", land(0.10)),
            Map.entry("treble_hook", new RodEffects(0, 0, 0, 0.05, 0, 0, 0, 0, 0.12, 0)),
            Map.entry("circle_hook", land(0.25)),
            Map.entry("fish_bone_hook", new RodEffects(0, 0, 0, -0.05, 0, 0, 0, 0.08)),
            Map.entry("lucky_hook", new RodEffects(0, 0, 0, 0, 1, 0, 0, 0)));

    /** Lines with their own perk, on top of every line's casting range. */
    private static final Map<String, RodEffects> LINES = Map.ofEntries(
            Map.entry("coal_line", window(0.10)),
            Map.entry("honey_line", land(0.10)),
            Map.entry("silk_line", bite(0.08)),
            Map.entry("phantom_line", cast(0.15)),
            Map.entry("braided_line", new RodEffects(0, 0, 0, -0.05, 0, 0, 0.10, 0)),
            Map.entry("redstone_line", new RodEffects(0, 0, 0, 0, 0, 0, 0, 0, 0, 0.20)));

    /** Bobbers with their own always-on perk (dyed ones give a little bite speed). */
    private static final Map<String, RodEffects> BOBBERS = Map.ofEntries(
            Map.entry("apple_bobber", bite(0.05)),
            Map.entry("golden_apple_bobber", luck(1)),
            Map.entry("enchanted_golden_apple_bobber", new RodEffects(0, 0, 0, 0, 1, 0.12, 0, 0)),
            Map.entry("iron_bobber", window(0.08)),
            Map.entry("golden_bobber", xp(0.10)),
            Map.entry("diamond_bobber", bite(0.12)),
            Map.entry("netherite_bobber", new RodEffects(0, 0, 0.05, -0.10, 0, 0, 0, 0)),
            Map.entry("amethyst_bobber", quality(0.15)),
            Map.entry("echo_bobber", xp(0.15)),
            Map.entry("chorus_bobber", cast(0.15)),
            Map.entry("feather_bobber", new RodEffects(0.10, 0.05, 0, 0, 0, 0, 0, 0)),
            Map.entry("lichen_bobber", bite(0.10)),
            Map.entry("nautilus_bobber", new RodEffects(0, 0, 0.05, -0.05, 0, 0, 0, 0)),
            Map.entry("pearl_bobber", quality(0.20)),
            Map.entry("heart_bobber", land(0.15)),
            Map.entry("grassy_bobber", bite(0.08)),
            Map.entry("duck_bobber", xp(0.10)),
            Map.entry("slime_bobber", land(0.10)),
            Map.entry("cherry_bobber", new RodEffects(0, 0.05, 0, 0, 0, 0, 0, 0.05)),
            Map.entry("honey_bobber", xp(0.10)),
            Map.entry("bell_bobber", window(0.05)),
            Map.entry("ender_pearl_bobber", NONE));

    /** Lures' perks (what fish they draw in is in {@link Lures}). */
    private static final Map<String, RodEffects> LURES = Map.ofEntries(
            Map.entry("fly_lure", bite(0.05)),
            Map.entry("jig_lure", fight(-0.05)),
            Map.entry("echo_lure", bite(-0.10)));

    // ------------------------------------------------------------------ situational

    /** Where or when a perk applies (judged where the angler stands). */
    public enum When {
        DAY("by day"), NIGHT("at night"), RAIN("in the rain"), FULL_MOON("on a full moon"),
        UNDERGROUND("underground"), JUNGLE("in jungles"), OCEAN("in oceans"), SWAMP("in swamps"),
        COLD("in cold places"), DRY("in deserts and badlands"), BEACH("on beaches"), MUSHROOM("on mushroom islands");

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
            };
        }
    }

    public record Perk(When when, RodEffects fx) {}

    /** Perks that only count in a place or at a time, by item path (rods, lines, hooks, bobbers, lures). */
    private static final Map<String, Perk> SITUATIONAL = Map.ofEntries(
            Map.entry("bamboo_fishing_rod", new Perk(When.JUNGLE, new RodEffects(0, 0.25, 0, 0, 1, 0, 0, 0))),
            Map.entry("bone_fishing_rod", new Perk(When.NIGHT, bite(0.25))),
            Map.entry("copper_fishing_rod", new Perk(When.RAIN, new RodEffects(0, 0.20, 0, 0, 0, 0.10, 0, 0))),
            Map.entry("echo_fishing_rod", new Perk(When.UNDERGROUND, quality(0.15))),
            Map.entry("prismarine_fishing_rod", new Perk(When.OCEAN, bite(0.20))),
            Map.entry("sunflower_fishing_rod", new Perk(When.DAY, bite(0.15))),
            Map.entry("slime_line", new Perk(When.UNDERGROUND, new RodEffects(0, 0.20, 0.05, 0, 1, 0, 0, 0))),
            Map.entry("kelp_line", new Perk(When.OCEAN, bite(0.15))),
            Map.entry("vine_line", new Perk(When.JUNGLE, new RodEffects(0, 0.10, 0.05, 0, 0, 0, 0, 0))),
            Map.entry("spider_line", new Perk(When.NIGHT, window(0.10))),
            Map.entry("lucky_hook", new Perk(When.FULL_MOON, luck(1))),
            Map.entry("glow_berry_bobber", new Perk(When.NIGHT, bite(0.15))),
            Map.entry("pumpkin_bobber", new Perk(When.NIGHT, xp(0.15))),
            Map.entry("lily_pad_bobber", new Perk(When.SWAMP, bite(0.20))),
            Map.entry("copper_bobber", new Perk(When.RAIN, bite(0.15))),
            Map.entry("sculk_bobber", new Perk(When.UNDERGROUND, quality(0.15))),
            Map.entry("prismarine_bobber", new Perk(When.OCEAN, luck(1))),
            Map.entry("snowball_bobber", new Perk(When.COLD, bite(0.20))),
            Map.entry("cactus_bobber", new Perk(When.DRY, bite(0.20))),
            Map.entry("turtle_bobber", new Perk(When.BEACH, new RodEffects(0, 0.15, 0, 0, 1, 0, 0, 0))),
            Map.entry("mushroom_bobber", new Perk(When.MUSHROOM, new RodEffects(0, 0.10, 0, 0, 1, 0, 0, 0))),
            Map.entry("popper_lure", new Perk(When.DAY, new RodEffects(0, 0.15, 0, 0, 0, 0, 0, 0.05))));

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

    public RodEffects plus(RodEffects o) {
        return new RodEffects(cast + o.cast, bite + o.bite, window + o.window, fight + o.fight, luck + o.luck,
                quality + o.quality, land + o.land, xp + o.xp, twin + o.twin, cook + o.cook);
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
        if (accessory.getItem() instanceof FishingHookItem) return HOOKS.containsKey(path) ? ANY_HOOK.plus(HOOKS.get(path)) : ANY_HOOK;
        if (accessory.getItem() instanceof FishingLureItem) return LURES.getOrDefault(path, NONE);
        if (accessory.getItem() instanceof FishingBobberItem || path.endsWith("_bobber")) {
            if (BOBBERS.containsKey(path)) return BOBBERS.get(path);
            return SITUATIONAL.containsKey(path) ? NONE : DYED_BOBBER;
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
    public static RodEffects ofRod(ItemStack rod) {
        if (rod == null || rod.isEmpty()) return NONE;
        return RODS.getOrDefault(path(rod), NONE).plus(accessories(rod));
    }

    /** This item's situational perk, if it has one. */
    public static Perk situational(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        return SITUATIONAL.get(path(stack));
    }

    /** Situational perks of the rod and what's on it that apply where the player is now. */
    public static RodEffects situational(net.minecraft.world.entity.player.Player player, ItemStack rod) {
        if (player == null || rod == null || rod.isEmpty()) return NONE;
        RodEffects e = NONE;
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(parts(rod));
        for (ItemStack part : all) {
            Perk perk = situational(part);
            if (perk != null && perk.when().test(player)) e = e.plus(perk.fx());
        }
        return e;
    }

    /** A rod's own perk lines (the rod item, not what's attached). */
    public static List<Component> describeRod(ItemStack rod) {
        List<Component> out = new ArrayList<>(RODS.getOrDefault(path(rod), NONE).describe(ChatFormatting.DARK_AQUA));
        Perk perk = situational(rod);
        if (perk != null) out.addAll(perk.fx().describe(ChatFormatting.DARK_AQUA, " " + perk.when().text));
        return out;
    }

    /** Tooltip for a line, hook, bobber, lure or gear piece: always-on perks, then situational ones. */
    public static List<Component> describeItem(ItemStack stack) {
        List<Component> out = new ArrayList<>(Lures.describe(stack));
        out.addAll(of(stack).describe(ChatFormatting.DARK_AQUA));
        Perk perk = situational(stack);
        if (perk != null) out.addAll(perk.fx().describe(ChatFormatting.DARK_AQUA, " " + perk.when().text));
        if (path(stack).equals("bell_bobber") && isTide(stack)) out.add(line(ChatFormatting.DARK_AQUA, "Rings when a fish bites"));
        if (path(stack).equals("ender_pearl_bobber") && isTide(stack)) out.add(line(ChatFormatting.DARK_AQUA, "Shows what's on the hook when it bites"));
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

    /** Bite speed after stacking: full value up to +50%, half value past that. */
    public double effectiveBite() {
        return bite <= 0.5 ? bite : 0.5 + (bite - 0.5) * 0.5;
    }

    /** Multiply the bite wait by this (bite speed +10% -> waits 1/1.1 as long). */
    public double biteWaitFactor() {
        return 1.0 / Math.max(0.5, 1.0 + effectiveBite());
    }

    /** Tooltip lines: "+10% casting range", "Fish fight 8% easier"... */
    public List<Component> describe(ChatFormatting color) {
        return describe(color, "");
    }

    public List<Component> describe(ChatFormatting color, String when) {
        List<Component> out = new ArrayList<>();
        if (cast != 0) out.add(line(color, pct(cast) + " casting range"));
        if (bite != 0) out.add(line(color, pct(bite) + " bite speed"));
        if (window != 0) out.add(line(color, pct(window) + " hook window"));
        if (fight != 0) out.add(line(color, "Fish fight " + Math.round(Math.abs(fight) * 100) + "% " + (fight < 0 ? "easier" : "harder")));
        if (luck != 0) out.add(line(color, luck > 0 ? String.format("%+d fishing luck (rarer fish)", luck) : String.format("%d fishing luck (more common fish)", luck)));
        if (quality != 0) out.add(line(color, quality > 0 ? pct(quality) + " better catch quality" : "Mostly smaller, plainer fish"));
        if (land != 0) out.add(line(color, pct(land) + " to land Good and Bad hooks"));
        if (xp != 0) out.add(line(color, pct(xp) + " fishing XP"));
        if (twin != 0) out.add(line(color, Math.round(twin * 100) + "% chance of a second fish"));
        if (cook != 0) out.add(line(color, Math.round(cook * 100) + "% chance the fish comes up cooked"));
        if (!when.isEmpty()) out.replaceAll(c -> line(color, c.getString() + when));
        return out;
    }

    private static Component line(ChatFormatting color, String text) {
        return Component.literal(text).withStyle(color);
    }

    private static String pct(double v) {
        return String.format("%+d%%", Math.round(v * 100));
    }
}
