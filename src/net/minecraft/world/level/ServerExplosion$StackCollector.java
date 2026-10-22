package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

class ServerExplosion$StackCollector {
   private final BlockPos pos;
   private ItemStack stack;

   private ServerExplosion$StackCollector(final BlockPos pos, final ItemStack stack) {
      this.pos = pos;
      this.stack = stack;
   }

   public void tryMerge(final ItemStack input) {
      if (ItemEntity.areMergable(this.stack, input)) {
         this.stack = ItemEntity.merge(this.stack, input, 16);
      }
   }
}
