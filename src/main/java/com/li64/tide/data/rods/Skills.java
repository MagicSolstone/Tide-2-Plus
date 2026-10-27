package com.li64.tide.data.rods;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * What a piece does besides the base stats ({@link Stat}), each with a one- or two-word name: "Hot Line 20%",
 * "Green Gold 3%", "Fish Finder". The tooltip shows the name (in the skill colour) with the stats, and what it does in
 * grey at the bottom; the rod panel and the Angling Table show just the names, so a rod's tricks are never forgotten.
 * The drops and tricks themselves live where they always did (RodEffects drops and picks, FoundRodTricks, the hook).
 */
public final class Skills {
    private Skills() {}

    /** A skill: its name, its chance with each fish (or -1 when it always works), and what it does. */
    public record Skill(String name, double chance, String explain) {
        public String label() {
            return chance < 0 ? name : name + " " + Math.round(chance * 100) + "%";
        }
    }

    private static Skill s(String name, double chance, String explain) {
        return new Skill(name, chance, explain);
    }

    private static final Map<String, List<Skill>> ITEMS = Map.ofEntries(
            // rods
            Map.entry("honeycomb_fishing_rod", List.of(s("Beekeeper", 0.10, "10% chance of a honeycomb with any fish, and 50% of a honey bottle with Great or better fish"))),
            Map.entry("mangrove_fishing_rod", List.of(s("Slime Trail", 0.20, "20% chance of a slime ball with any fish"))),
            Map.entry("cherry_fishing_rod", List.of(s("Blossom", 0.25, "25% chance of pink petals with any fish"))),
            Map.entry("rotten_fishing_rod", List.of(s("Scavenger", 0.35, "35% chance of a mob drop with each fish (bones, string, gunpowder...)"))),
            Map.entry("grove_fishing_rod", List.of(s("Forager", 0.35, "35% chance of tree drops with each fish (saplings, sticks, apples...)"))),
            Map.entry("drowned_fishing_rod", List.of(s("Sunken Loot", 0.35, "35% chance of sunken loot with each fish (kelp, copper, prismarine...)"))),
            Map.entry("sickle_fishing_rod", List.of(s("Harvest", 0.25, "25% chance of seeds with each fish"),
                    s("Green Thumb", RodEffects.SICKLE_GROW, "35% chance each fish makes nearby crops grow"))),
            Map.entry("hex_fishing_rod", List.of(s("Hex", RodEffects.HEX_EFFECT, "20% chance each fish gives you a helpful potion effect"))),
            Map.entry("widow_fishing_rod", List.of(s("Night Hunter", -1, "Strong after dark, sluggish by day"))),
            Map.entry("wayfinder_fishing_rod", List.of(s("Far Reach", -1, "Stronger the farther you fish from world spawn: from 500 blocks out, up to +15% Quality, +30% Fortune and +10% Experience at 5,000 blocks"))),
            Map.entry("amethyst_fishing_rod", List.of(s("Moonlit", -1, "+8% Quality at night on a full moon (less as the moon wanes, none on a new moon)"))),
            Map.entry("village_fishing_rod", List.of(s("Mentor", -1, "Players fishing within 16 blocks of you get +10% Experience"),
                    s("Bounty", -1, "A bonus item with every catch: usually an emerald, or iron, a book, an apple, a stick or a poppy"))),
            Map.entry("crystal_fishing_rod", List.of(s("Fish Finder", -1, "Shows what bit, how good it is and how rare, and chimes, the moment a fish bites"))),
            Map.entry("echo_fishing_rod", List.of(s("Echo Sense", -1, "Shows what's on the hook the moment it bites, with a sculk click"))),
            Map.entry("midas_fishing_rod", List.of(s("Midas Touch", -1, "A gold item with every catch: usually a gold ingot, sometimes a golden carrot, golden apple or gold block"))),
            Map.entry("diamond_fishing_rod", List.of(s("Bonus XP", -1, "+1 to 4 experience with each catch, on top of the usual"))),
            Map.entry("blazing_fishing_rod", List.of(s("Hellfire", -1, "Nether fish bite in any lava"))),
            // lines, hooks, bobbers
            Map.entry("amethyst_line", List.of(s("Moonlit", -1, "+4% Quality at night on a full moon (less as the moon wanes, none on a new moon)"))),
            Map.entry("amethyst_hook", List.of(s("Moonlit", -1, "+6% Quality at night on a full moon (less as the moon wanes, none on a new moon)"))),
            Map.entry("amethyst_bobber", List.of(s("Moonlit", -1, "+5% Quality at night on a full moon (less as the moon wanes, none on a new moon)"))),
            Map.entry("stone_hook", List.of(s("Bottom Feeder", 0.25, "It sinks to the bottom: 15% chance of kelp and 10% of seagrass with any fish"))),
            Map.entry("emerald_hook", List.of(s("Green Gold", 0.03, "3% chance of an emerald with any fish"))),
            Map.entry("barbed_hook", List.of(s("Barbed", -1, "Lands almost anything (Good and Bad hooks always, Terrible ones half the time), but it tears the fish: never better than Great, and half the time a tier worse"))),
            Map.entry("lavaproof_hook", List.of(s("Lava Fishing", -1, "Lets you fish in lava"))),
            Map.entry("void_hook", List.of(s("Void Fishing", -1, "Lets you fish in the void"))),
            Map.entry("glow_hook", List.of(s("Glow", -1, "Glows, lighting the water around it"))),
            Map.entry("lichen_bobber", List.of(s("Glow", -1, "Glows, lighting the water around it"))),
            Map.entry("heart_bobber", List.of(s("Heartening", -1, "Heals you three hearts with each fish"))),
            Map.entry("bell_bobber", List.of(s("Bell", -1, "Rings when a fish bites"))),
            Map.entry("ender_eye_bobber", List.of(s("Ender Sight", -1, "Shows what's on the hook when it bites"))));

    /** Plain notes for pieces with no stats or skills of their own (grey, at the bottom of the tooltip). */
    private static final Map<String, String> NOTES = Map.of(
            "stone_fishing_rod", "No perks of its own: the start of the Stone rig (+10% Attraction with 2 pieces, +20% and +10% Experience with 3)",
            "red_bobber", "No perks of its own: dye it any colour. Part of the Stone rig (+10% Attraction with 2 pieces)");

    private static String path(ItemStack s) {
        return BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
    }

    private static boolean isTide(ItemStack s) {
        return BuiltInRegistries.ITEM.getKey(s.getItem()).getNamespace().equals("tide");
    }

    /** "Hot Line": a chance the fish comes up cooked (the redstone line, the blazing rod). */
    private static Skill hotLine(double cook) {
        return s("Hot Line", cook, Math.round(cook * 100) + "% chance each fish comes up cooked");
    }

    /** One item's own skills. */
    public static List<Skill> of(ItemStack stack) {
        List<Skill> out = new ArrayList<>();
        if (stack == null || stack.isEmpty() || !isTide(stack)) return out;
        out.addAll(ITEMS.getOrDefault(path(stack), List.of()));
        if (stack.is(com.li64.tide.data.TideTags.Items.LAVA_FISHING_RODS)) out.add(s("Lava Fishing", -1, "Lets you fish in lava"));
        double cook = stack.getItem() instanceof com.li64.tide.registries.items.TideFishingRodItem
                ? RodEffects.ownRod(stack).cook() : RodEffects.of(stack).cook();
        if (cook > 0) out.add(hotLine(cook));
        return out;
    }

    /**
     * A rod's skills with what's on it: its own, each piece's, and its rigs' (a rig's only when it counts). Skills with
     * the same name merge (two chances to come up cooked add up). Each comes with whether it is working right now.
     */
    public static List<Skill> ofRod(ItemStack rod) {
        LinkedHashMap<String, Skill> out = new LinkedHashMap<>();
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(RodEffects.parts(rod));
        double cook = 0;
        for (ItemStack piece : all)
            for (Skill k : of(piece)) {
                if (k.name().equals("Hot Line")) { cook += k.chance(); continue; }
                out.putIfAbsent(k.name(), k);
            }
        if (cook > 0) out.put("Hot Line", hotLine(cook));
        for (RigSets.RigSet set : RigSets.carried(rod)) {
            Skill k = RigSets.skill(set, RigSets.pieces(set, rod));
            if (k != null) out.putIfAbsent(k.name(), k);
        }
        return new ArrayList<>(out.values());
    }

    /** The skill names for a tooltip, in the skill colour ("Hot Line 20%"). */
    public static List<Component> lines(List<Skill> skills) {
        List<Component> out = new ArrayList<>();
        for (Skill k : skills) out.add(Component.literal(k.label()).withStyle(st -> st.withColor(Stat.SKILL)));
        return out;
    }

    /** What each skill does, in grey, for the bottom of a tooltip (and any plain note the item has). */
    public static List<Component> explanations(List<Skill> skills, ItemStack stack) {
        List<Component> out = new ArrayList<>();
        for (Skill k : skills)
            out.add(Component.literal(k.name() + ": ").withStyle(st -> st.withColor(Stat.SKILL))
                    .append(Component.literal(k.explain()).withStyle(ChatFormatting.GRAY)));
        if (stack != null && !stack.isEmpty() && isTide(stack) && NOTES.containsKey(path(stack)))
            out.add(Component.literal(NOTES.get(path(stack))).withStyle(ChatFormatting.GRAY));
        return out;
    }
}
