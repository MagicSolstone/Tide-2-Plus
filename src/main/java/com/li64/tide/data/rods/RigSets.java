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
 */
public final class RigSets {
    private RigSets() {}

    public static final int PIECES = 3;

    public record RigSet(String name, When when, RodEffects bonus, Set<String> members) {
        public String bonusText() {
            return String.join(", ", bonus.describe(ChatFormatting.WHITE).stream().map(Component::getString).toList()).toLowerCase() + " " + when.text;
        }
    }

    private static RigSet set(String name, When when, RodEffects bonus, String... members) {
        return new RigSet(name, when, bonus, Set.of(members));
    }

    private static final RodEffects LUCK_QUALITY = new RodEffects(0, 0, 0, 0, 1, 0.10, 0, 0);
    private static final RodEffects LUCK_BITE = new RodEffects(0, 0.10, 0, 0, 1, 0, 0, 0);

    public static final List<RigSet> SETS = List.of(
            set("Ocean rig", When.OCEAN, LUCK_QUALITY, "prismarine_fishing_rod", "conduit_fishing_rod", "drowned_fishing_rod",
                    "kelp_line", "prismarine_line", "prismarine_hook", "nautilus_bobber", "prismarine_bobber", "turtle_bobber",
                    "squid_lure", "coastal_bait", "brine_shrimp"),
            set("River rig", When.RIVER, new RodEffects(0, 0.15, 0, 0, 0, 0, 0, 0.10), "silk_line", "wooden_hook",
                    "feather_bobber", "duck_bobber", "fly_lure", "dough_ball"),
            set("Night rig", When.NIGHT, new RodEffects(0, 0, 0, 0, 1, 0, 0, 0.15), "bone_fishing_rod", "widow_fishing_rod",
                    "phantom_line", "spider_line", "glow_hook", "glow_berry_bobber", "pumpkin_bobber", "glow_lure", "ink_lure",
                    "glow_worms", "moon_bait"),
            set("Twilight rig", When.DAWN_DUSK, new RodEffects(0, 0, 0, 0, 0, 0.10, 0, 0.20), "cherry_fishing_rod",
                    "feathered_hook", "twilight_hook", "cherry_bobber", "grassy_bobber", "popper_lure", "blossom_bait"),
            set("Storm rig", When.RAIN, LUCK_QUALITY, "copper_fishing_rod", "copper_line", "copper_hook", "copper_bobber", "storm_lure"),
            set("Swamp rig", When.SWAMP, LUCK_BITE, "mangrove_fishing_rod", "slime_line", "lily_pad_bobber", "slime_bobber",
                    "frog_lure", "swamp_bait"),
            set("Jungle rig", When.JUNGLE, LUCK_QUALITY, "bamboo_fishing_rod", "vine_line", "apple_bobber", "jungle_bait"),
            set("Desert rig", When.DRY, LUCK_BITE, "cactus_fishing_rod", "golden_line", "golden_hook", "bone_hook",
                    "cactus_bobber", "desert_bait"),
            set("Frost rig", When.COLD, new RodEffects(0, 0.15, 0, 0, 0, 0.10, 0, 0), "frost_fishing_rod", "permafrost_hook",
                    "snowball_bobber", "ice_jig_lure", "frost_bait"),
            set("Cave rig", When.UNDERGROUND, LUCK_QUALITY, "echo_fishing_rod", "slime_line", "echo_bobber", "sculk_bobber",
                    "jig_lure", "echo_lure", "cave_grubs", "dripstone_bait", "sculk_bait"),
            set("Fire rig", When.LAVA, LUCK_QUALITY, "blazing_fishing_rod", "netherite_fishing_rod", "netherite_line",
                    "lavaproof_hook", "fiery_hook", "netherite_bobber", "magma_lure"),
            set("Void rig", When.END, LUCK_BITE, "void_hook", "chorus_bobber", "chorus_lure"));

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

    /** The bonus from every complete rig that's working right now. */
    public static RodEffects active(Player player, ItemStack rod) {
        RodEffects e = RodEffects.NONE;
        for (RigSet set : SETS)
            if (pieces(set, rod) >= PIECES && set.when().test(player)) e = e.plus(set.bonus());
        return e;
    }

    /** Tooltip lines for a piece that belongs to one or more rigs. */
    public static List<Component> describe(ItemStack stack) {
        List<Component> out = new ArrayList<>();
        for (RigSet set : SETS)
            if (set.members().contains(path(stack)))
                out.add(Component.literal(set.name() + " piece: " + PIECES + "+ pieces give " + set.bonusText()).withStyle(ChatFormatting.LIGHT_PURPLE));
        return out;
    }
}
