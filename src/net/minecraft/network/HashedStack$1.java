package net.minecraft.network;

import net.minecraft.world.item.ItemStack;

class HashedStack$1 implements HashedStack {
   @Override
   public String toString() {
      return "<empty>";
   }

   @Override
   public boolean matches(final ItemStack stack, final HashedPatchMap$HashGenerator hasher) {
      return stack.isEmpty();
   }
}
