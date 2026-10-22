package net.minecraft.util.debug;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public record DebugStructureInfo$Piece(BoundingBox boundingBox, boolean isStart) {
   public static final StreamCodec<ByteBuf, DebugStructureInfo$Piece> STREAM_CODEC = StreamCodec.composite(
      BoundingBox.STREAM_CODEC, DebugStructureInfo$Piece::boundingBox, ByteBufCodecs.BOOL, DebugStructureInfo$Piece::isStart, DebugStructureInfo$Piece::new
   );
}
