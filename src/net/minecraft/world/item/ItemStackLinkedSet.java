package net.minecraft.world.item;

import it.unimi.dsi.fastutil.Hash.Strategy;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenCustomHashSet;
import java.util.Set;

public class ItemStackLinkedSet {
   private static final Strategy<? super ItemStack> TYPE_AND_TAG = new ItemStackLinkedSet$1();

   public static Set<ItemStack> createTypeAndComponentsSet() {
      return new ObjectLinkedOpenCustomHashSet(TYPE_AND_TAG);
   }
}
