package net.minecraft.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Style$Serializer;
import net.minecraft.network.codec.StreamCodec;

class StyledFormat$1 implements NumberFormatType<StyledFormat> {
   private static final MapCodec<StyledFormat> CODEC = Style$Serializer.MAP_CODEC.xmap(StyledFormat::new, StyledFormat::style);
   private static final StreamCodec<RegistryFriendlyByteBuf, StyledFormat> STREAM_CODEC = StreamCodec.composite(
      Style$Serializer.TRUSTED_STREAM_CODEC, StyledFormat::style, StyledFormat::new
   );

   @Override
   public MapCodec<StyledFormat> mapCodec() {
      return CODEC;
   }

   @Override
   public StreamCodec<RegistryFriendlyByteBuf, StyledFormat> streamCodec() {
      return STREAM_CODEC;
   }
}
