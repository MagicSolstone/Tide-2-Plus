package net.minecraft.world.item.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.serialization.DataResult;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.slot.SlotSelector;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;

public class BundleContents$Mutable extends GrowableMutableContainer<BundleContents> {
   private Fraction weight;
   private int selectedItem;
   private boolean needsFlattening;

   private BundleContents$Mutable(final List<ItemStack> items, final Fraction weight, final int selectedItem) {
      super(items);
      this.weight = weight;
      this.selectedItem = selectedItem;
   }

   public BundleContents$Mutable() {
      this(new ArrayList<>(), Fraction.ZERO, -1);
   }

   public BundleContents$Mutable clearItems() {
      this.items.clear();
      this.weight = Fraction.ZERO;
      this.selectedItem = -1;
      return this;
   }

   private int findStackIndexWithinRange(final ItemStack itemsToAdd, final int minInclusive, final int maxExclusive) {
      if (!itemsToAdd.isStackable()) {
         return -1;
      } else {
         int startIndex = Math.max(minInclusive, 0);
         int endIndex = Math.min(maxExclusive, this.items.size());

         for (int i = startIndex; i < endIndex; i++) {
            if (ItemStack.isSameItemSameComponents(this.items.get(i), itemsToAdd)) {
               return i;
            }
         }

         return -1;
      }
   }

   private int findStackIndex(final ItemStack itemsToAdd) {
      return this.findStackIndexWithinRange(itemsToAdd, 0, this.items.size());
   }

   private int getMaxAmountToAdd(final Fraction itemWeight) {
      Fraction remainingWeight = Fraction.ONE.subtract(this.weight);
      return Math.max(remainingWeight.divideBy(itemWeight).intValue(), 0);
   }

   public int tryInsert(final ItemStack itemsToAdd) {
      if (!BundleContents.canItemBeInBundle(itemsToAdd)) {
         return 0;
      } else {
         DataResult<Fraction> maybeItemWeight = BundleContents.getWeight(itemsToAdd);
         if (maybeItemWeight.isError()) {
            return 0;
         } else {
            Fraction itemWeight = (Fraction)maybeItemWeight.getOrThrow();
            int amountToAdd = Math.min(itemsToAdd.getCount(), this.getMaxAmountToAdd(itemWeight));
            if (amountToAdd == 0) {
               return 0;
            } else {
               this.weight = this.weight.add(getStackedWeight(itemWeight, amountToAdd));
               int stackIndex = this.findStackIndex(itemsToAdd);
               if (stackIndex != -1) {
                  ItemStack removedStack = this.items.remove(stackIndex);
                  ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
                  itemsToAdd.shrink(amountToAdd);
                  this.items.addFirst(mergedStack);
               } else {
                  this.items.addFirst(itemsToAdd.split(amountToAdd));
               }

               return amountToAdd;
            }
         }
      }
   }

   public int tryTransfer(final Slot slot, final Player player) {
      ItemStack other = slot.getItem();
      DataResult<Fraction> itemWeight = BundleContents.getWeight(other);
      if (itemWeight.isError()) {
         return 0;
      } else {
         int maxAmount = this.getMaxAmountToAdd((Fraction)itemWeight.getOrThrow());
         return BundleContents.canItemBeInBundle(other) ? this.tryInsert(slot.safeTake(other.getCount(), maxAmount, player)) : 0;
      }
   }

   public void toggleSelectedItem(final int selectedItem) {
      this.selectedItem = this.selectedItem != selectedItem && !this.indexIsOutsideAllowedBounds(selectedItem) ? selectedItem : -1;
   }

   private boolean indexIsOutsideAllowedBounds(final int selectedItem) {
      return selectedItem < 0 || selectedItem >= this.items.size();
   }

   @Nullable
   public ItemStack removeOne() {
      if (this.items.isEmpty()) {
         return null;
      } else {
         int removeIndex = this.indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
         ItemStack stack = this.items.remove(removeIndex).copy();
         this.weight = this.weight.subtract(getStackedWeight(stack));
         this.toggleSelectedItem(-1);
         return stack;
      }
   }

   private static Fraction getStackedWeight(final Fraction weight, final int count) {
      return weight.multiplyBy(Fraction.getFraction(count, 1));
   }

   private static Fraction getStackedWeight(final ItemStack stack) {
      return getStackedWeight((Fraction)BundleContents.getWeight(stack).getOrThrow(), stack.getCount());
   }

   public Fraction weight() {
      return this.weight;
   }

   @Override
   public int replaceSlotItems(final ItemProvider newItems, final SlotSelector slotSelector) {
      this.mergeIdenticalStacks();
      return super.replaceSlotItems(newItems, slotSelector);
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      this.mergeIdenticalStacks();
      super.modifySlots(consumer, slotSelector);
   }

   @Override
   protected boolean setItem(final int slot, final ItemStack itemStack) {
      ItemStack currentItem = this.items.get(slot);
      Fraction adjustedWeight = currentItem.isEmpty() ? this.weight : this.weight.subtract(getStackedWeight(currentItem));
      Fraction newWeight = itemStack.isEmpty() ? adjustedWeight : getWeightWithAddedItems(adjustedWeight, itemStack);
      if (newWeight != null && super.setItem(slot, itemStack)) {
         this.weight = newWeight;
         this.needsFlattening = true;
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected boolean addSlotWithItem(final ItemProvider newItems) {
      if (!newItems.findNextNonEmpty()) {
         return false;
      } else {
         Fraction newWeight = getWeightWithAddedItems(this.weight, newItems.peek());
         if (newWeight != null && super.addSlotWithItem(newItems)) {
            this.weight = newWeight;
            this.needsFlattening = true;
            return true;
         } else {
            return false;
         }
      }
   }

   @Nullable
   private static Fraction getWeightWithAddedItems(final Fraction weight, final ItemStack itemsToAdd) {
      if (BundleContents.canItemBeInBundle(itemsToAdd)) {
         DataResult<Fraction> itemWeight = BundleContents.getWeight(itemsToAdd);
         if (itemWeight.isError()) {
            return null;
         }

         Fraction newWeight = weight.add(getStackedWeight((Fraction)itemWeight.getOrThrow(), itemsToAdd.getCount()));
         if (newWeight.compareTo(Fraction.ONE) <= 0) {
            return newWeight;
         }
      }

      return null;
   }

   @Override
   public boolean canInsertNewSlots() {
      return this.weight.compareTo(Fraction.ONE) < 0;
   }

   private void mergeIdenticalStacks() {
      if (this.needsFlattening) {
         for (int index = 0; index < this.items.size(); index++) {
            ItemStack itemStack = this.items.get(index);
            if (itemStack.isEmpty()) {
               this.items.remove(index--);
            } else {
               int stackIndex = this.findStackIndexWithinRange(itemStack, index + 1, this.items.size());
               if (stackIndex != -1) {
                  ItemStack targetStack = this.items.get(stackIndex);
                  int mergedCount = itemStack.getCount() + targetStack.getCount();
                  this.items.set(stackIndex, itemStack.copyWithCount(mergedCount));
                  this.items.remove(index--);
               }
            }
         }

         this.needsFlattening = false;
      }
   }

   public BundleContents toImmutable() {
      this.mergeIdenticalStacks();
      Builder<ItemStackTemplate> builder = ImmutableList.builder();

      for (ItemStack item : this.items) {
         builder.add(ItemStackTemplate.fromNonEmptyStack(item));
      }

      return new BundleContents(builder.build(), this.selectedItem);
   }
}
