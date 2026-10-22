package net.minecraft.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

class FixedFormat$1 implements NumberFormatType<FixedFormat> {
   private static final MapCodec<FixedFormat> CODEC = ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
   private static final StreamCodec<RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = StreamCodec.composite(
      ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new
   );

   @Override
   public MapCodec<FixedFormat> mapCodec() {
      return CODEC;
   }

   @Override
   public StreamCodec<RegistryFriendlyByteBuf, FixedFormat> streamCodec() {
      return STREAM_CODEC;
   }
}
