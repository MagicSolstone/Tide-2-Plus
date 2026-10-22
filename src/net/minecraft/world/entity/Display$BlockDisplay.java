package net.minecraft.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Display$BlockDisplay extends Display {
   public static final String TAG_BLOCK_STATE = "block_state";
   private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(
      Display$BlockDisplay.class, EntityDataSerializers.BLOCK_STATE
   );
   @Nullable
   private Display$BlockDisplay$BlockRenderState blockRenderState;

   public Display$BlockDisplay(final EntityType<?> type, final Level level) {
      super(type, level);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_BLOCK_STATE_ID, Blocks.AIR.defaultBlockState());
   }

   @Override
   public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
      super.onSyncedDataUpdated(accessor);
      if (accessor.equals(DATA_BLOCK_STATE_ID)) {
         this.updateRenderState = true;
      }
   }

   private BlockState getBlockState() {
      return this.entityData.get(DATA_BLOCK_STATE_ID);
   }

   private void setBlockState(final BlockState blockState) {
      this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setBlockState(input.<BlockState>read("block_state", BlockState.CODEC).orElse(Blocks.AIR.defaultBlockState()));
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      output.store("block_state", BlockState.CODEC, this.getBlockState());
   }

   @Nullable
   public Display$BlockDisplay$BlockRenderState blockRenderState() {
      return this.blockRenderState;
   }

   @Override
   protected void updateRenderSubState(final boolean shouldInterpolate, final float progress) {
      this.blockRenderState = new Display$BlockDisplay$BlockRenderState(this.getBlockState());
   }
}
