package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jspecify.annotations.Nullable;

public class CalibratedSculkSensorBlockEntity$VibrationUser extends SculkSensorBlockEntity$VibrationUser {
   public CalibratedSculkSensorBlockEntity$VibrationUser(final CalibratedSculkSensorBlockEntity this$0, final BlockPos blockPos) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0, blockPos);
   }

   @Override
   public int getListenerRadius() {
      return 16;
   }

   @Override
   public boolean canReceiveVibration(final ServerLevel level, final BlockPos pos, final Holder<GameEvent> event, @Nullable final GameEvent$Context context) {
      int comparisonType = this.getBackSignal(level, this.blockPos, this.this$0.getBlockState());
      return comparisonType != 0 && VibrationSystem.getGameEventFrequency(event) != comparisonType
         ? false
         : super.canReceiveVibration(level, pos, event, context);
   }

   private int getBackSignal(final Level level, final BlockPos pos, final BlockState state) {
      Direction direction = state.getValue(CalibratedSculkSensorBlock.FACING).getOpposite();
      return level.getSignal(pos.relative(direction), direction);
   }
}
