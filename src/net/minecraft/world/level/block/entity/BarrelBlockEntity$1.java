package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

class BarrelBlockEntity$1 extends ContainerOpenersCounter {
   BarrelBlockEntity$1(final BarrelBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   protected void onOpen(final Level level, final BlockPos pos, final BlockState state) {
      this.this$0.playSound(state, SoundEvents.BARREL_OPEN);
      this.this$0.updateBlockState(state, true);
   }

   @Override
   protected void onClose(final Level level, final BlockPos pos, final BlockState state) {
      this.this$0.playSound(state, SoundEvents.BARREL_CLOSE);
      this.this$0.updateBlockState(state, false);
   }

   @Override
   protected void openerCountChanged(final Level level, final BlockPos pos, final BlockState blockState, final int previous, final int current) {
   }

   @Override
   public boolean isOwnContainer(final Player player) {
      if (player.containerMenu instanceof ChestMenu) {
         Container container = ((ChestMenu)player.containerMenu).getContainer();
         return container == this.this$0;
      } else {
         return false;
      }
   }
}
