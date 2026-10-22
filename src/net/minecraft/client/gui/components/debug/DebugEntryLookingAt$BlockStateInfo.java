package net.minecraft.client.gui.components.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

@Environment(EnvType.CLIENT)
public class DebugEntryLookingAt$BlockStateInfo extends DebugEntryLookingAt$DebugEntryLookingAtState<Block, BlockState> {
   protected DebugEntryLookingAt$BlockStateInfo() {
      super("Targeted Block");
   }

   @Override
   public HitResult getHitResult(final Entity cameraEntity) {
      return cameraEntity.pick(20.0, 0.0F, false);
   }

   public BlockState getInstance(final Level level, final BlockPos pos) {
      return level.getBlockState(pos);
   }

   @Override
   public Identifier group() {
      return DebugEntryLookingAt.BLOCK_GROUP;
   }
}
