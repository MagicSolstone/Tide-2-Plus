package net.minecraft.world.entity.player;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap.Entry;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

public class StackedContents<T> {
   public final Reference2IntOpenHashMap<T> amounts = new Reference2IntOpenHashMap();

   private boolean hasAtLeast(final T item, final int count) {
      return this.amounts.getInt(item) >= count;
   }

   private void take(final T item, final int amount) {
      int previous = this.amounts.addTo(item, -amount);
      if (previous < amount) {
         throw new IllegalStateException("Took " + amount + " items, but only had " + previous);
      }
   }

   private void put(final T item, final int count) {
      this.amounts.addTo(item, count);
   }

   public boolean tryPick(
      final List<? extends StackedContents$IngredientInfo<T>> ingredients, final int amount, @Nullable final StackedContents$Output<T> output
   ) {
      return new StackedContents$RecipePicker(this, ingredients).tryPick(amount, output);
   }

   public int tryPickAll(
      final List<? extends StackedContents$IngredientInfo<T>> ingredients, final int maxSize, @Nullable final StackedContents$Output<T> output
   ) {
      return new StackedContents$RecipePicker(this, ingredients).tryPickAll(maxSize, output);
   }

   public void clear() {
      this.amounts.clear();
   }

   public void account(final T item, final int count) {
      this.put(item, count);
   }

   private List<T> getUniqueAvailableIngredientItems(final Iterable<? extends StackedContents$IngredientInfo<T>> ingredients) {
      List<T> result = new ArrayList<>();
      ObjectIterator var3 = Reference2IntMaps.fastIterable(this.amounts).iterator();

      while (var3.hasNext()) {
         Entry<T> availableItem = (Entry<T>)var3.next();
         if (availableItem.getIntValue() > 0 && anyIngredientMatches(ingredients, (T)availableItem.getKey())) {
            result.add((T)availableItem.getKey());
         }
      }

      return result;
   }

   private static <T> boolean anyIngredientMatches(final Iterable<? extends StackedContents$IngredientInfo<T>> ingredients, final T item) {
      for (StackedContents$IngredientInfo<T> ingredient : ingredients) {
         if (ingredient.acceptsItem(item)) {
            return true;
         }
      }

      return false;
   }

   @VisibleForTesting
   public int getResultUpperBound(final List<? extends StackedContents$IngredientInfo<T>> ingredients) {
      int min = Integer.MAX_VALUE;
      ObjectIterable<Entry<T>> availableItems = Reference2IntMaps.fastIterable(this.amounts);

      label31:
      for (StackedContents$IngredientInfo<T> ingredient : ingredients) {
         int max = 0;
         ObjectIterator var7 = availableItems.iterator();

         while (var7.hasNext()) {
            Entry<T> entry = (Entry<T>)var7.next();
            int itemCount = entry.getIntValue();
            if (itemCount > max) {
               if (ingredient.acceptsItem((T)entry.getKey())) {
                  max = itemCount;
               }

               if (max >= min) {
                  continue label31;
               }
            }
         }

         min = max;
         if (max == 0) {
            break;
         }
      }

      return min;
   }
}
