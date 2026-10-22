package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

class ComposterBlock$EmptyContainer extends SimpleContainer implements WorldlyContainer {
   public ComposterBlock$EmptyContainer() {
      super(0);
   }

   @Override
   public int[] getSlotsForFace(final Direction direction) {
      return new int[0];
   }

   @Override
   public boolean canPlaceItemThroughFace(final int slot, final ItemStack itemStack, @Nullable final Direction direction) {
      return false;
   }

   @Override
   public boolean canTakeItemThroughFace(final int slot, final ItemStack itemStack, final Direction direction) {
      return false;
   }
}
