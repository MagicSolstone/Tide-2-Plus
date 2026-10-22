package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record TransportItemsBetweenContainers$TransportItemTarget(BlockPos pos, Container container, BlockEntity blockEntity, BlockState state) {
   @Nullable
   public static TransportItemsBetweenContainers$TransportItemTarget tryCreatePossibleTarget(final BlockEntity blockEntity, final Level level) {
      BlockPos blockPos = blockEntity.getBlockPos();
      BlockState blockState = blockEntity.getBlockState();
      Container container = getBlockEntityContainer(blockEntity, blockState, level, blockPos);
      return container != null ? new TransportItemsBetweenContainers$TransportItemTarget(blockPos, container, blockEntity, blockState) : null;
   }

   @Nullable
   public static TransportItemsBetweenContainers$TransportItemTarget tryCreatePossibleTarget(final BlockPos blockPos, final Level level) {
      BlockEntity blockEntity = level.getBlockEntity(blockPos);
      return blockEntity == null ? null : tryCreatePossibleTarget(blockEntity, level);
   }

   @Nullable
   private static Container getBlockEntityContainer(final BlockEntity blockEntity, final BlockState blockState, final Level level, final BlockPos blockPos) {
      if (blockState.getBlock() instanceof ChestBlock chestBlock) {
         return ChestBlock.getContainer(chestBlock, blockState, level, blockPos, false);
      } else {
         return blockEntity instanceof Container container ? container : null;
      }
   }
}
