package net.minecraft.advancements.predicates;

import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class ItemPredicate$Builder {
   private Optional<HolderSet<Item>> items = Optional.empty();
   private MinMaxBounds$Ints count = MinMaxBounds$Ints.ANY;
   private DataComponentMatchers components = DataComponentMatchers.ANY;

   public static ItemPredicate$Builder item() {
      return new ItemPredicate$Builder();
   }

   public ItemPredicate$Builder of(final HolderGetter<Item> lookup, final ItemLike... items) {
      this.items = Optional.of(HolderSet.direct(i -> i.asItem().builtInRegistryHolder(), items));
      return this;
   }

   public ItemPredicate$Builder of(final HolderGetter<Item> lookup, final TagKey<Item> tag) {
      this.items = Optional.of(lookup.getOrThrow(tag));
      return this;
   }

   public ItemPredicate$Builder withCount(final MinMaxBounds$Ints count) {
      this.count = count;
      return this;
   }

   public ItemPredicate$Builder withComponents(final DataComponentMatchers components) {
      this.components = components;
      return this;
   }

   public ItemPredicate build() {
      return new ItemPredicate(this.items, this.count, this.components);
   }
}
