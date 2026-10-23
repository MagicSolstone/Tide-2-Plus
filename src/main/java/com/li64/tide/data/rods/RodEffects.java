package com.li64.tide.data.rods;

import com.li64.tide.registries.items.FishingBobberItem;
import com.li64.tide.registries.items.FishingHookItem;
import com.li64.tide.registries.items.FishingLineItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * What a rod's line, hook and bobber add to fishing. Every line gives casting range and every hook
 * a wider hook window (so even the plain ones are worth attaching: an empty slot gives nothing);
 * each bobber has its own perk. Special lines and hooks keep their own effects on top (copper line's
 * slower fish, the lavaproof hook's lava fishing...).
 *
 * All fractions: 0.10 = +10%. {@code fight} below zero makes fish easier; {@code land} adds to the
 * chance that a Good or Bad hook still lands the fish.
 */
public record RodEffects(double cast, double bite, double window, double fight, int luck, double quality, double land, double xp) {
    public static final RodEffects NONE = new RodEffects(0, 0, 0, 0, 0, 0, 0, 0);

    private static final RodEffects ANY_LINE = cast(0.10);
    private static final RodEffects ANY_HOOK = window(0.05);
    private static final RodEffects DYED_BOBBER = bite(0.05);

    /** Bobbers with their own perk, by item path. */
    private static final Map<String, RodEffects> BOBBERS = Map.ofEntries(
            Map.entry("apple_bobber", bite(0.05)),
            Map.entry("golden_apple_bobber", new RodEffects(0, 0, 0, 0, 1, 0, 0, 0)),
            Map.entry("enchanted_golden_apple_bobber", new RodEffects(0, 0, 0, 0, 1, 0.10, 0, 0)),
            Map.entry("iron_bobber", window(0.08)),
            Map.entry("golden_bobber", xp(0.10)),
            Map.entry("diamond_bobber", bite(0.12)),
            Map.entry("netherite_bobber", new RodEffects(0, 0, 0, -0.08, 0, 0, 0, 0)),
            Map.entry("amethyst_bobber", quality(0.15)),
            Map.entry("echo_bobber", xp(0.15)),
            Map.entry("chorus_bobber", cast(0.15)),
            Map.entry("feather_bobber", new RodEffects(0.10, 0.05, 0, 0, 0, 0, 0, 0)),
            Map.entry("lichen_bobber", bite(0.10)),
            Map.entry("nautilus_bobber", new RodEffects(0, 0, 0.05, -0.05, 0, 0, 0, 0)),
            Map.entry("pearl_bobber", quality(0.20)),
            Map.entry("heart_bobber", new RodEffects(0, 0, 0, 0, 0, 0, 0.15, 0)),
            Map.entry("grassy_bobber", bite(0.08)),
            Map.entry("duck_bobber", xp(0.10)));

    /** Hooks with their own perk on top of every hook's wider window, by item path. */
    private static final Map<String, RodEffects> HOOKS = Map.ofEntries(
            Map.entry("stone_hook", new RodEffects(0, 0, 0, -0.03, 0, 0, 0, 0)),
            Map.entry("copper_hook", bite(0.05)),
            Map.entry("golden_hook", new RodEffects(0, 0, 0, 0, 1, 0, 0, 0.05)),
            Map.entry("diamond_hook", window(0.10)),
            Map.entry("emerald_hook", xp(0.15)),
            Map.entry("amethyst_hook", quality(0.10)),
            Map.entry("netherite_hook", new RodEffects(0, 0, 0.08, -0.08, 0, 0, 0, 0)),
            Map.entry("bone_hook", new RodEffects(0, 0, 0, -0.05, 0, 0, 0.05, 0)),
            Map.entry("prismarine_hook", new RodEffects(0, 0.05, 0, 0, 1, 0, 0, 0)),
            Map.entry("glow_hook", bite(0.15)),
            Map.entry("feathered_hook", new RodEffects(0.05, 0.10, 0, 0, 0, 0, 0, 0)),
            Map.entry("barbed_hook", new RodEffects(0, 0, 0, 0, 0, 0, 0.10, 0)));

    /** Lines with their own perk on top of every line's casting range. */
    private static final Map<String, RodEffects> LINES = Map.of(
            "coal_line", window(0.10),
            "honey_line", new RodEffects(0, 0, 0, 0, 0, 0, 0.10, 0),
            "silk_line", bite(0.08),
            "phantom_line", cast(0.15));

    /** New bobbers' always-on perks (situational ones are in SITUATIONAL). */
    private static final Map<String, RodEffects> MORE_BOBBERS = Map.of(
            "slime_bobber", new RodEffects(0, 0, 0, 0, 0, 0, 0.10, 0),
            "cherry_bobber", new RodEffects(0, 0.05, 0, 0, 0, 0, 0, 0.05));

    /** Rods' own perks. The kid's pole: easy, quick fights, but mostly common, smaller fish. */
    private static final Map<String, RodEffects> RODS = Map.of(
            "kids_fishing_pole", new RodEffects(0, 0.10, 0.15, -0.20, -1, -0.30, 0.10, 0),
            "bamboo_fishing_rod", cast(0.05),
            "bone_fishing_rod", NONE,
            "copper_fishing_rod", NONE);

    /** Where or when a perk applies. */
    public enum When {
        NIGHT("at night"), JUNGLE("in jungles"), UNDERGROUND("underground"), RAIN("in the rain"),
        OCEAN("in oceans"), SWAMP("in swamps");

        public final String text;

        When(String text) { this.text = text; }

        public boolean test(net.minecraft.world.entity.player.Player player) {
            net.minecraft.world.level.Level level = player.level();
            net.minecraft.core.BlockPos pos = player.blockPosition();
            return switch (this) {
                case NIGHT -> level.dimensionType().hasSkyLight() && level.isDarkOutside();
                case JUNGLE -> level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_JUNGLE);
                case UNDERGROUND -> !level.canSeeSky(pos.above()) && pos.getY() < level.getSeaLevel();
                case RAIN -> level.isRaining() && level.canSeeSky(pos.above());
                case OCEAN -> level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_OCEAN);
                case SWAMP -> level.getBiome(pos).unwrapKey().map(k -> k.identifier().getPath().contains("swamp")).orElse(false);
            };
        }
    }

    public record Perk(When when, RodEffects fx) {}

    /** Perks that only count in a place or at a time, by item path (rods, lines, hooks, bobbers). */
    private static final Map<String, Perk> SITUATIONAL = Map.ofEntries(
            Map.entry("bamboo_fishing_rod", new Perk(When.JUNGLE, new RodEffects(0, 0.25, 0, 0, 1, 0, 0, 0))),
            Map.entry("bone_fishing_rod", new Perk(When.NIGHT, bite(0.25))),
            Map.entry("copper_fishing_rod", new Perk(When.RAIN, new RodEffects(0, 0.20, 0, 0, 0, 0.10, 0, 0))),
            Map.entry("slime_line", new Perk(When.UNDERGROUND, new RodEffects(0, 0.20, 0.05, 0, 1, 0, 0, 0))),
            Map.entry("kelp_line", new Perk(When.OCEAN, bite(0.15))),
            Map.entry("glow_berry_bobber", new Perk(When.NIGHT, bite(0.15))),
            Map.entry("pumpkin_bobber", new Perk(When.NIGHT, xp(0.15))),
            Map.entry("lily_pad_bobber", new Perk(When.SWAMP, bite(0.20))),
            Map.entry("copper_bobber", new Perk(When.RAIN, bite(0.15))),
            Map.entry("sculk_bobber", new Perk(When.UNDERGROUND, quality(0.15))),
            Map.entry("prismarine_bobber", new Perk(When.OCEAN, new RodEffects(0, 0, 0, 0, 1, 0, 0, 0))));

    /** Fishing gear, worn (and the full set on top). */
    private static final Map<String, RodEffects> GEAR = Map.of(
            "angler_hat", bite(0.10),
            "fishing_vest", new RodEffects(0, 0, 0, 0, 1, 0, 0, 0),
            "waders", window(0.05),
            "wading_boots", cast(0.10));
    public static final RodEffects FULL_SET = new RodEffects(0, 0, 0, 0, 0, 0.10, 0, 0.10);
    private static final net.minecraft.world.entity.EquipmentSlot[] SLOTS = {
            net.minecraft.world.entity.EquipmentSlot.HEAD, net.minecraft.world.entity.EquipmentSlot.CHEST,
            net.minecraft.world.entity.EquipmentSlot.LEGS, net.minecraft.world.entity.EquipmentSlot.FEET};

    private static RodEffects cast(double v) { return new RodEffects(v, 0, 0, 0, 0, 0, 0, 0); }
    private static RodEffects bite(double v) { return new RodEffects(0, v, 0, 0, 0, 0, 0, 0); }
    private static RodEffects window(double v) { return new RodEffects(0, 0, v, 0, 0, 0, 0, 0); }
    private static RodEffects quality(double v) { return new RodEffects(0, 0, 0, 0, 0, v, 0, 0); }
    private static RodEffects xp(double v) { return new RodEffects(0, 0, 0, 0, 0, 0, 0, v); }

    public RodEffects plus(RodEffects o) {
        return new RodEffects(cast + o.cast, bite + o.bite, window + o.window, fight + o.fight, luck + o.luck,
                quality + o.quality, land + o.land, xp + o.xp);
    }

    public boolean isEmpty() {
        return this.equals(NONE);
    }

    /** One accessory's effects (nothing for something that isn't a line, hook or bobber). */
    public static RodEffects of(ItemStack accessory) {
        if (accessory == null || accessory.isEmpty()) return NONE;
        String path = BuiltInRegistries.ITEM.getKey(accessory.getItem()).getPath();
        if (GEAR.containsKey(path) && BuiltInRegistries.ITEM.getKey(accessory.getItem()).getNamespace().equals("tide")) return GEAR.get(path);
        if (accessory.getItem() instanceof FishingLineItem) return LINES.containsKey(path) ? ANY_LINE.plus(LINES.get(path)) : ANY_LINE;
        if (MORE_BOBBERS.containsKey(path)) return MORE_BOBBERS.get(path);
        if (SITUATIONAL.containsKey(path) && path.endsWith("_bobber")) return NONE;
        if (accessory.getItem() instanceof FishingHookItem) return HOOKS.containsKey(path) ? ANY_HOOK.plus(HOOKS.get(path)) : ANY_HOOK;
        if (accessory.getItem() instanceof FishingBobberItem || path.endsWith("_bobber"))
            return BOBBERS.getOrDefault(path, path.endsWith("_bobber") ? DYED_BOBBER : NONE);
        return NONE;
    }

    /** Everything attached to a rod, added up. */
    public static RodEffects accessories(ItemStack rod) {
        if (rod == null || rod.isEmpty()) return NONE;
        RodEffects e = NONE;
        if (CustomRodManager.hasLine(rod)) e = e.plus(of(CustomRodManager.getLine(rod)));
        if (CustomRodManager.hasHook(rod)) e = e.plus(of(CustomRodManager.getHook(rod)));
        if (CustomRodManager.hasBobber(rod)) e = e.plus(of(CustomRodManager.getBobber(rod)));
        return e;
    }

    /** The rod's own perk plus its accessories (always-on ones). */
    public static RodEffects ofRod(ItemStack rod) {
        if (rod == null || rod.isEmpty()) return NONE;
        return RODS.getOrDefault(path(rod), NONE).plus(accessories(rod));
    }

    private static String path(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
    }

    /** This item's situational perk, if it has one. */
    public static Perk situational(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        return SITUATIONAL.get(path(stack));
    }

    /** Situational perks of the rod and its accessories that apply where the player is now. */
    public static RodEffects situational(net.minecraft.world.entity.player.Player player, ItemStack rod) {
        if (player == null || rod == null || rod.isEmpty()) return NONE;
        RodEffects e = NONE;
        List<ItemStack> parts = new ArrayList<>(List.of(rod));
        if (CustomRodManager.hasLine(rod)) parts.add(CustomRodManager.getLine(rod));
        if (CustomRodManager.hasHook(rod)) parts.add(CustomRodManager.getHook(rod));
        if (CustomRodManager.hasBobber(rod)) parts.add(CustomRodManager.getBobber(rod));
        for (ItemStack part : parts) {
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

    /** Tooltip for a line, hook, bobber or gear piece: always-on perks, then situational ones. */
    public static List<Component> describeItem(ItemStack stack) {
        List<Component> out = new ArrayList<>(of(stack).describe(ChatFormatting.DARK_AQUA));
        Perk perk = situational(stack);
        if (perk != null) out.addAll(perk.fx().describe(ChatFormatting.DARK_AQUA, " " + perk.when().text));
        return out;
    }

    /** Fishing gear the player is wearing, plus the full-set bonus. */
    public static RodEffects worn(net.minecraft.world.entity.player.Player player) {
        if (player == null) return NONE;
        RodEffects e = NONE;
        int pieces = 0;
        for (net.minecraft.world.entity.EquipmentSlot slot : SLOTS) {
            ItemStack worn = player.getItemBySlot(slot);
            String path = BuiltInRegistries.ITEM.getKey(worn.getItem()).getPath();
            if (!worn.isEmpty() && GEAR.containsKey(path) && BuiltInRegistries.ITEM.getKey(worn.getItem()).getNamespace().equals("tide")) {
                e = e.plus(GEAR.get(path));
                pieces++;
            }
        }
        return pieces == SLOTS.length ? e.plus(FULL_SET) : e;
    }

    /** Everything that helps this cast: the rod's line, hook and bobber, and what the angler's wearing. */
    public static RodEffects of(net.minecraft.world.entity.player.Player player, ItemStack rod) {
        return ofRod(rod).plus(worn(player)).plus(situational(player, rod));
    }

    public static boolean isGear(ItemStack stack) {
        return !stack.isEmpty() && GEAR.containsKey(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath())
                && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("tide");
    }

    /** Multiply the bite wait by this (bite speed +10% -> waits 1/1.1 as long). */
    public double biteWaitFactor() {
        return 1.0 / (1.0 + bite);
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
