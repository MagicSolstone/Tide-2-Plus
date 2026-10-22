package net.minecraft.util.debug;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DebugGoalInfo$DebugGoal(int priority, boolean isRunning, String name) {
   public static final StreamCodec<ByteBuf, DebugGoalInfo$DebugGoal> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      DebugGoalInfo$DebugGoal::priority,
      ByteBufCodecs.BOOL,
      DebugGoalInfo$DebugGoal::isRunning,
      ByteBufCodecs.stringUtf8(255),
      DebugGoalInfo$DebugGoal::name,
      DebugGoalInfo$DebugGoal::new
   );
}
