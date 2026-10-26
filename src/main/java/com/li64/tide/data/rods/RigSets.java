package com.li64.tide.data.rods;

import com.li64.tide.data.rods.RodEffects.When;
import com.li64.tide.data.tackle.TackleLog;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Matched rigs: tackle made for the same place or time. Three or more pieces of one family on a rod (counting
 * the rod, line, hook, bobber, lure and bait as one piece each, whichever family members they are) add the
 * family's bonus while you fish where or when it's made for.
 *
 * <p>HOW TO CHANGE THINGS HERE (see CODE_GUIDE.md): the rigs are the {@link #SETS} list below, one line each: name,
 * where/when it works, the two-piece bonus, the three-piece bonus, then the item names that belong to it. A rig counts
 * at most one piece per kind of tackle, so it needs members of three or more kinds. The iron rig also gives the rod a
 * sword's attack ({@link #refreshMelee}).
 */
public final class RigSets {
    private RigSets() {}

    /** Two pieces of a family give its small bonus; three or more, its full bonus. */
    public static final int HALF = 2, PIECES = 3;

    public record RigSet(String name, When when, RodEffects two, RodEffects full, Set<String> members, String twoNote, String note) {
        /** The full (3-piece) bonus in words, with where it works. */
        public String bonusText() {
            return join(text(full), note);
        }

        /** The 2-piece bonus in words, with where it works. */
        public String twoText() {
            return join(text(two), twoNote);
        }

        private static String join(String numbers, String note) {
            return numbers.isEmpty() ? note : note.isEmpty() ? numbers : numbers + "; " + note;
        }

        private String text(RodEffects fx) {
            String numbers = String.join(", ", fx.describe(ChatFormatting.WHITE).stream().map(Component::getString).toList()).toLowerCase();
            return numbers.isEmpty() ? "" : numbers + (when.text.isEmpty() ? "" : " " + when.text);
        }

        /** What this many pieces give (nothing below two). */
        public RodEffects bonus(int pieces) {
            return pieces >= PIECES ? full : pieces >= HALF ? two : RodEffects.NONE;
        }
    }

    private static RigSet set(String name, When when, RodEffects two, RodEffects full, String... members) {
        return new RigSet(name, when, two, full, Set.of(members), "", "");
    }

    /** Iron: works anywhere. Two pieces make the rod hit like a wooden sword, three like an iron sword (see {@link #refreshMelee}). */
    private static final String IRON_TWO_NOTE = "hits like a wooden sword", IRON_NOTE = "hits like an iron sword";

    private static RodEffects fx(double bite, int luck, double quality, double xp) {
        return new RodEffects(0, bite, 0, 0, luck, quality, 0, xp);
    }

    /** A full rig: its own thing, plus luck and a big quality boost. */
    private static RodEffects rig(double bite, int luck, double quality, double xp) {
        return new RodEffects(0, bite, 0, 0, luck, quality, 0, xp);
    }

    // The usual shapes: a little of one thing at two pieces; at three, the best kit there is for that place or time
    // (it beats any always-on set there, so a trip is worth a change of tackle), but only there.
    private static final RodEffects QUALITY_2 = fx(0, 0, 0.08, 0), LUCK_QUALITY_3 = rig(0, 2, 0.50, 0);
    private static final RodEffects BITE_2 = fx(0.10, 0, 0, 0), LUCK_BITE_3 = rig(0.25, 2, 0.36, 0);

    public static final List<RigSet> SETS = List.of(
            set("Ocean rig", When.OCEAN, QUALITY_2, LUCK_QUALITY_3, "prismarine_fishing_rod", "conduit_fishing_rod", "drowned_fishing_rod",
                    "kelp_line", "prismarine_line", "prismarine_hook", "nautilus_bobber", "prismarine_bobber", "turtle_bobber",
                    "squid_lure", "coastal_bait", "brine_shrimp"),
            set("River rig", When.RIVER, BITE_2, rig(0.25, 1, 0.32, 0.15), "silk_line", "wooden_hook",
                    "feather_bobber", "duck_bobber", "fly_lure", "dough_ball"),
            set("Night rig", When.NIGHT, fx(0, 0, 0, 0.10), rig(0, 2, 0.42, 0.25), "bone_fishing_rod", "widow_fishing_rod",
                    "phantom_line", "spider_line", "glow_hook", "glow_berry_bobber", "pumpkin_bobber", "glow_lure", "ink_lure",
                    "glow_worms", "moon_bait"),
            set("Twilight rig", When.DAWN_DUSK, fx(0, 0, 0, 0.10), rig(0, 1, 0.42, 0.30), "cherry_fishing_rod",
                    "feathered_hook", "twilight_hook", "cherry_bobber", "grassy_bobber", "popper_lure", "blossom_bait"),
            set("Storm rig", When.RAIN, QUALITY_2, LUCK_QUALITY_3, "copper_fishing_rod", "copper_line", "copper_hook", "copper_bobber", "storm_lure"),
            set("Swamp rig", When.SWAMP, BITE_2, LUCK_BITE_3, "mangrove_fishing_rod", "slime_line", "lily_pad_bobber", "slime_bobber",
                    "frog_lure", "swamp_bait"),
            set("Jungle rig", When.JUNGLE, QUALITY_2, LUCK_QUALITY_3, "bamboo_fishing_rod", "vine_line", "apple_bobber", "jungle_bait"),
            set("Desert rig", When.DRY, BITE_2, LUCK_BITE_3, "cactus_fishing_rod", "golden_line", "golden_hook", "fish_bone_hook",
                    "cactus_bobber", "desert_bait"),
            set("Frost rig", When.COLD, BITE_2, rig(0.20, 1, 0.42, 0), "frost_fishing_rod", "permafrost_hook",
                    "snowball_bobber", "ice_jig_lure", "frost_bait"),
            set("Cave rig", When.UNDERGROUND, QUALITY_2, LUCK_QUALITY_3, "echo_fishing_rod", "slime_line", "echo_bobber", "sculk_bobber", "lichen_bobber",
                    "jig_lure", "echo_lure", "cave_grubs", "dripstone_bait", "sculk_bait"),
            set("Fire rig", When.LAVA, QUALITY_2, LUCK_QUALITY_3, "blazing_fishing_rod", "netherite_fishing_rod", "netherite_line",
                    "lavaproof_hook", "fiery_hook", "netherite_bobber", "magma_lure"),
            set("Void rig", When.END, BITE_2, LUCK_BITE_3, "void_hook", "chorus_bobber", "chorus_lure"),
            new RigSet("Iron rig", When.ALWAYS, new RodEffects(0, 0, 0.05, 0, 0, 0, 0, 0), new RodEffects(0, 0, 0.10, -0.10, 0, 0, 0, 0),
                    Set.of("iron_fishing_rod", "iron_line", "fishing_hook", "iron_bobber"), IRON_TWO_NOTE, IRON_NOTE),
            // the material sets: always on, a little weaker than a place-and-time rig in its own place, each with its own twist
            new RigSet("Stone set", When.ALWAYS, RodEffects.NONE, RodEffects.NONE,
                    Set.of("stone_fishing_rod", "stone_hook", "fishing_line"), "the rod wears half as fast", "the rod barely wears"),
            new RigSet("Gold set", When.ALWAYS, new RodEffects(0, 0, 0, 0, 0, 0, 0, 0.10), new RodEffects(0, 0, 0, 0, 0, 0, 0, 0.20),
                    Set.of("golden_fishing_rod", "midas_fishing_rod", "golden_line", "golden_hook", "golden_bobber", "golden_spoon_lure"),
                    "", "25% chance of a gold nugget with each fish"),
            new RigSet("Diamond set", When.ALWAYS, new RodEffects(0, 0, 0, 0, 0, 0.10, 0, 0), new RodEffects(0, 0, 0, 0, 0, 0.25, 0, 0),
                    Set.of("diamond_fishing_rod", "diamond_line", "diamond_hook", "diamond_bobber"), "", ""),
            new RigSet("Amethyst set", When.ALWAYS, RodEffects.NONE, RodEffects.NONE,
                    Set.of("amethyst_fishing_rod", "amethyst_line", "amethyst_hook", "amethyst_bobber"),
                    "10% chance of an amethyst shard with each fish", "20% chance of an amethyst shard with each fish"),
            new RigSet("Honey set", When.ALWAYS, RodEffects.NONE, new RodEffects(0, 0, 0, 0, 0, 0, 0.10, 0),
                    Set.of("honeycomb_fishing_rod", "honey_line", "honey_bobber"),
                    "15% chance of a honeycomb with each fish", "25% chance of a honey bottle with each fish"));

    /** What a set brings up with each fish: at two pieces, and at three or more (by set name). */
    private record SetDrops(List<RodEffects.Drop> two, List<RodEffects.Drop> full) {}
    private static final java.util.Map<String, SetDrops> SET_DROPS = java.util.Map.of(
            "Gold set", new SetDrops(List.of(), List.of(new RodEffects.Drop("minecraft:gold_nugget", 0.25, 0, "Gold set: 25% chance of a gold nugget"))),
            "Amethyst set", new SetDrops(List.of(new RodEffects.Drop("minecraft:amethyst_shard", 0.10, 0, "Amethyst set: 10% chance of an amethyst shard")),
                    List.of(new RodEffects.Drop("minecraft:amethyst_shard", 0.20, 0, "Amethyst set: 20% chance of an amethyst shard"))),
            "Honey set", new SetDrops(List.of(new RodEffects.Drop("minecraft:honeycomb", 0.15, 0, "Honey set: 15% chance of a honeycomb")),
                    List.of(new RodEffects.Drop("minecraft:honey_bottle", 0.25, 0, "Honey set: 25% chance of a honey bottle"))));

    /** The extra things this rod's sets bring up with each fish. */
    public static List<RodEffects.Drop> drops(ItemStack rod) {
        List<RodEffects.Drop> out = new ArrayList<>();
        for (RigSet set : SETS) {
            SetDrops d = SET_DROPS.get(set.name());
            if (d == null) continue;
            int n = pieces(set, rod);
            out.addAll(n >= PIECES ? d.full() : n >= HALF ? d.two() : List.of());
        }
        return out;
    }

    /** The share of normal wear a rod takes (the Stone set: half at two pieces, a tenth at three). */
    public static double wear(ItemStack rod) {
        int n = SETS.stream().filter(s -> s.name().equals("Stone set")).mapToInt(s -> pieces(s, rod)).max().orElse(0);
        return n >= PIECES ? 0.1 : n >= HALF ? 0.5 : 1.0;
    }

    private static String path(ItemStack s) {
        return BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
    }

    /** How many pieces of this family are on the rod (one per slot). */
    public static int pieces(RigSet set, ItemStack rod) {
        List<ItemStack> all = new ArrayList<>(List.of(rod));
        all.addAll(RodEffects.parts(rod));
        return filled(set, all).size();
    }

    private static EnumSet<TackleLog.Category> filled(RigSet set, List<ItemStack> pieces) {
        EnumSet<TackleLog.Category> slots = EnumSet.noneOf(TackleLog.Category.class);
        for (ItemStack s : pieces) {
            if (s.isEmpty()) continue;
            TackleLog.Category c = TackleLog.category(s.getItem());
            if (c != null && set.members().contains(path(s))) slots.add(c);
        }
        return slots;
    }

    /** A rig in progress on a rod being put together: how many pieces, and which kinds of piece could still be added. */
    public record Progress(RigSet set, int have, List<String> canAdd) {}

    /** Every rig with at least one piece among these (a rod and what's on it), most complete first. */
    public static List<Progress> progress(List<ItemStack> pieces) {
        List<Progress> out = new ArrayList<>();
        for (RigSet set : SETS) {
            EnumSet<TackleLog.Category> have = filled(set, pieces);
            if (have.isEmpty()) continue;
            EnumSet<TackleLog.Category> kinds = EnumSet.noneOf(TackleLog.Category.class);
            for (String m : set.members())
                BuiltInRegistries.ITEM.getOptional(com.li64.tide.Tide.resource(m)).map(TackleLog::category).ifPresent(kinds::add);
            kinds.removeAll(have);
            out.add(new Progress(set, have.size(), kinds.stream().map(k -> k.label.toLowerCase().replaceAll("s$", "")).toList()));
        }
        out.sort((a, b) -> b.have() - a.have());
        return out;
    }

    /** Attack damage added to a rod by the iron rig: +3 at two pieces (a wooden sword's 4), +5 at three (an iron sword's 6). */
    private static final net.minecraft.resources.Identifier IRON_RIG_ID = com.li64.tide.Tide.resource("iron_rig");

    public static boolean isIronRig(RigSet set) {
        return set.name().equals("Iron rig");
    }

    /**
     * Call after anything on a rod changes: gives the rod a sword's attack while it has two or three pieces of the iron
     * rig, and takes it away again. It also gives the rod the "weapon" property, so each hit on a mob wears it down by 1
     * like a sword (a worn-out rod stops at broken, it never breaks: see ItemStackMixin).
     */
    public static void refreshMelee(ItemStack rod) {
        if (rod == null || rod.isEmpty() || !BuiltInRegistries.ITEM.getKey(rod.getItem()).getNamespace().equals("tide")) return;
        int pieces = SETS.stream().filter(RigSets::isIronRig).mapToInt(s -> pieces(s, rod)).max().orElse(0);
        double damage = pieces >= PIECES ? 5.0 : pieces >= HALF ? 3.0 : 0;
        var current = rod.get(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);
        double has = current == null ? 0 : current.modifiers().stream().filter(m -> m.modifier().id().equals(IRON_RIG_ID))
                .filter(m -> m.attribute().equals(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE))
                .mapToDouble(m -> m.modifier().amount()).findFirst().orElse(0);
        if (damage == has && (damage == 0 || rod.has(net.minecraft.core.component.DataComponents.WEAPON))) return;
        if (damage == 0) {
            rod.remove(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);
            rod.remove(net.minecraft.core.component.DataComponents.WEAPON);
            return;
        }
        rod.set(net.minecraft.core.component.DataComponents.WEAPON, new net.minecraft.world.item.component.Weapon(1));
        rod.set(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, net.minecraft.world.item.component.ItemAttributeModifiers.builder()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                        IRON_RIG_ID, damage, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED, new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                        IRON_RIG_ID, -2.4, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE), net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND)
                .build());
    }

    /** The bonus from every complete rig that's working right now. */
    public static RodEffects active(Player player, ItemStack rod) {
        RodEffects e = RodEffects.NONE;
        for (RigSet set : SETS)
            if (set.when().test(player)) e = e.plus(set.bonus(pieces(set, rod)));
        return e;
    }

    /** One short tooltip line naming the rigs a piece belongs to ("Part of: Storm rig"), or null. */
    public static Component memberLine(ItemStack stack) {
        List<String> names = new ArrayList<>();
        for (RigSet set : SETS) if (set.members().contains(path(stack))) names.add(set.name());
        return names.isEmpty() ? null : Component.literal("Part of: " + String.join(", ", names)).withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    /** Tooltip lines for a piece that belongs to one or more rigs. */
    public static List<Component> describe(ItemStack stack) {
        List<Component> out = new ArrayList<>();
        for (RigSet set : SETS)
            if (set.members().contains(path(stack)))
                out.add(Component.literal(set.name() + " piece: 2 pieces give " + set.twoText() + "; 3 or more give " + set.bonusText()).withStyle(ChatFormatting.LIGHT_PURPLE));
        return out;
    }
}
