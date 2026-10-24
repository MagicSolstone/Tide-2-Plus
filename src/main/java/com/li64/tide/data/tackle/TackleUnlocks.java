package com.li64.tide.data.tackle;

import com.li64.tide.Tide;
import com.li64.tide.network.messages.ShowToastMsg;
import com.li64.tide.network.messages.TackleUnlocksMsg;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;

/**
 * Which Angling Table recipes a player has discovered. Like vanilla's recipe book, a recipe shows up once you've
 * had its special ingredients: bamboo for the bamboo rod, redstone and a fishing line for the redstone line, a Tier 1
 * hook and a diamond for the diamond hook. Basic materials (sticks, string, planks) don't count, so string alone
 * doesn't open up every line. Having had the thing itself unlocks it too. Unlocking only changes what the book
 * shows: a recipe you know still works in the grid.
 */
public final class TackleUnlocks {
    private TackleUnlocks() {}

    private static final String TAG = "tide_table_recipes";

    private static boolean basic(Ingredient ing) {
        // sticks, string, and "any planks" (but cherry planks on their own are special: they make the cherry rod)
        if (ing.items().anyMatch(h -> h.value() == Items.STICK || h.value() == Items.STRING)) return true;
        return ing.items().filter(h -> h.value().builtInRegistryHolder().is(ItemTags.PLANKS)).limit(2).count() >= 2;
    }

    /** For each recipe: the ingredient groups to have had (any item of each). Empty groups list = any one ingredient will do. */
    private static List<List<Ingredient>> requirement(TackleRecipes.Entry e) {
        List<Ingredient> special = new ArrayList<>();
        for (Ingredient i : e.ingredients()) if (!basic(i) && !special.contains(i)) special.add(i);
        return special.isEmpty() ? List.of() : List.of(special);
    }

    private static boolean met(TackleRecipes.Entry e, Set<Item> seen) {
        if (seen.contains(e.result())) return true;
        List<List<Ingredient>> req = requirement(e);
        if (req.isEmpty()) {
            for (Ingredient i : e.ingredients()) if (seenAny(i, seen)) return true;
            return false;
        }
        for (Ingredient i : req.get(0)) if (!seenAny(i, seen)) return false;
        return true;
    }

    private static boolean seenAny(Ingredient i, Set<Item> seen) {
        return i.items().anyMatch(h -> seen.contains(h.value()));
    }

    // ------------------------------------------------------------------ saved per player

    private record State(Set<String> seen, Set<String> unlocked) {}

    private static State load(ServerPlayer player) {
        CompoundTag tag = Tide.PLATFORM.getPlayerData(player).getCompoundOrEmpty(TAG);
        return new State(split(tag.getStringOr("seen", "")), split(tag.getStringOr("unlocked", "")));
    }

    private static Set<String> split(String s) {
        return s.isEmpty() ? new LinkedHashSet<>() : new LinkedHashSet<>(Arrays.asList(s.split(",")));
    }

    private static void save(ServerPlayer player, State st) {
        CompoundTag tag = new CompoundTag();
        tag.putString("seen", String.join(",", st.seen));
        tag.putString("unlocked", String.join(",", st.unlocked));
        Tide.PLATFORM.getPlayerData(player).put(TAG, tag);
    }

    public static Set<String> unlocked(ServerPlayer player) {
        return load(player).unlocked;
    }

    /** Items worth remembering: anything a table recipe takes or makes. */
    private static Set<Item> relevant() {
        Set<Item> out = new HashSet<>();
        for (TackleRecipes.Entry e : TackleRecipes.entries()) {
            out.add(e.result());
            for (Ingredient i : e.ingredients()) i.items().forEach(h -> out.add(h.value()));
        }
        return out;
    }

    private static Set<Item> cachedRelevant = Set.of();
    private static List<TackleRecipes.Entry> cachedFor = List.of();

    private static Set<Item> relevantNow() {
        if (cachedFor != TackleRecipes.entries()) {
            cachedFor = TackleRecipes.entries();
            cachedRelevant = relevant();
        }
        return cachedRelevant;
    }

    /** Every couple of seconds: note new materials in the inventory and unlock what they open up. */
    public static void tick(ServerPlayer player) {
        if (player.isSpectator() || TackleRecipes.entries().isEmpty()) return;
        State st = load(player);
        Set<Item> relevant = relevantNow();
        boolean changed = false;
        var inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (!s.isEmpty() && relevant.contains(s.getItem()) && st.seen.add(BuiltInRegistries.ITEM.getKey(s.getItem()).toString())) changed = true;
        }
        for (String found : TackleLog.found(player)) {             // tackle already found counts as had
            Item it = BuiltInRegistries.ITEM.getOptional(Tide.resource(found)).orElse(null);
            if (it != null && st.seen.add(BuiltInRegistries.ITEM.getKey(it).toString())) changed = true;
        }
        if (!changed) return;
        Set<Item> seen = new HashSet<>();
        for (String id : st.seen) BuiltInRegistries.ITEM.getOptional(net.minecraft.resources.Identifier.parse(id)).ifPresent(seen::add);
        List<TackleRecipes.Entry> fresh = new ArrayList<>();
        for (TackleRecipes.Entry e : TackleRecipes.entries())
            if (!st.unlocked.contains(e.id().toString()) && met(e, seen)) {
                st.unlocked.add(e.id().toString());
                fresh.add(e);
            }
        save(player, st);
        if (fresh.isEmpty()) return;
        sync(player, st.unlocked);
        if (Tide.CI) Tide.LOG.info("TIDE-CI table unlocked {} for {}", fresh.stream().map(e -> e.id().getPath()).toList(), player.getScoreboardName());
        // a popup or two (a crowd of them on first join would bury the screen)
        boolean first = st.unlocked.size() == fresh.size();
        if (first && fresh.size() > 2) {
            Tide.NETWORK.sendToPlayer(new ShowToastMsg(Component.literal(fresh.size() + " fishing recipes"),
                    Component.literal("at the Angling Table"), new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("angling_table")))), player);
            return;
        }
        for (TackleRecipes.Entry e : fresh.subList(0, Math.min(2, fresh.size())))
            Tide.NETWORK.sendToPlayer(new ShowToastMsg(Component.literal("New fishing recipe"),
                    Component.literal(e.result().getDescriptionId()), new ItemStack(e.result())), player);
        if (fresh.size() > 2)
            Tide.NETWORK.sendToPlayer(new ShowToastMsg(Component.literal("+" + (fresh.size() - 2) + " more fishing recipes"),
                    Component.literal("at the Angling Table"), new ItemStack(BuiltInRegistries.ITEM.getValue(Tide.resource("angling_table")))), player);
    }

    public static void sync(ServerPlayer player) {
        sync(player, unlocked(player));
    }

    private static void sync(ServerPlayer player, Set<String> unlocked) {
        Tide.NETWORK.sendToPlayer(new TackleUnlocksMsg(new ArrayList<>(unlocked)), player);
    }

    /** Which recipes a set of had-items unlocks (for the CI check). */
    public static List<String> unlockedBy(Set<Item> seen) {
        List<String> out = new ArrayList<>();
        for (TackleRecipes.Entry e : TackleRecipes.entries()) if (met(e, seen)) out.add(e.id().getPath());
        return out;
    }

    /** What a recipe asks you to have had, for the CI log. */
    public static String needs(TackleRecipes.Entry e) {
        List<List<Ingredient>> req = requirement(e);
        if (req.isEmpty()) return "any ingredient";
        List<String> out = new ArrayList<>();
        for (Ingredient i : req.get(0)) out.add(i.items().findFirst().map(h -> BuiltInRegistries.ITEM.getKey(h.value()).getPath()).orElse("?")
                + (i.items().count() > 1 ? " (or " + (i.items().count() - 1) + " more)" : ""));
        return String.join(" + ", out);
    }

    /** For /fishing commands and tests: forget (or learn) every table recipe. */
    public static void reset(ServerPlayer player, boolean all) {
        State st = new State(new LinkedHashSet<>(), new LinkedHashSet<>());
        if (all) for (TackleRecipes.Entry e : TackleRecipes.entries()) st.unlocked.add(e.id().toString());
        save(player, st);
        sync(player, st.unlocked);
    }
}
