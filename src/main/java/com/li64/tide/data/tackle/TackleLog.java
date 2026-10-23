package com.li64.tide.data.tackle;

import com.li64.tide.Tide;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.rods.RodEffects;
import com.li64.tide.network.messages.ShowToastMsg;
import com.li64.tide.network.messages.TackleSyncMsg;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.*;

/**
 * The tackle section of the journal: every rod, line, hook, bobber, lure, bait and piece of gear the player has
 * held. Checked every two seconds (inventory, what's on their rods, inside tackle boxes), saved under "TideTackle"
 * (its own tag: the angler data rewrites its tag whole), and sent to the client for the journal.
 */
public final class TackleLog {
    private TackleLog() {}

    private static final String TAG = "TideTackle";

    public enum Category {
        RODS("Rods"), LINES("Lines"), HOOKS("Hooks"), BOBBERS("Bobbers"), LURES("Lures"), BAIT("Bait"), GEAR("Gear");
        public final String label;
        Category(String label) { this.label = label; }
    }

    private static final Set<String> GEAR = Set.of("angler_hat", "fishing_vest", "waders", "wading_boots", "tackle_box", "fish_satchel");

    /** What kind of tackle this is, or null if it isn't tackle (works on both sides: tags are synced). */
    public static Category category(Item item) {
        var key = BuiltInRegistries.ITEM.getKey(item);
        if (!key.getNamespace().equals("tide")) return null;
        ItemStack stack = new ItemStack(item);
        if (stack.is(TideTags.Items.FISHING_RODS)) return Category.RODS;
        if (stack.is(TideTags.Items.LINES)) return Category.LINES;
        if (stack.is(TideTags.Items.HOOKS)) return Category.HOOKS;
        if (stack.is(TideTags.Items.BOBBERS)) return Category.BOBBERS;
        if (stack.is(TideTags.Items.LURES)) return Category.LURES;
        if (stack.is(TideTags.Items.BAIT_ITEMS)) return Category.BAIT;
        if (GEAR.contains(key.getPath())) return Category.GEAR;
        return null;
    }

    /** Whether a tackle box takes this: lines, hooks, bobbers, lures and bait. */
    public static boolean fitsTackleBox(ItemStack stack) {
        Category c = stack.isEmpty() ? null : category(stack.getItem());
        return c == Category.LINES || c == Category.HOOKS || c == Category.BOBBERS || c == Category.LURES || c == Category.BAIT;
    }

    /** Tackle that isn't crafted, and where it comes from. */
    public static final Map<String, String> FOUND = Map.ofEntries(
            Map.entry("echo_fishing_rod", "Found in ancient city chests."),
            Map.entry("blazing_fishing_rod", "Found in nether fortress chests."),
            Map.entry("prismarine_fishing_rod", "Dropped by elder guardians."),
            Map.entry("honeycomb_fishing_rod", "Sometimes drops when you harvest honeycomb from a hive."),
            Map.entry("sunflower_fishing_rod", "Fished up in plains biomes (rare)."),
            Map.entry("village_fishing_rod", "Bought from a fisherman villager."),
            Map.entry("enchanted_golden_apple_bobber", "Found in treasure, like the enchanted golden apple."),
            Map.entry("magnetic_bait", "Crafted, or found in fishing crates."),
            Map.entry("abyss_bait", "Made from a pale clubfish."),
            Map.entry("incandescent_bait", "Made from an incandescent larva."));

    /** How a piece of tackle is made: its crafting grid, its smithing recipe, or where it's found. */
    public static com.li64.tide.network.messages.RecipeViewMsg recipeFor(net.minecraft.server.MinecraftServer server, String itemId) {
        Item target = BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(itemId));
        net.minecraft.server.level.ServerLevel level = server.overworld();
        for (var h : server.getRecipeManager().getRecipes()) {
            var r = h.value();
            try {
                if (r instanceof net.minecraft.world.item.crafting.ShapedRecipe s) {
                    List<List<String>> slots = s.getIngredients().stream().map(o -> o.map(TackleLog::options).orElse(List.of())).toList();
                    if (makes(s, s.getWidth(), s.getHeight(), slots, target))
                        return new com.li64.tide.network.messages.RecipeViewMsg(itemId, "crafting", s.getWidth(), s.getHeight(), slots, "");
                } else if (r instanceof net.minecraft.world.item.crafting.ShapelessRecipe s) {
                    List<List<String>> slots = s.placementInfo().ingredients().stream().map(TackleLog::options).toList();
                    int w = Math.min(3, slots.size()), hh = (slots.size() + 2) / 3;
                    List<List<String>> padded = new ArrayList<>(slots);
                    while (padded.size() < w * hh) padded.add(List.of());
                    if (makes(s, w, hh, padded, target))
                        return new com.li64.tide.network.messages.RecipeViewMsg(itemId, "shapeless", w, hh, padded, "");
                } else if (r instanceof net.minecraft.world.item.crafting.SmithingTransformRecipe s) {
                    List<List<String>> slots = s.placementInfo().ingredients().stream().map(TackleLog::options).toList();
                    if (slots.size() == 3) {
                        ItemStack out = s.assemble(new net.minecraft.world.item.crafting.SmithingRecipeInput(first(slots.get(0)), first(slots.get(1)), first(slots.get(2))));
                        if (out.is(target)) return new com.li64.tide.network.messages.RecipeViewMsg(itemId, "smithing", 3, 1, slots, "");
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        String path = BuiltInRegistries.ITEM.getKey(target).getPath();
        return new com.li64.tide.network.messages.RecipeViewMsg(itemId, "found", 0, 0, List.of(), FOUND.getOrDefault(path, "Found while fishing or exploring."));
    }

    private static List<String> options(net.minecraft.world.item.crafting.Ingredient ing) {
        return ing.items().limit(8).map(h -> BuiltInRegistries.ITEM.getKey(h.value()).toString()).toList();
    }

    private static ItemStack first(List<String> ids) {
        return ids.isEmpty() ? ItemStack.EMPTY : new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(ids.get(0))));
    }

    private static boolean makes(net.minecraft.world.item.crafting.CraftingRecipe r, int w, int h, List<List<String>> slots, Item target) {
        List<ItemStack> items = slots.stream().map(TackleLog::first).toList();
        if (items.size() != w * h) return false;
        return r.assemble(net.minecraft.world.item.crafting.CraftingInput.of(w, h, items)).is(target);
    }

    public static Set<String> found(ServerPlayer player) {
        CompoundTag tag = Tide.PLATFORM.getPlayerData(player).getCompoundOrEmpty(TAG);
        String s = tag.getStringOr("found", "");
        return s.isEmpty() ? new LinkedHashSet<>() : new LinkedHashSet<>(Arrays.asList(s.split(",")));
    }

    private static void save(ServerPlayer player, Set<String> found) {
        CompoundTag tag = new CompoundTag();
        tag.putString("found", String.join(",", found));
        Tide.PLATFORM.getPlayerData(player).put(TAG, tag);
    }

    public static void clear(ServerPlayer player) {
        Tide.PLATFORM.getPlayerData(player).remove(TAG);
        sync(player);
    }

    public static void sync(ServerPlayer player) {
        Tide.NETWORK.sendToPlayer(new TackleSyncMsg(new ArrayList<>(found(player))), player);
    }

    public static void tick(ServerPlayer player) {
        if (player.isSpectator()) return;
        Set<String> found = found(player);
        List<ItemStack> newOnes = new ArrayList<>();
        var inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            check(stack, found, newOnes);
            if (stack.is(TideTags.Items.FISHING_RODS)) for (ItemStack part : RodEffects.parts(stack)) check(part, found, newOnes);
            ItemContainerContents box = stack.get(DataComponents.CONTAINER);
            if (box != null && BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().equals("tackle_box"))
                box.nonEmptyItemCopyStream().forEach(s -> check(s, found, newOnes));
        }
        if (newOnes.isEmpty()) return;
        save(player, found);
        sync(player);
        // the client translates the item name (the toast message sends text it looks up)
        for (ItemStack s : newOnes.subList(0, Math.min(3, newOnes.size())))
            Tide.NETWORK.sendToPlayer(new ShowToastMsg(Component.literal("New tackle in your journal"),
                    Component.literal(s.getItem().getDescriptionId()), s), player);
    }

    private static void check(ItemStack stack, Set<String> found, List<ItemStack> newOnes) {
        if (category(stack.getItem()) == null) return;
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        if (found.add(path)) newOnes.add(stack.copyWithCount(1));
    }
}
