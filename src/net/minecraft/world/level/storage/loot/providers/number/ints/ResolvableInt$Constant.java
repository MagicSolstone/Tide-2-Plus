package net.minecraft.world.level.storage.loot.providers.number.ints;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.loot.LootContext;

public record ResolvableInt$Constant(int value) implements ResolvableInt {
   private static final Codec<ResolvableInt$Constant> CODEC = Codec.INT.xmap(ResolvableInt$Constant::new, ResolvableInt$Constant::value);
   private static final StreamCodec<ByteBuf, ResolvableInt$Constant> STREAM_CODEC = ByteBufCodecs.INT
      .map(ResolvableInt$Constant::new, ResolvableInt$Constant::value);

   @Override
   public int get(final LootContext context, final int defaultValue) {
      return this.value;
   }
}
