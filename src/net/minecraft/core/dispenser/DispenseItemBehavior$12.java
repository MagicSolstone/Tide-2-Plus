package net.minecraft.core.dispenser;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

class DispenseItemBehavior$12 extends OptionalDispenseItemBehavior {
   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
      Level level = source.level();
      BlockState blockState = level.getBlockState(pos);
      Optional<BlockState> maybeWaxed = HoneycombItem.getWaxed(blockState);
      if (maybeWaxed.isPresent()) {
         level.setBlockAndUpdate(pos, maybeWaxed.get());
         level.levelEvent(3003, pos, 0);
         level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
         dispensed.shrink(1);
         this.setSuccess(true);
         return dispensed;
      } else {
         return super.execute(source, dispensed);
      }
   }
}
