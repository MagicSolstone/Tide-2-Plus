package net.minecraft.world.level.storage.loot.providers.number.ints;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.BinaryProvider;

public record FloorModulus(Holder<ContextIntProvider> left, Holder<ContextIntProvider> right) implements ContextIntProvider, BinaryProvider<ContextIntProvider> {
   public static final MapCodec<FloorModulus> MAP_CODEC = BinaryProvider.mapCodec(ContextIntProviders.CODEC, FloorModulus::new);

   @Override
   public MapCodec<FloorModulus> codec() {
      return MAP_CODEC;
   }

   @Override
   public int getIntUnsafe(final LootContext context) throws ArithmeticException {
      return Math.floorMod(this.left().value().getIntUnsafe(context), this.right().value().getIntUnsafe(context));
   }
}
