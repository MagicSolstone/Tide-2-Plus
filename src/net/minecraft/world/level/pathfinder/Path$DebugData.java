package net.minecraft.world.level.pathfinder;

import io.netty.buffer.ByteBuf;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Path$DebugData(List<Node> openSet, List<Node> closedSet, Set<Target> targetNodes) {
   public static final StreamCodec<ByteBuf, Path$DebugData> STREAM_CODEC = StreamCodec.composite(
      Node.DEBUG_STREAM_CODEC.apply(ByteBufCodecs.list()),
      Path$DebugData::openSet,
      Node.DEBUG_STREAM_CODEC.apply(ByteBufCodecs.list()),
      Path$DebugData::closedSet,
      Target.DEBUG_STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)),
      Path$DebugData::targetNodes,
      Path$DebugData::new
   );
}
