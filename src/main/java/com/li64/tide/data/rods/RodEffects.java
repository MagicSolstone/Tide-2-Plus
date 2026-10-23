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
        if (accessory.getItem() instanceof FishingLineItem) return ANY_LINE;
        if (accessory.getItem() instanceof FishingHookItem) return ANY_HOOK;
        if (accessory.getItem() instanceof FishingBobberItem || path.endsWith("_bobber"))
            return BOBBERS.getOrDefault(path, path.endsWith("_bobber") ? DYED_BOBBER : NONE);
        return NONE;
    }

    /** Everything attached to a rod, added up. */
    public static RodEffects ofRod(ItemStack rod) {
        if (rod == null || rod.isEmpty()) return NONE;
        RodEffects e = NONE;
        if (CustomRodManager.hasLine(rod)) e = e.plus(of(CustomRodManager.getLine(rod)));
        if (CustomRodManager.hasHook(rod)) e = e.plus(of(CustomRodManager.getHook(rod)));
        if (CustomRodManager.hasBobber(rod)) e = e.plus(of(CustomRodManager.getBobber(rod)));
        return e;
    }

    /** Multiply the bite wait by this (bite speed +10% -> waits 1/1.1 as long). */
    public double biteWaitFactor() {
        return 1.0 / (1.0 + bite);
    }

    /** Tooltip lines: "+10% casting range", "Fish fight 8% easier"... */
    public List<Component> describe(ChatFormatting color) {
        List<Component> out = new ArrayList<>();
        if (cast != 0) out.add(line(color, pct(cast) + " casting range"));
        if (bite != 0) out.add(line(color, pct(bite) + " bite speed"));
        if (window != 0) out.add(line(color, pct(window) + " hook window"));
        if (fight != 0) out.add(line(color, "Fish fight " + Math.round(Math.abs(fight) * 100) + "% " + (fight < 0 ? "easier" : "harder")));
        if (luck != 0) out.add(line(color, String.format("%+d fishing luck", luck)));
        if (quality != 0) out.add(line(color, pct(quality) + " better catch quality"));
        if (land != 0) out.add(line(color, pct(land) + " to land Good and Bad hooks"));
        if (xp != 0) out.add(line(color, pct(xp) + " fishing XP"));
        return out;
    }

    private static Component line(ChatFormatting color, String text) {
        return Component.literal(text).withStyle(color);
    }

    private static String pct(double v) {
        return String.format("%+d%%", Math.round(v * 100));
    }
}
