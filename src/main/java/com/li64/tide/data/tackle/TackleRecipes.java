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
                        RecipeHolder<?> holder) {}

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
        if (r instanceof ShapedRecipe s) {
            ingredients = s.getIngredients().stream().filter(Optional::isPresent).map(Optional::get).toList();
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
        return new Entry(id, out.getItem(), out.getCount(), kind, smithing, List.copyOf(ingredients), h);
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

    /** Inventory slots the table may take from: the main inventory and the offhand (not armour). */
    private static List<Integer> slots(ServerPlayer player) {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < 36; i++) out.add(i);
        out.add(40);
        return out;
    }

    /**
     * Which inventory slot each ingredient comes from, or null if the player is short. {@code prefer}: for each
     * ingredient, an item to use first if the player has it (e.g. which Tier 1 hook a Tier 2 hook is made on).
     */
    public static int[] plan(ServerPlayer player, Entry e, List<Item> prefer) {
        var inv = player.getInventory();
        Map<Integer, Integer> left = new HashMap<>();
        for (int s : slots(player)) left.put(s, inv.getItem(s).getCount());
        int[] from = new int[e.ingredients.size()];
        for (int i = 0; i < from.length; i++) {
            Ingredient ing = e.ingredients.get(i);
            Item want = prefer != null && i < prefer.size() ? prefer.get(i) : null;
            int pick = -1;
            for (int pass = 0; pass < 2 && pick < 0; pass++)
                for (int s : slots(player)) {
                    ItemStack st = inv.getItem(s);
                    if (st.isEmpty() || left.get(s) <= 0 || !ing.test(st)) continue;
                    if (pass == 0 && want != null && !st.is(want)) continue;
                    if (pass == 0 && want == null) continue;
                    pick = s;
                    break;
                }
            if (pick < 0) return null;
            left.put(pick, left.get(pick) - 1);
            from[i] = pick;
        }
        return from;
    }

    /** Makes one; returns false (changing nothing) if the player is short of something. */
    public static boolean craftOnce(ServerPlayer player, Entry e, List<Item> prefer) {
        int[] from = plan(player, e, prefer);
        if (from == null) return false;
        var inv = player.getInventory();
        List<ItemStack> used = new ArrayList<>();
        for (int s : from) used.add(inv.getItem(s).copyWithCount(1));
        ItemStack result;
        List<ItemStack> remainders = new ArrayList<>();
        Recipe<?> r = e.holder.value();
        if (r instanceof SmithingTransformRecipe s) {
            int n = 0;
            ItemStack template = s.templateIngredient().isPresent() ? used.get(n++) : ItemStack.EMPTY;
            ItemStack base = used.get(n++);
            ItemStack addition = s.additionIngredient().isPresent() ? used.get(n) : ItemStack.EMPTY;
            result = s.assemble(new SmithingRecipeInput(template, base, addition));
        } else if (r instanceof ShapedRecipe s) {
            List<ItemStack> grid = new ArrayList<>();
            int k = 0;
            for (Optional<Ingredient> o : s.getIngredients()) grid.add(o.isPresent() ? used.get(k++) : ItemStack.EMPTY);
            CraftingInput input = CraftingInput.of(s.getWidth(), s.getHeight(), grid);
            result = s.assemble(input);
            remainders.addAll(s.getRemainingItems(input));
        } else if (r instanceof ShapelessRecipe s) {
            CraftingInput input = CraftingInput.of(used.size(), 1, used);
            result = s.assemble(input);
            remainders.addAll(s.getRemainingItems(input));
        } else return false;
        if (result.isEmpty()) return false;
        for (int s : from) inv.getItem(s).shrink(1);
        if (Tide.CI) Tide.LOG.info("TIDE-CI bench made {} from {} (base hook {})", BuiltInRegistries.ITEM.getKey(result.getItem()),
                used.stream().map(u -> BuiltInRegistries.ITEM.getKey(u.getItem()).getPath()).toList(), com.li64.tide.data.rods.RodEffects.baseHook(result));
        result.onCraftedBy(player, result.getCount());
        player.awardStat(net.minecraft.stats.Stats.ITEM_CRAFTED.get(result.getItem()), result.getCount());
        give(player, result);
        for (ItemStack rest : remainders) if (!rest.isEmpty()) give(player, rest);
        inv.setChanged();
        return true;
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack) && !stack.isEmpty()) player.drop(stack, false, net.minecraft.util.Prediction.SERVER_ONLY);
    }

    /** The list as sent to players: result, count, filter, smithing flag, and the item ids each ingredient accepts. */
    public static com.li64.tide.network.messages.TackleRecipesMsg message() {
        List<com.li64.tide.network.messages.TackleRecipesMsg.Row> rows = new ArrayList<>();
        for (Entry e : entries) {
            List<List<String>> ings = e.ingredients.stream()
                    .map(i -> i.items().map(h -> BuiltInRegistries.ITEM.getKey(h.value()).toString()).toList()).toList();
            rows.add(new com.li64.tide.network.messages.TackleRecipesMsg.Row(e.id.toString(),
                    BuiltInRegistries.ITEM.getKey(e.result).toString(), e.count, e.kind.ordinal(), e.smithing, ings));
        }
        return new com.li64.tide.network.messages.TackleRecipesMsg(rows);
    }

    public static void sync(ServerPlayer player) {
        Tide.NETWORK.sendToPlayer(message(), player);
    }
}
