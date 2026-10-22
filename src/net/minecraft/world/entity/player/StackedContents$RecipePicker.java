package net.minecraft.world.entity.player;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

class StackedContents$RecipePicker {
   private final List<? extends StackedContents$IngredientInfo<T>> ingredients;
   private final int ingredientCount;
   private final List<T> items;
   private final int itemCount;
   private final BitSet data;
   private final IntList path;

   public StackedContents$RecipePicker(final List<? extends StackedContents$IngredientInfo<T>> this$0, final List ingredients) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.path = new IntArrayList();
      this.ingredients = ingredients;
      this.ingredientCount = ingredients.size();
      this.items = this$0x.getUniqueAvailableIngredientItems(ingredients);
      this.itemCount = this.items.size();
      this.data = new BitSet(this.visitedIngredientCount() + this.visitedItemCount() + this.satisfiedCount() + this.connectionCount() + this.residualCount());
      this.setInitialConnections();
   }

   private void setInitialConnections() {
      for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
         StackedContents$IngredientInfo<T> ingredientInfo = (StackedContents$IngredientInfo<T>)this.ingredients.get(ingredient);

         for (int item = 0; item < this.itemCount; item++) {
            if (ingredientInfo.acceptsItem(this.items.get(item))) {
               this.setConnection(item, ingredient);
            }
         }
      }
   }

   public boolean tryPick(final int capacity, @Nullable final StackedContents$Output<T> output) {
      if (capacity <= 0) {
         return true;
      } else {
         int satisfiedIngredientCount = 0;

         while (true) {
            IntList path = this.tryAssigningNewItem(capacity);
            if (path == null) {
               boolean isValidAssignment = satisfiedIngredientCount == this.ingredientCount;
               boolean hasOutput = isValidAssignment && output != null;
               this.clearAllVisited();
               this.clearSatisfied();

               for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
                  for (int item = 0; item < this.itemCount; item++) {
                     if (this.isAssigned(item, ingredient)) {
                        this.unassign(item, ingredient);
                        this.this$0.put(this.items.get(item), capacity);
                        if (hasOutput) {
                           output.accept(this.items.get(item));
                        }
                        break;
                     }
                  }
               }

               if (!$assertionsDisabled && !this.data.get(this.residualOffset(), this.residualOffset() + this.residualCount()).isEmpty()) {
                  throw new AssertionError();
               }

               return isValidAssignment;
            }

            int assignedItem = path.getInt(0);
            this.this$0.take(this.items.get(assignedItem), capacity);
            int satisfiedIngredient = path.size() - 1;
            this.setSatisfied(path.getInt(satisfiedIngredient));
            satisfiedIngredientCount++;

            for (int i = 0; i < path.size() - 1; i++) {
               if (isPathIndexItem(i)) {
                  int itemx = path.getInt(i);
                  int ingredient = path.getInt(i + 1);
                  this.assign(itemx, ingredient);
               } else {
                  int itemx = path.getInt(i + 1);
                  int ingredient = path.getInt(i);
                  this.unassign(itemx, ingredient);
               }
            }
         }
      }
   }

   private static boolean isPathIndexItem(final int index) {
      return (index & 1) == 0;
   }

   @Nullable
   private IntList tryAssigningNewItem(final int capacity) {
      this.clearAllVisited();

      for (int item = 0; item < this.itemCount; item++) {
         if (this.this$0.hasAtLeast(this.items.get(item), capacity)) {
            IntList path = this.findNewItemAssignmentPath(item);
            if (path != null) {
               return path;
            }
         }
      }

      return null;
   }

   @Nullable
   private IntList findNewItemAssignmentPath(final int startingItem) {
      this.path.clear();
      this.visitItem(startingItem);
      this.path.add(startingItem);

      while (!this.path.isEmpty()) {
         int pathLength = this.path.size();
         if (isPathIndexItem(pathLength - 1)) {
            int itemToAssign = this.path.getInt(pathLength - 1);

            for (int ingredient = 0; ingredient < this.ingredientCount; ingredient++) {
               if (!this.hasVisitedIngredient(ingredient) && this.hasConnection(itemToAssign, ingredient) && !this.isAssigned(itemToAssign, ingredient)) {
                  this.visitIngredient(ingredient);
                  this.path.add(ingredient);
                  break;
               }
            }
         } else {
            int lastAssignedIngredient = this.path.getInt(pathLength - 1);
            if (!this.isSatisfied(lastAssignedIngredient)) {
               return this.path;
            }

            for (int item = 0; item < this.itemCount; item++) {
               if (!this.hasVisitedItem(item) && this.isAssigned(item, lastAssignedIngredient)) {
                  if (!$assertionsDisabled && !this.hasConnection(item, lastAssignedIngredient)) {
                     throw new AssertionError();
                  }

                  this.visitItem(item);
                  this.path.add(item);
                  break;
               }
            }
         }

         int newLength = this.path.size();
         if (newLength == pathLength) {
            this.path.removeInt(newLength - 1);
         }
      }

      return null;
   }

   private int visitedIngredientOffset() {
      return 0;
   }

   private int visitedIngredientCount() {
      return this.ingredientCount;
   }

   private int visitedItemOffset() {
      return this.visitedIngredientOffset() + this.visitedIngredientCount();
   }

   private int visitedItemCount() {
      return this.itemCount;
   }

   private int satisfiedOffset() {
      return this.visitedItemOffset() + this.visitedItemCount();
   }

   private int satisfiedCount() {
      return this.ingredientCount;
   }

   private int connectionOffset() {
      return this.satisfiedOffset() + this.satisfiedCount();
   }

   private int connectionCount() {
      return this.ingredientCount * this.itemCount;
   }

   private int residualOffset() {
      return this.connectionOffset() + this.connectionCount();
   }

   private int residualCount() {
      return this.ingredientCount * this.itemCount;
   }

   private boolean isSatisfied(final int ingredient) {
      return this.data.get(this.getSatisfiedIndex(ingredient));
   }

   private void setSatisfied(final int ingredient) {
      this.data.set(this.getSatisfiedIndex(ingredient));
   }

   private int getSatisfiedIndex(final int ingredient) {
      if ($assertionsDisabled || ingredient >= 0 && ingredient < this.ingredientCount) {
         return this.satisfiedOffset() + ingredient;
      } else {
         throw new AssertionError();
      }
   }

   private void clearSatisfied() {
      this.clearRange(this.satisfiedOffset(), this.satisfiedCount());
   }

   private void setConnection(final int item, final int ingredient) {
      this.data.set(this.getConnectionIndex(item, ingredient));
   }

   private boolean hasConnection(final int item, final int ingredient) {
      return this.data.get(this.getConnectionIndex(item, ingredient));
   }

   private int getConnectionIndex(final int item, final int ingredient) {
      if ($assertionsDisabled || item >= 0 && item < this.itemCount) {
         if ($assertionsDisabled || ingredient >= 0 && ingredient < this.ingredientCount) {
            return this.connectionOffset() + item * this.ingredientCount + ingredient;
         } else {
            throw new AssertionError();
         }
      } else {
         throw new AssertionError();
      }
   }

   private boolean isAssigned(final int item, final int ingredient) {
      return this.data.get(this.getResidualIndex(item, ingredient));
   }

   private void assign(final int item, final int ingredient) {
      int residualIndex = this.getResidualIndex(item, ingredient);
      if (!$assertionsDisabled && this.data.get(residualIndex)) {
         throw new AssertionError();
      } else {
         this.data.set(residualIndex);
      }
   }

   private void unassign(final int item, final int ingredient) {
      int residualIndex = this.getResidualIndex(item, ingredient);
      if (!$assertionsDisabled && !this.data.get(residualIndex)) {
         throw new AssertionError();
      } else {
         this.data.clear(residualIndex);
      }
   }

   private int getResidualIndex(final int item, final int ingredient) {
      if ($assertionsDisabled || item >= 0 && item < this.itemCount) {
         if ($assertionsDisabled || ingredient >= 0 && ingredient < this.ingredientCount) {
            return this.residualOffset() + item * this.ingredientCount + ingredient;
         } else {
            throw new AssertionError();
         }
      } else {
         throw new AssertionError();
      }
   }

   private void visitIngredient(final int item) {
      this.data.set(this.getVisitedIngredientIndex(item));
   }

   private boolean hasVisitedIngredient(final int ingredient) {
      return this.data.get(this.getVisitedIngredientIndex(ingredient));
   }

   private int getVisitedIngredientIndex(final int ingredient) {
      if ($assertionsDisabled || ingredient >= 0 && ingredient < this.ingredientCount) {
         return this.visitedIngredientOffset() + ingredient;
      } else {
         throw new AssertionError();
      }
   }

   private void visitItem(final int item) {
      this.data.set(this.getVisitiedItemIndex(item));
   }

   private boolean hasVisitedItem(final int item) {
      return this.data.get(this.getVisitiedItemIndex(item));
   }

   private int getVisitiedItemIndex(final int item) {
      if ($assertionsDisabled || item >= 0 && item < this.itemCount) {
         return this.visitedItemOffset() + item;
      } else {
         throw new AssertionError();
      }
   }

   private void clearAllVisited() {
      this.clearRange(this.visitedIngredientOffset(), this.visitedIngredientCount());
      this.clearRange(this.visitedItemOffset(), this.visitedItemCount());
   }

   private void clearRange(final int offset, final int count) {
      this.data.clear(offset, offset + count);
   }

   public int tryPickAll(final int maxSize, @Nullable final StackedContents$Output<T> output) {
      int min = 0;
      int max = Math.min(maxSize, this.this$0.getResultUpperBound(this.ingredients)) + 1;

      while (true) {
         int mid = (min + max) / 2;
         if (this.tryPick(mid, null)) {
            if (max - min <= 1) {
               if (mid > 0) {
                  this.tryPick(mid, output);
               }

               return mid;
            }

            min = mid;
         } else {
            max = mid;
         }
      }
   }
}
