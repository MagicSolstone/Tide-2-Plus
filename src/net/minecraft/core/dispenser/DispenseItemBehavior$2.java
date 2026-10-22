package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

class DispenseItemBehavior$2 extends OptionalDispenseItemBehavior {
   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

      for (AbstractChestedHorse abstractChestedHorse : source.level()
         .getEntitiesOfClass(AbstractChestedHorse.class, new AABB(pos), entity -> entity.isAlive() && !entity.hasChest())) {
         if (abstractChestedHorse.isTamed()) {
            SlotAccess slot = abstractChestedHorse.getSlot(499);
            if (slot != null && slot.set(dispensed)) {
               dispensed.shrink(1);
               this.setSuccess(true);
               return dispensed;
            }
         }
      }

      return super.execute(source, dispensed);
   }
}
