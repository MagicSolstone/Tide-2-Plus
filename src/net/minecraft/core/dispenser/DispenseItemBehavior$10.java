package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;

class DispenseItemBehavior$10 extends OptionalDispenseItemBehavior {
   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      Direction direction = source.state().getValue(DispenserBlock.FACING);
      BlockPos pos = source.pos().relative(direction);
      Level level = source.level();
      BlockState blockState = level.getBlockState(pos);
      this.setSuccess(true);
      if (blockState.is(Blocks.RESPAWN_ANCHOR)) {
         if (blockState.getValue(RespawnAnchorBlock.CHARGE) != 4) {
            RespawnAnchorBlock.charge(null, level, pos, blockState);
            dispensed.shrink(1);
         } else {
            this.setSuccess(false);
         }

         return dispensed;
      } else {
         return super.execute(source, dispensed);
      }
   }
}
