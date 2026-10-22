package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

class ComposterBlock$InputContainer extends SimpleContainer implements WorldlyContainer {
   private final BlockState state;
   private final LevelAccessor level;
   private final BlockPos pos;
   private boolean changed;

   public ComposterBlock$InputContainer(final BlockState state, final LevelAccessor level, final BlockPos pos) {
      super(1);
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
      return direction == Direction.UP ? new int[]{0} : new int[0];
   }

   @Override
   public boolean canPlaceItemThroughFace(final int slot, final ItemStack itemStack, @Nullable final Direction direction) {
      return !this.changed && direction == Direction.UP && itemStack.has(DataComponents.COMPOSTABLE);
   }

   @Override
   public boolean canTakeItemThroughFace(final int slot, final ItemStack itemStack, final Direction direction) {
      return false;
   }

   @Override
   public void setChanged() {
      ItemStack contents = this.getItem(0);
      Compostable compostable = contents.get(DataComponents.COMPOSTABLE);
      if (!contents.isEmpty() && compostable != null && this.level instanceof ServerLevel serverLevel) {
         this.changed = true;
         BlockState newState = ComposterBlock.addLayer(null, this.state, serverLevel, this.pos, compostable);
         this.level.levelEvent(1500, this.pos, newState != this.state ? 1 : 0);
         this.removeItemNoUpdate(0);
      }
   }
}
