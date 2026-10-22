package net.minecraft.world.item.slot;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

public record SlotSelector$TrackingSelector(SlotSelector slotSelector, int limit, MutableInt selectedCount) implements SlotSelector {
   @Override
   public boolean trySelectSlot(final ItemStack itemInSlot) {
      if (this.selectedCount.intValue() < this.limit && this.slotSelector.trySelectSlot(itemInSlot)) {
         this.selectedCount.increment();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public SlotSelector filter(final Predicate<? super ItemStack> predicate) {
      return new SlotSelector$TrackingSelector(this.slotSelector.filter(predicate), this.limit, this.selectedCount);
   }

   @Override
   public SlotSelector limit(final int limit) {
      return limit < this.limit ? new SlotSelector$TrackingSelector(this.slotSelector, limit, this.selectedCount) : this;
   }
}
