package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$User;

public class CalibratedSculkSensorBlockEntity extends SculkSensorBlockEntity {
   public CalibratedSculkSensorBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
      super(BlockEntityTypes.CALIBRATED_SCULK_SENSOR, worldPosition, blockState);
   }

   @Override
   public VibrationSystem$User createVibrationUser() {
      return new CalibratedSculkSensorBlockEntity$VibrationUser(this, this.getBlockPos());
   }
}
