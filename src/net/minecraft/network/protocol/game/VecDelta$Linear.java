package net.minecraft.network.protocol.game;

import net.minecraft.world.entity.PositionPath;
import net.minecraft.world.phys.Vec3;

public record VecDelta$Linear(short xa, short ya, short za) implements VecDelta {
   @Override
   public int stepCount() {
      return 0;
   }

   @Override
   public boolean hasDeltaX() {
      return this.xa != 0;
   }

   @Override
   public boolean hasDeltaZ() {
      return this.za != 0;
   }

   @Override
   public PositionPath decode(final VecDeltaCodec positionCodec) {
      Vec3 pos = positionCodec.decode(this.xa, this.ya, this.za);
      return PositionPath.of(pos);
   }
}
