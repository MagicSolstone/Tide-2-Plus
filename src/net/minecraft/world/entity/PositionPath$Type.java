package net.minecraft.world.entity;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum PositionPath$Type {
   LINEAR(PositionPath$Linear.STREAM_CODEC),
   STEPPED(PositionPath$Stepped.STREAM_CODEC);

   public static final IntFunction<PositionPath$Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, PositionPath$Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
   private final StreamCodec<ByteBuf, ? extends PositionPath> streamCodec;

   private PositionPath$Type(final StreamCodec<ByteBuf, ? extends PositionPath> streamCodec) {
      this.streamCodec = streamCodec;
   }

   public StreamCodec<ByteBuf, ? extends PositionPath> streamCodec() {
      return this.streamCodec;
   }
}
