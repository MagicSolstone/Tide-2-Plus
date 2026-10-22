package net.minecraft.client.multiplayer.prediction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
class BlockStatePredictionHandler$ServerVerifiedState {
   private final Vec3 playerPos;
   private int sequence;
   private BlockState blockState;

   private BlockStatePredictionHandler$ServerVerifiedState(final int sequence, final BlockState blockState, final Vec3 playerPos) {
      this.sequence = sequence;
      this.blockState = blockState;
      this.playerPos = playerPos;
   }

   private BlockStatePredictionHandler$ServerVerifiedState setSequence(final int sequence) {
      this.sequence = sequence;
      return this;
   }

   private void setBlockState(final BlockState blockState) {
      this.blockState = blockState;
   }
}
