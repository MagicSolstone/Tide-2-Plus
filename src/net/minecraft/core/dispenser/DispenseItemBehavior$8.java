package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

class DispenseItemBehavior$8 extends OptionalDispenseItemBehavior {
   @Override
   protected ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      Level level = source.level();
      BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
      CarvedPumpkinBlock pumpkinBlock = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
      if (level.isEmptyBlock(target) && pumpkinBlock.canSpawnGolem(level, target)) {
         if (!level.isClientSide()) {
            level.setBlockAndUpdate(target, pumpkinBlock.defaultBlockState());
            level.gameEvent(null, GameEvent.BLOCK_PLACE, target);
         }

         dispensed.shrink(1);
         this.setSuccess(true);
      } else {
         this.setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
      }

      return dispensed;
   }
}
