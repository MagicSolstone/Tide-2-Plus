package net.minecraft.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class BlankFormat$1 implements NumberFormatType<BlankFormat> {
   private static final MapCodec<BlankFormat> CODEC = MapCodec.unit(BlankFormat.INSTANCE);
   private static final StreamCodec<RegistryFriendlyByteBuf, BlankFormat> STREAM_CODEC = StreamCodec.unit(BlankFormat.INSTANCE);

   @Override
   public MapCodec<BlankFormat> mapCodec() {
      return CODEC;
   }

   @Override
   public StreamCodec<RegistryFriendlyByteBuf, BlankFormat> streamCodec() {
      return STREAM_CODEC;
   }
}
