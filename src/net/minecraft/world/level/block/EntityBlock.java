package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.GameEventListener$Provider;
import org.jspecify.annotations.Nullable;

public interface EntityBlock {
   @Nullable
   BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState);

   @Nullable
   default <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level, final BlockState blockState, final BlockEntityType<T> type) {
      return null;
   }

   @Nullable
   default <T extends BlockEntity> GameEventListener getListener(final ServerLevel level, final T blockEntity) {
      return blockEntity instanceof GameEventListener$Provider<?> provider ? provider.getListener() : null;
   }
}
