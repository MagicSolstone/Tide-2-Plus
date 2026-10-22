package net.minecraft.world.level.storage.loot.providers.number.floats;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.PowerProvider;

public record Power(Holder<ContextFloatProvider> base, Holder<ContextFloatProvider> exponent)
   implements ContextFloatProvider,
   PowerProvider<ContextFloatProvider> {
   public static final MapCodec<Power> MAP_CODEC = PowerProvider.mapCodec(ContextFloatProviders.CODEC, Power::new);

   @Override
   public MapCodec<Power> codec() {
      return MAP_CODEC;
   }

   @Override
   public float getFloatUnsafe(final LootContext context) {
      float base = this.base.value().getFloatUnsafe(context);
      float exponent = this.exponent.value().getFloatUnsafe(context);
      return base == 0.0F && exponent == 0.0F ? Float.NaN : (float)Math.pow(base, exponent);
   }
}
