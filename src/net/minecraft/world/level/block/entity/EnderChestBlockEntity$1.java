package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

class EnderChestBlockEntity$1 extends ContainerOpenersCounter {
   EnderChestBlockEntity$1(final EnderChestBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   protected void onOpen(final Level level, final BlockPos pos, final BlockState blockState) {
      level.playSound(
         null,
         pos.getX() + 0.5,
         pos.getY() + 0.5,
         pos.getZ() + 0.5,
         SoundEvents.ENDER_CHEST_OPEN,
         SoundSource.BLOCKS,
         0.5F,
         level.getRandom().nextFloat() * 0.1F + 0.9F
      );
   }

   @Override
   protected void onClose(final Level level, final BlockPos pos, final BlockState blockState) {
      level.playSound(
         null,
         pos.getX() + 0.5,
         pos.getY() + 0.5,
         pos.getZ() + 0.5,
         SoundEvents.ENDER_CHEST_CLOSE,
         SoundSource.BLOCKS,
         0.5F,
         level.getRandom().nextFloat() * 0.1F + 0.9F
      );
   }

   @Override
   protected void openerCountChanged(final Level level, final BlockPos pos, final BlockState blockState, final int previous, final int current) {
      level.blockEvent(this.this$0.worldPosition, Blocks.ENDER_CHEST, 1, current);
   }

   @Override
   public boolean isOwnContainer(final Player player) {
      return player.getEnderChestInventory().isActiveChest(this.this$0);
   }
}
