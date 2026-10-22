package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;

public record ItemPredicate(Optional<HolderSet<Item>> items, MinMaxBounds$Ints count, DataComponentMatchers components) implements Predicate<ItemInstance> {
   public static final Codec<ItemPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.ITEM).optionalFieldOf("items").forGetter(ItemPredicate::items),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("count", MinMaxBounds$Ints.ANY).forGetter(ItemPredicate::count),
            DataComponentMatchers.CODEC.forGetter(ItemPredicate::components)
         )
         .apply(i, ItemPredicate::new)
   );

   public boolean test(final ItemInstance itemStack) {
      if (this.items.isPresent() && !itemStack.is(this.items.get())) {
         return false;
      } else {
         return !this.count.matches(itemStack.count()) ? false : this.components.test((DataComponentGetter)itemStack);
      }
   }
}
