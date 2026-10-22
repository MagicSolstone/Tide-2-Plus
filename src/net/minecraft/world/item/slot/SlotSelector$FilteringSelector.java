package net.minecraft.world.item.slot;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jspecify.annotations.Nullable;

public record SlotSelector$FilteringSelector(@Nullable Predicate<? super ItemStack> filter) implements SlotSelector {
   @Override
   public boolean trySelectSlot(final ItemStack itemInSlot) {
      return this.filter == null || this.filter.test(itemInSlot);
   }

   @Override
   public SlotSelector filter(final Predicate<? super ItemStack> predicate) {
      return this.filter == null
         ? new SlotSelector$FilteringSelector(predicate)
         : new SlotSelector$FilteringSelector(t -> this.filter.test(t) && predicate.test(t));
   }

   @Override
   public SlotSelector limit(final int limit) {
      return new SlotSelector$TrackingSelector(this, limit, new MutableInt());
   }
}
