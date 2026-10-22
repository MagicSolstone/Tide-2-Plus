package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

class DispenseItemBehavior$3 extends DefaultDispenseItemBehavior {
   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      DispensibleContainerItem bucket = (DispensibleContainerItem)dispensed.getItem();
      BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
      Level level = source.level();
      if (bucket.emptyContents(null, level, target, null)) {
         bucket.checkExtraContent(null, level, dispensed, target);
         return this.consumeWithRemainder(source, dispensed, new ItemStack(Items.BUCKET));
      } else {
         return this.defaultDispenseItemBehavior.dispense(source, dispensed);
      }
   }
}
