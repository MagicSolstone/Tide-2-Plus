package net.minecraft.util.debug;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public record DebugStructureInfo(BoundingBox boundingBox, List<DebugStructureInfo$Piece> pieces) {
   public static final StreamCodec<ByteBuf, DebugStructureInfo> STREAM_CODEC = StreamCodec.composite(
      BoundingBox.STREAM_CODEC,
      DebugStructureInfo::boundingBox,
      DebugStructureInfo$Piece.STREAM_CODEC.apply(ByteBufCodecs.list()),
      DebugStructureInfo::pieces,
      DebugStructureInfo::new
   );
}
