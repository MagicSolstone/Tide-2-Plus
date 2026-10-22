package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

class DispenseItemBehavior$4 extends DefaultDispenseItemBehavior {
   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      LevelAccessor level = source.level();
      BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
      BlockState blockState = level.getBlockState(target);
      if (blockState.getBlock() instanceof BucketPickup bucket) {
         ItemStack pickup = bucket.pickupBlock(null, level, target, blockState);
         if (pickup.isEmpty()) {
            return super.execute(source, dispensed);
         } else {
            level.gameEvent(null, GameEvent.FLUID_PICKUP, target);
            Item targetType = pickup.getItem();
            return this.consumeWithRemainder(source, dispensed, new ItemStack(targetType));
         }
      } else {
         return super.execute(source, dispensed);
      }
   }
}
