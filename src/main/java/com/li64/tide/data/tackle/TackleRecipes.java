package com.li64.tide.data.tackle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.li64.tide.Tide;
import com.li64.tide.data.TideTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.*;

/**
 * Fishing gear is made at the Angling Table, not the crafting table or smithing table. When recipes load, Tide's
 * recipes for rods, lines, bobbers, hooks, lures, bait and fishing gear are taken out of the normal recipe list
 * (so the crafting table, smithing table and recipe book never see them) and kept here for the table's Craft tab.
 */
public final class TackleRecipes {
    private TackleRecipes() {}

    /** The table's filters, in order. */
    public enum Kind {
        ROD("Rods"), LINE("Lines"), BOBBER("Bobbers"), HOOK("Hooks"), LURE("Lures"), BAIT("Bait"), GEAR("Gear");
        public final String label;
        Kind(String label) { this.label = label; }
    }

    /** Fishing gear that isn't tackle but is still made at the table. */
    public static final Set<String> GEAR = Set.of(
            "fishing_journal", "tackle_box", "fish_satchel", "fish_finder", "depth_meter", "climate_gauge", "weather_radio",
            "lunar_calendar", "pocket_watch", "enchanted_pocket_watch", "angler_hat", "fishing_vest", "waders", "wading_boots",
            "dragonfin_boots", "fish_display");

    /** One recipe as the table shows it: what it makes and one ingredient per item it uses (smithing: template, base, addition). */
    public record Entry(Identifier id, Item result, int count, Kind kind, boolean smithing, List<Ingredient> ingredients,
                        int[] grid, RecipeHolder<?> holder) {}

    private static volatile List<Entry> entries = List.of();

    public static List<Entry> entries() {
        return entries;
    }

    public static Optional<Entry> byId(Identifier id) {
        for (Entry e : entries) if (e.id.equals(id)) return Optional.of(e);
        return Optional.empty();
    }

    /** The table's recipes plus everything else (the journal's "how is this made" looks in both). */
    public static List<RecipeHolder<?>> withMoved(Collection<RecipeHolder<?>> rest) {
        List<RecipeHolder<?>> all = new ArrayList<>(rest);
        for (Entry e : entries) all.add(e.holder);
        return all;
    }

    /** Called as recipes finish loading: keeps the table's recipes here and returns what's left for everything else. */
    public static RecipeMap split(RecipeMap map, java.util.function.BiFunction<Multimap<RecipeType<?>, RecipeHolder<?>>, Map<ResourceKey<Recipe<?>>, RecipeHolder<?>>, RecipeMap> make) {
        List<Entry> found = new ArrayList<>();
        Multimap<RecipeType<?>, RecipeHolder<?>> byType = ArrayListMultimap.create();
        Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey = new LinkedHashMap<>();
        for (RecipeHolder<?> h : map.values()) {
            Entry e = null;
            try {
                e = entryFor(h);
            } catch (Throwable t) {
                Tide.LOG.warn("Tide: couldn't read recipe {} for the angling table, leaving it at the crafting table", h.id().identifier(), t);
            }
            if (e != null) {
                found.add(e);
                continue;
            }
            byType.put(h.value().getType(), h);
            byKey.put(h.id(), h);
        }
        found.sort(Comparator.<Entry>comparingInt(e -> e.kind.ordinal()).thenComparingInt(e -> BuiltInRegistries.ITEM.getId(e.result)));
        entries = List.copyOf(found);
        Tide.LOG.info("Tide: {} recipes moved to the angling table", found.size());
        return found.isEmpty() ? map : make.apply(byType, byKey);
    }

    private static Entry entryFor(RecipeHolder<?> h) {
        Identifier id = h.id().identifier();
        if (!id.getNamespace().equals(Tide.MOD_ID)) return null;
        Recipe<?> r = h.value();
        List<Ingredient> ingredients;
        ItemStack out;
        boolean smithing = false;
        int[] grid = null;
        if (r instanceof ShapedRecipe s) {
            ingredients = s.getIngredients().stream().filter(Optional::isPresent).map(Optional::get).toList();
            List<Integer> at = new ArrayList<>();
            for (int i = 0; i < s.getIngredients().size(); i++)
                if (s.getIngredients().get(i).isPresent()) at.add(i % s.getWidth() + (i / s.getWidth()) * 3);
            grid = at.stream().mapToInt(Integer::intValue).toArray();
            out = s.assemble(CraftingInput.of(s.getWidth(), s.getHeight(), s.getIngredients().stream().map(TackleRecipes::sample).toList()));
        } else if (r instanceof ShapelessRecipe s) {
            ingredients = s.placementInfo().ingredients();
            List<ItemStack> in = ingredients.stream().map(i -> sample(Optional.of(i))).toList();
            out = s.assemble(CraftingInput.of(in.size(), 1, in));
        } else if (r instanceof SmithingTransformRecipe s) {
            ingredients = new ArrayList<>();
            s.templateIngredient().ifPresent(ingredients::add);
            ingredients.add(s.baseIngredient());
            s.additionIngredient().ifPresent(ingredients::add);
            out = s.assemble(new SmithingRecipeInput(sample(s.templateIngredient()), sample(Optional.of(s.baseIngredient())), sample(s.additionIngredient())));
            smithing = true;
        } else return null;
        if (out.isEmpty() || ingredients.isEmpty()) return null;
        Kind kind = kindOf(out);
        if (kind == null) return null;
        if (grid == null) {                                       // shapeless and upgrades: one per slot, from the top left
            if (ingredients.size() > 9) return null;
            grid = new int[ingredients.size()];
            for (int i = 0; i < grid.length; i++) grid[i] = i;
        }
        return new Entry(id, out.getItem(), out.getCount(), kind, smithing, List.copyOf(ingredients), grid, h);
    }

    private static ItemStack sample(Optional<Ingredient> ing) {
        return ing.flatMap(i -> i.items().findFirst()).map(item -> new ItemStack(item.value())).orElse(ItemStack.EMPTY);
    }

    /** Which filter an item belongs to, or null if it isn't made at the table. */
    public static Kind kindOf(ItemStack stack) {
        Identifier key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!key.getNamespace().equals(Tide.MOD_ID)) return null;
        if (key.getPath().equals("angling_table")) return null;                  // the table itself is made at the crafting table
        if (stack.is(TideTags.Items.FISHING_RODS)) return Kind.ROD;
        if (stack.is(TideTags.Items.LINES)) return Kind.LINE;
        if (stack.is(TideTags.Items.BOBBERS)) return Kind.BOBBER;
        if (stack.is(TideTags.Items.HOOKS)) return Kind.HOOK;
        if (stack.is(TideTags.Items.LURES)) return Kind.LURE;
        if (stack.is(TideTags.Items.BAIT_ITEMS)) return Kind.BAIT;
        if (GEAR.contains(key.getPath())) return Kind.GEAR;
        return null;
    }

    // ------------------------------------------------------------------ crafting at the table

    /** What the table's grid makes, and the recipe that makes it. */
    public record Match(Entry entry, ItemStack result, CraftingInput input) {}

    /** The table recipe the grid's items make, or null. Upgrades (smithing) take their three items in any arrangement. */
    public static Match match(CraftingInput input, net.minecraft.world.level.Level level) {
        if (input.isEmpty()) return null;
        for (Entry e : entries) {
            Recipe<?> r = e.holder.value();
            try {
                if (r instanceof CraftingRecipe c) {
                    if (c.matches(input, level)) {
                        ItemStack out = c.assemble(input);
                        if (!out.isEmpty()) return new Match(e, out, input);
                    }
                } else if (r instanceof SmithingTransformRecipe s) {
                    ItemStack out = smith(s, input);
                    if (!out.isEmpty()) return new Match(e, out, input);
                }
            } catch (RuntimeException ex) {
                Tide.LOG.debug("Tide: table recipe {} failed to match", e.id, ex);
            }
        }
        return null;
    }

    /** An upgrade in the grid: exactly its template, base and addition, in any order. */
    private static ItemStack smith(SmithingTransformRecipe s, CraftingInput input) {
        List<ItemStack> items = input.items().stream().filter(st -> !st.isEmpty()).toList();
        int need = 1 + (s.templateIngredient().isPresent() ? 1 : 0) + (s.additionIngredient().isPresent() ? 1 : 0);
        if (items.size() != need) return ItemStack.EMPTY;
        for (ItemStack st : items) if (st.getCount() < 1) return ItemStack.EMPTY;
        int[][] orders = need == 3 ? new int[][]{{0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0}} : need == 2 ? new int[][]{{0, 1}, {1, 0}} : new int[][]{{0}};
        for (int[] o : orders) {
            int k = 0;
            ItemStack t = s.templateIngredient().isPresent() ? items.get(o[k++]) : ItemStack.EMPTY;
            ItemStack b = items.get(o[k++]);
            ItemStack a = s.additionIngredient().isPresent() ? items.get(o[k]) : ItemStack.EMPTY;
            if (!Ingredient.testOptionalIngredient(s.templateIngredient(), t) || !s.baseIngredient().test(b)
                    || !Ingredient.testOptionalIngredient(s.additionIngredient(), a)) continue;
            ItemStack out = s.assemble(new SmithingRecipeInput(t.copyWithCount(1), b.copyWithCount(1), a.copyWithCount(1)));
            if (!out.isEmpty()) return out;
        }
        return ItemStack.EMPTY;
    }

    /** What's left in the grid after crafting (a bucket from a milk bucket, say), position by position. */
    public static List<ItemStack> remainders(Match m) {
        if (m.entry.holder.value() instanceof CraftingRecipe c) return c.getRemainingItems(m.input);
        List<ItemStack> none = new ArrayList<>();
        for (int i = 0; i < m.input.size(); i++) none.add(ItemStack.EMPTY);
        return none;
    }

    /** Inventory slots the recipe book may take from: the main inventory and the offhand (not armour). */
    private static int[] SLOTS = null;

    private static int[] slots() {
        if (SLOTS == null) {
            SLOTS = new int[37];
            for (int i = 0; i < 36; i++) SLOTS[i] = i;
            SLOTS[36] = 40;
        }
        return SLOTS;
    }

    /**
     * The recipe book's click: put a recipe's items into the grid from the inventory (as many sets as possible with
     * {@code all}, else one). Whatever was in the grid goes back to the inventory first. False if one set can't be made.
     */
    public static boolean fill(ServerPlayer player, Entry e, net.minecraft.world.Container grid, boolean all) {
        var inv = player.getInventory();
        for (int i = 0; i < grid.getContainerSize(); i++) {
            ItemStack st = grid.removeItemNoUpdate(i);
            if (!st.isEmpty()) inv.placeItemBackInInventory(st, net.minecraft.util.Prediction.SERVER_ONLY);
        }
        // pick an item for each ingredient: the one the player has most of, kept the same for every set
        Map<Item, Integer> have = new HashMap<>();
        for (int s : slots()) {
            ItemStack st = inv.getItem(s);
            if (!st.isEmpty()) have.merge(st.getItem(), st.getCount(), Integer::sum);
        }
        Map<Item, Integer> uses = new HashMap<>();
        Item[] pick = new Item[e.ingredients.size()];
        for (int i = 0; i < pick.length; i++) {
            Ingredient ing = e.ingredients.get(i);
            Item best = null;
            int bestLeft = 0;
            for (var entry : have.entrySet()) {
                int left = entry.getValue() - uses.getOrDefault(entry.getKey(), 0);
                if (left > bestLeft && ing.test(new ItemStack(entry.getKey()))) { best = entry.getKey(); bestLeft = left; }
            }
            if (best == null) return false;
            pick[i] = best;
            uses.merge(best, 1, Integer::sum);
        }
        int sets = 1;
        if (all) {
            sets = 64;
            for (var u : uses.entrySet()) sets = Math.min(sets, have.get(u.getKey()) / u.getValue());
            for (Item it : pick) sets = Math.min(sets, it.getDefaultMaxStackSize());
            sets = Math.min(sets, Math.max(1, e.result.getDefaultMaxStackSize() / Math.max(1, e.count)));
            sets = Math.max(1, sets);
        }
        for (int i = 0; i < pick.length; i++) {
            ItemStack taken = take(inv, pick[i], sets);
            grid.setItem(e.grid[i], taken);
        }
        return true;
    }

    /** Takes {@code n} of an item from the inventory (keeping one kind of stack, so an enchanted rod stays itself). */
    private static ItemStack take(net.minecraft.world.entity.player.Inventory inv, Item item, int n) {
        ItemStack out = ItemStack.EMPTY;
        for (int s : slots()) {
            ItemStack st = inv.getItem(s);
            if (st.isEmpty() || !st.is(item)) continue;
            if (!out.isEmpty() && !ItemStack.isSameItemSameComponents(out, st)) continue;
            int k = Math.min(n - out.getCount(), st.getCount());
            if (out.isEmpty()) out = st.split(k);
            else { st.shrink(k); out.grow(k); }
            if (out.getCount() >= n) break;
        }
        inv.setChanged();
        return out;
    }

    /** The list as sent to players: result, count, filter, smithing flag, and the item ids each ingredient accepts. */
    public static com.li64.tide.network.messages.TackleRecipesMsg message() {
        List<com.li64.tide.network.messages.TackleRecipesMsg.Row> rows = new ArrayList<>();
        for (Entry e : entries) {
            List<List<String>> ings = e.ingredients.stream()
                    .map(i -> i.items().map(h -> BuiltInRegistries.ITEM.getKey(h.value()).toString()).toList()).toList();
            rows.add(new com.li64.tide.network.messages.TackleRecipesMsg.Row(e.id.toString(),
                    BuiltInRegistries.ITEM.getKey(e.result).toString(), e.count, e.kind.ordinal(), e.smithing, ings,
                    java.util.Arrays.stream(e.grid).boxed().toList()));
        }
        return new com.li64.tide.network.messages.TackleRecipesMsg(rows);
    }

    public static void sync(ServerPlayer player) {
        Tide.NETWORK.sendToPlayer(message(), player);
    }
}
