package com.li64.tide.data.rods;

import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.journal.FishRarity;
import com.li64.tide.data.journal.JournalGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * What a lure draws in. Each lure multiplies the chance of some fish (big ones, small ones, rare ones,
 * cave fish) or of crates, among whatever can be caught where you are; it never adds fish that couldn't
 * bite there. Lures don't wear out.
 */
public final class Lures {
    private Lures() {}

    /** Which fish a lure favours, and how much; {@code when} limits it to a time or place. */
    record Pull(String text, Predicate<FishData> fish, double factor, RodEffects.When when) {}

    private static boolean big(FishData d) { return d.getAverageLength() >= 60; }
    private static boolean small(FishData d) { return d.size().isPresent() && d.getAverageLength() < 30; }
    private static boolean atLeast(FishData d, FishRarity r) { return d.profile().rarity().ordinal() >= r.ordinal(); }

    private static final Map<String, List<Pull>> FISH = Map.of(
            "spinner_lure", List.of(new Pull("Attracts big fish", Lures::big, 2.0, null)),
            "fly_lure", List.of(new Pull("Attracts small fish", Lures::small, 2.0, null)),
            "jig_lure", List.of(new Pull("Attracts cave fish", d -> d.profile().group() == JournalGroup.UNDERGROUND, 2.5, null)),
            "popper_lure", List.of(new Pull("Attracts common fish", d -> d.profile().rarity() == FishRarity.COMMON, 1.5, RodEffects.When.DAY)),
            "glow_lure", List.of(new Pull("Attracts rare fish", d -> atLeast(d, FishRarity.RARE), 1.6, RodEffects.When.NIGHT)),
            "golden_spoon_lure", List.of(new Pull("Attracts uncommon and rarer fish", d -> atLeast(d, FishRarity.UNCOMMON), 1.3, null)),
            "echo_lure", List.of(new Pull("Attracts legendary fish", d -> d.profile().rarity() == FishRarity.LEGENDARY, 2.5, null),
                    new Pull("Attracts very rare fish", d -> d.profile().rarity() == FishRarity.VERY_RARE, 1.5, null)));

    /** Lures that bring up more crates, and by how much. */
    private static final Map<String, Double> CRATES = Map.of("magnet_lure", 2.5);

    private static String lure(FishingContext context) {
        if (context == null || context.rod() == null || !CustomRodManager.hasLure(context.rod())) return null;
        return BuiltInRegistries.ITEM.getKey(CustomRodManager.getLure(context.rod()).getItem()).getPath();
    }

    private static Player angler(FishingContext context) {
        return context.hook() == null ? null : context.hook().getPlayerOwner();
    }

    /** Multiply a fish's selection weight by this. */
    public static double fishFactor(FishingContext context, FishData data) {
        String lure = lure(context);
        if (lure == null || !FISH.containsKey(lure)) return 1.0;
        double f = 1.0;
        for (Pull pull : FISH.get(lure)) {
            if (pull.when() != null && (angler(context) == null || !pull.when().test(angler(context)))) continue;
            if (pull.fish().test(data)) f *= pull.factor();
        }
        return f;
    }

    /** Multiply a crate's selection weight by this. */
    public static double crateFactor(FishingContext context) {
        String lure = lure(context);
        return lure == null ? 1.0 : CRATES.getOrDefault(lure, 1.0);
    }

    /** Tooltip lines for a lure: what it draws in. */
    public static List<Component> describe(ItemStack stack) {
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        List<Component> out = new java.util.ArrayList<>();
        for (Pull pull : FISH.getOrDefault(path, List.of()))
            out.add(Component.literal(pull.text() + " (x" + trim(pull.factor()) + ")" + (pull.when() == null ? "" : " " + pull.when().text))
                    .withStyle(ChatFormatting.DARK_AQUA));
        if (CRATES.containsKey(path))
            out.add(Component.literal("Pulls up crates (x" + trim(CRATES.get(path)) + ")").withStyle(ChatFormatting.DARK_AQUA));
        if (!out.isEmpty()) out.add(Component.literal("Never wears out").withStyle(ChatFormatting.GRAY));
        return out;
    }

    private static String trim(double v) {
        return v == Math.floor(v) ? String.valueOf((int) v) : String.valueOf(v);
    }
}
