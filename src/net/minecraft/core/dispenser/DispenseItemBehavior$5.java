package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

class DispenseItemBehavior$5 extends OptionalDispenseItemBehavior {
   @Override
   protected ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      this.setSuccess(true);
      Level level = source.level();
      BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
      if (!BoneMealItem.growCrop(dispensed, level, target) && !BoneMealItem.growWaterPlant(dispensed, level, target, null)) {
         this.setSuccess(false);
      } else if (!level.isClientSide()) {
         level.levelEvent(1505, target, 15);
      }

      return dispensed;
   }
}
