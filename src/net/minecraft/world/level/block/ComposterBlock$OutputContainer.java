package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

class ComposterBlock$OutputContainer extends SimpleContainer implements WorldlyContainer {
   private final BlockState state;
   private final LevelAccessor level;
   private final BlockPos pos;
   private boolean changed;

   public ComposterBlock$OutputContainer(final BlockState state, final LevelAccessor level, final BlockPos pos, final ItemStack contents) {
      super(contents);
      this.state = state;
      this.level = level;
      this.pos = pos;
   }

   @Override
   public int getMaxStackSize() {
      return 1;
   }

   @Override
   public int[] getSlotsForFace(final Direction direction) {
      return direction == Direction.DOWN ? new int[]{0} : new int[0];
   }

   @Override
   public boolean canPlaceItemThroughFace(final int slot, final ItemStack itemStack, @Nullable final Direction direction) {
      return false;
   }

   @Override
   public boolean canTakeItemThroughFace(final int slot, final ItemStack itemStack, final Direction direction) {
      return !this.changed && direction == Direction.DOWN && itemStack.is(Items.BONE_MEAL);
   }

   @Override
   public void setChanged() {
      ComposterBlock.empty(null, this.state, this.level, this.pos);
      this.changed = true;
   }
}
