package net.minecraft.world.level.storage.loot.providers.number.ints;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.AggregateProvider;

public record Minimum(HolderSet<ContextIntProvider> inputs) implements ContextIntProvider, AggregateProvider<ContextIntProvider> {
   public static final MapCodec<Minimum> MAP_CODEC = AggregateProvider.mapCodec(ContextIntProviders.LIST_CODEC, Minimum::new);

   @Override
   public MapCodec<Minimum> codec() {
      return MAP_CODEC;
   }

   @Override
   public int getIntUnsafe(final LootContext context) {
      int value = Integer.MAX_VALUE;

      for (Holder<ContextIntProvider> input : this.inputs()) {
         value = Math.min(value, input.value().getIntUnsafe(context));
      }

      return value;
   }
}
