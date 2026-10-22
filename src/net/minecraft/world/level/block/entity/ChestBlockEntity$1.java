package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;

class ChestBlockEntity$1 extends ContainerOpenersCounter {
   ChestBlockEntity$1(final ChestBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   protected void onOpen(final Level level, final BlockPos pos, final BlockState blockState) {
      if (blockState.getBlock() instanceof ChestBlock chestBlock) {
         ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getOpenChestSound());
      }
   }

   @Override
   protected void onClose(final Level level, final BlockPos pos, final BlockState blockState) {
      if (blockState.getBlock() instanceof ChestBlock chestBlock) {
         ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getCloseChestSound());
      }
   }

   @Override
   protected void openerCountChanged(final Level level, final BlockPos pos, final BlockState blockState, final int previous, final int current) {
      this.this$0.signalOpenCount(level, pos, blockState, previous, current);
   }

   @Override
   public boolean isOwnContainer(final Player player) {
      if (!(player.containerMenu instanceof ChestMenu)) {
         return false;
      } else {
         Container container = ((ChestMenu)player.containerMenu).getContainer();
         return container == this.this$0 || container instanceof CompoundContainer compoundContainer && compoundContainer.contains(this.this$0);
      }
   }
}
