package net.minecraft.world.entity.player;

import java.util.Objects;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;

class Player$1 implements SlotAccess {
   Player$1(final Player this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ItemStack get() {
      return this.this$0.containerMenu.getCarried();
   }

   @Override
   public boolean set(final ItemStack itemStack) {
      this.this$0.containerMenu.setCarried(itemStack);
      return true;
   }
}
