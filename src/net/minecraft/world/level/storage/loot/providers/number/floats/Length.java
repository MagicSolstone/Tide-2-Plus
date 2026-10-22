package net.minecraft.world.level.storage.loot.providers.number.floats;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.AggregateProvider;

public record Length(HolderSet<ContextFloatProvider> inputs) implements ContextFloatProvider, AggregateProvider<ContextFloatProvider> {
   public static final MapCodec<Length> MAP_CODEC = AggregateProvider.mapCodec(ContextFloatProviders.LIST_CODEC, Length::new);

   @Override
   public MapCodec<Length> codec() {
      return MAP_CODEC;
   }

   @Override
   public float getFloatUnsafe(final LootContext context) {
      float sumOfSquares = 0.0F;

      for (Holder<ContextFloatProvider> input : this.inputs()) {
         float value = input.value().getFloatUnsafe(context);
         sumOfSquares += value * value;
      }

      return Mth.sqrt(sumOfSquares);
   }
}
