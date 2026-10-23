package com.li64.tide.data.angler;

import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.data.journal.FishRarity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Cooking keeps what made a fish special: "Cooked Mythic Largemouth Bass" fills you up more than
 * a cooked Poor one, and the good ones give buffs. Rare species make it a bit better again.
 *
 *   Poor -1 food · Average as usual · Good +1 · Great +2, Regeneration · Trophy +3, Regeneration, Absorption
 *   Mythic +5, Regeneration II, Absorption II, Luck, Water Breathing
 *   very rare species: +1 food and effects last 25% longer; legendary: +2 and 50% longer
 */
public final class CookedFish {
    private CookedFish() {}

    /** The cooked result of a raw fish (furnace, smoker, campfire): quality, name, food and buffs carried over. */
    public static ItemStack cook(ItemStack raw, ItemStack cooked) {
        Integer qid = TideItemData.FISH_QUALITY.getOrDefault(raw, null);
        if (qid == null || cooked.isEmpty()) return cooked;
        CatchQuality q = CatchQuality.byId(qid);
        FishRarity rarity = FishData.get(raw).map(d -> d.profile().rarity()).orElse(FishRarity.COMMON);
        TideItemData.FISH_QUALITY.set(cooked, q.ordinal());

        // "Cooked Mythic Largemouth Bass" (the species, not just "Cooked Fish")
        Component species = Component.translatable(raw.getItem().getDescriptionId());
        Component name = q == CatchQuality.AVERAGE
                ? Component.translatable("item.tide.cooked_name", species)
                : Component.translatable("item.tide.cooked_quality_name", Component.literal(q.label), species);
        cooked.set(DataComponents.ITEM_NAME, name.copy().withStyle(s -> q == CatchQuality.AVERAGE ? s.withItalic(false)
                : s.withColor(TextColor.fromRgb(q.color)).withItalic(false)));

        FoodProperties food = cooked.get(DataComponents.FOOD);
        if (food != null) {
            // Quality scales the food: a plain cooked fish (6) is Poor 1, Average 3, Good 4, Great 6, Trophy 8, Mythic 11
            double f = foodFactor(q);
            int extra = rarity == FishRarity.LEGENDARY ? 2 : rarity == FishRarity.VERY_RARE ? 1 : 0;
            cooked.set(DataComponents.FOOD, new FoodProperties(Math.max(1, (int) Math.round(food.nutrition() * f) + extra),
                    Math.max(0.2f, (float) (food.saturation() * f) + extra * 1.2f), food.canAlwaysEat() || q.ordinal() >= CatchQuality.TROPHY.ordinal()));
        }
        double longer = rarity == FishRarity.LEGENDARY ? 1.5 : rarity == FishRarity.VERY_RARE ? 1.25 : 1.0;
        List<MobEffectInstance> buffs = buffs(q, longer);
        Consumable eat = cooked.get(DataComponents.CONSUMABLE);
        if (eat != null && !buffs.isEmpty()) {
            List<ConsumeEffect> effects = new ArrayList<>(eat.onConsumeEffects());
            effects.add(new ApplyStatusEffectsConsumeEffect(buffs));
            cooked.set(DataComponents.CONSUMABLE, new Consumable(eat.consumeSeconds(), eat.animation(), eat.sound(), eat.hasConsumeParticles(), effects));
        }
        return cooked;
    }

    private static double foodFactor(CatchQuality q) {
        return switch (q) {
            case POOR -> 0.2;
            case AVERAGE -> 0.5;
            case GOOD -> 0.7;
            case GREAT -> 1.0;
            case TROPHY -> 1.3;
            case MYTHIC -> 1.8;
        };
    }

    private static List<MobEffectInstance> buffs(CatchQuality q, double longer) {
        List<MobEffectInstance> out = new ArrayList<>();
        switch (q) {
            case GREAT -> out.add(effect(MobEffects.REGENERATION, 8, 0, longer));
            case TROPHY -> {
                out.add(effect(MobEffects.REGENERATION, 12, 0, longer));
                out.add(effect(MobEffects.ABSORPTION, 60, 0, longer));
            }
            case MYTHIC -> {
                out.add(effect(MobEffects.REGENERATION, 20, 1, longer));
                out.add(effect(MobEffects.ABSORPTION, 120, 1, longer));
                out.add(effect(MobEffects.LUCK, 300, 0, longer));
                out.add(effect(MobEffects.WATER_BREATHING, 180, 0, longer));
            }
            default -> { }
        }
        return out;
    }

    private static MobEffectInstance effect(Holder<MobEffect> what, int seconds, int amplifier, double longer) {
        return new MobEffectInstance(what, (int) Math.round(seconds * 20 * longer), amplifier);
    }

    /** Tooltip lines for a cooked fish: what eating it gives. */
    public static List<Component> describe(ItemStack cooked) {
        List<Component> out = new ArrayList<>();
        Consumable eat = cooked.get(DataComponents.CONSUMABLE);
        if (eat == null || !TideItemData.FISH_QUALITY.isPresent(cooked)) return out;
        for (ConsumeEffect e : eat.onConsumeEffects()) {
            if (!(e instanceof ApplyStatusEffectsConsumeEffect apply)) continue;
            for (MobEffectInstance m : apply.effects()) {
                int secs = m.getDuration() / 20;
                out.add(Component.translatable(m.getDescriptionId()).append((m.getAmplifier() > 0 ? " II" : "") + String.format(" (%d:%02d)", secs / 60, secs % 60))
                        .withStyle(ChatFormatting.BLUE));
            }
        }
        if (!out.isEmpty()) out.add(0, Component.literal("When eaten:").withStyle(ChatFormatting.GRAY));
        return out;
    }
}
