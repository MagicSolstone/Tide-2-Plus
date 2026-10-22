package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem$User;
import org.jspecify.annotations.Nullable;

public class SculkSensorBlockEntity$VibrationUser implements VibrationSystem$User {
   public static final int LISTENER_RANGE = 8;
   protected final BlockPos blockPos;
   private final PositionSource positionSource;

   public SculkSensorBlockEntity$VibrationUser(final SculkSensorBlockEntity this$0, final BlockPos blockPos) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.blockPos = blockPos;
      this.positionSource = new BlockPositionSource(blockPos);
   }

   @Override
   public int getListenerRadius() {
      return 8;
   }

   @Override
   public PositionSource getPositionSource() {
      return this.positionSource;
   }

   @Override
   public boolean canTriggerAvoidVibration() {
      return true;
   }

   @Override
   public boolean canReceiveVibration(final ServerLevel level, final BlockPos pos, final Holder<GameEvent> event, @Nullable final GameEvent$Context context) {
      if (!pos.equals(this.blockPos) || !event.is(GameEvent.BLOCK_DESTROY) && !event.is(GameEvent.BLOCK_PLACE)) {
         return VibrationSystem.getGameEventFrequency(event) == 0 ? false : SculkSensorBlock.canActivate(this.this$0.getBlockState());
      } else {
         return false;
      }
   }

   @Override
   public void onReceiveVibration(
      final ServerLevel level,
      final BlockPos pos,
      final Holder<GameEvent> event,
      @Nullable final Entity sourceEntity,
      @Nullable final Entity projectileOwner,
      final float receivingDistance
   ) {
      BlockState state = this.this$0.getBlockState();
      if (SculkSensorBlock.canActivate(state)) {
         int eventFrequency = VibrationSystem.getGameEventFrequency(event);
         this.this$0.setLastVibrationFrequency(eventFrequency);
         int calculatedPower = VibrationSystem.getRedstoneStrengthForDistance(receivingDistance, this.getListenerRadius());
         if (state.getBlock() instanceof SculkSensorBlock sculkSensorBlock) {
            sculkSensorBlock.activate(sourceEntity, level, this.blockPos, state, calculatedPower, eventFrequency);
         }
      }
   }

   @Override
   public void onDataChanged() {
      this.this$0.setChanged();
   }

   @Override
   public boolean requiresAdjacentChunksToBeTicking() {
      return true;
   }
}
