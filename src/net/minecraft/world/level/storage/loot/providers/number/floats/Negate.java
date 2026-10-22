package net.minecraft.world.level.storage.loot.providers.number.floats;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.UnaryProvider;

public record Negate(Holder<ContextFloatProvider> input) implements ContextFloatProvider, UnaryProvider<ContextFloatProvider> {
   public static final MapCodec<Negate> MAP_CODEC = UnaryProvider.codec(ContextFloatProviders.CODEC, Negate::new);

   @Override
   public MapCodec<Negate> codec() {
      return MAP_CODEC;
   }

   @Override
   public float getFloatUnsafe(final LootContext context) {
      return -this.input().value().getFloatUnsafe(context);
   }
}
