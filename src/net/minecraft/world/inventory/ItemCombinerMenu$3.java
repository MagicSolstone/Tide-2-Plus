package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

class ItemCombinerMenu$3 extends Slot {
   ItemCombinerMenu$3(final ItemCombinerMenu this$0, final Container container, final int slot, final int x, final int y) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return false;
   }

   @Override
   public boolean mayPickup(final Player player) {
      return this.this$0.mayPickup(player, this.hasItem());
   }

   @Override
   public void onTake(final Player player, final ItemStack carried) {
      this.this$0.onTake(player, carried);
   }
}
