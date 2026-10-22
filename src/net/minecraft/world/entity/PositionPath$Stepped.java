package net.minecraft.world.entity;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record PositionPath$Stepped(Vec3 endPosition, List<PositionStep> steps) implements PositionPath {
   public static final StreamCodec<ByteBuf, PositionPath$Stepped> STREAM_CODEC = PositionStep.STREAM_CODEC
      .apply(ByteBufCodecs.list())
      .map(PositionPath$Stepped::new, PositionPath$Stepped::steps);

   public PositionPath$Stepped(final List<PositionStep> steps) {
      this(steps.getLast().position(), steps);
   }

   @Override
   public PositionPath$Type type() {
      return PositionPath$Type.STEPPED;
   }
}
