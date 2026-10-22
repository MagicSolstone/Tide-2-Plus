package net.minecraft.world.level.storage.loot.providers.number.floats;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.loot.LootContext;

public record ResolvableFloat$Constant(float value) implements ResolvableFloat {
   private static final Codec<ResolvableFloat$Constant> CODEC = Codec.FLOAT.xmap(ResolvableFloat$Constant::new, ResolvableFloat$Constant::value);
   private static final StreamCodec<ByteBuf, ResolvableFloat$Constant> STREAM_CODEC = ByteBufCodecs.FLOAT
      .map(ResolvableFloat$Constant::new, ResolvableFloat$Constant::value);

   @Override
   public float get(final LootContext context, final float defaultValue) {
      return this.value;
   }
}
