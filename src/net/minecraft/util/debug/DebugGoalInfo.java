package net.minecraft.util.debug;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DebugGoalInfo(List<DebugGoalInfo$DebugGoal> goals) {
   public static final StreamCodec<ByteBuf, DebugGoalInfo> STREAM_CODEC = StreamCodec.composite(
      DebugGoalInfo$DebugGoal.STREAM_CODEC.apply(ByteBufCodecs.list()), DebugGoalInfo::goals, DebugGoalInfo::new
   );
}
