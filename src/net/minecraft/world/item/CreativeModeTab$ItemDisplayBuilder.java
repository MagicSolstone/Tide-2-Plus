package net.minecraft.world.item;

import java.util.Collection;
import java.util.Set;
import net.minecraft.world.flag.FeatureFlagSet;

class CreativeModeTab$ItemDisplayBuilder implements CreativeModeTab$Output {
   public final Collection<ItemStack> tabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
   public final Set<ItemStack> searchTabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
   private final CreativeModeTab tab;
   private final FeatureFlagSet featureFlagSet;

   public CreativeModeTab$ItemDisplayBuilder(final CreativeModeTab tab, final FeatureFlagSet featureFlagSet) {
      this.tab = tab;
      this.featureFlagSet = featureFlagSet;
   }

   @Override
   public void accept(final ItemStack stack, final CreativeModeTab$TabVisibility tabVisibility) {
      if (stack.getCount() != 1) {
         throw new IllegalArgumentException("Stack size must be exactly 1");
      } else {
         boolean foundDuplicateStack = this.tabContents.contains(stack) && tabVisibility != CreativeModeTab$TabVisibility.SEARCH_TAB_ONLY;
         if (foundDuplicateStack) {
            throw new IllegalStateException(
               "Accidentally adding the same item stack twice "
                  + stack.getDisplayName().getString()
                  + " to a Creative Mode Tab: "
                  + this.tab.getDisplayName().getString()
            );
         } else {
            if (stack.getItem().isEnabled(this.featureFlagSet)) {
               switch (tabVisibility) {
                  case PARENT_AND_SEARCH_TABS:
                     this.tabContents.add(stack);
                     this.searchTabContents.add(stack);
                     break;
                  case PARENT_TAB_ONLY:
                     this.tabContents.add(stack);
                     break;
                  case SEARCH_TAB_ONLY:
                     this.searchTabContents.add(stack);
               }
            }
         }
      }
   }
}
