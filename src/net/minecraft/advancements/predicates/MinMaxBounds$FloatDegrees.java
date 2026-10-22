package net.minecraft.advancements.predicates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MinMaxBounds$FloatDegrees(MinMaxBounds$Bounds<Float> bounds) implements MinMaxBounds<Float> {
   public static final MinMaxBounds$FloatDegrees ANY = new MinMaxBounds$FloatDegrees(MinMaxBounds$Bounds.any());
   public static final Codec<MinMaxBounds$FloatDegrees> CODEC = MinMaxBounds$Bounds.createCodec(Codec.FLOAT)
      .xmap(MinMaxBounds$FloatDegrees::new, MinMaxBounds$FloatDegrees::bounds);
   public static final StreamCodec<ByteBuf, MinMaxBounds$FloatDegrees> STREAM_CODEC = MinMaxBounds$Bounds.createStreamCodec(ByteBufCodecs.FLOAT)
      .map(MinMaxBounds$FloatDegrees::new, MinMaxBounds$FloatDegrees::bounds);

   public static MinMaxBounds$FloatDegrees fromReader(final StringReader reader) throws CommandSyntaxException {
      MinMaxBounds$Bounds<Float> bounds = MinMaxBounds$Bounds.fromReader(
         reader, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat
      );
      return new MinMaxBounds$FloatDegrees(bounds);
   }
}
