package net.minecraft.world.level.block.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.level.block.state.properties.Property;

record StateDefinition$StateCollection<S extends StateHolder<?, ?>>(Map<List<Comparable<?>>, S> statesByValues, Map<List<Comparable<?>>, S[]> statesByPivotCache) {
   public S[][] fillNeighborsForState(final Property<?>[] propertyKeys, final List<Comparable<?>> propertyValues) {
      S[][] neighbors = (S[][])(new StateHolder[propertyKeys.length][]);
      List<Comparable<?>> valuesKey = new ArrayList<>(propertyValues);

      for (int i = 0; i < propertyKeys.length; i++) {
         neighbors[i] = this.fillStatesForPivot(valuesKey, propertyKeys[i], i);
      }

      return neighbors;
   }

   private <T extends Comparable<T>> S[] fillStatesForPivot(final List<Comparable<?>> valuesKey, final Property<T> pivot, final int pivotIndex) {
      Comparable<?> ownPivotValue = valuesKey.set(pivotIndex, StateDefinition$StateCollection$Wildcard.INSTANCE);

      S[] neighbourStatesForPivot;
      try {
         S[] cachedResult = (S[])((StateHolder[])this.statesByPivotCache.get(valuesKey));
         if (cachedResult == null) {
            neighbourStatesForPivot = this.computeStatesForPivot(valuesKey, pivot, pivotIndex);
            valuesKey.set(pivotIndex, StateDefinition$StateCollection$Wildcard.INSTANCE);
            this.statesByPivotCache.put(List.copyOf(valuesKey), neighbourStatesForPivot);
            return neighbourStatesForPivot;
         }

         neighbourStatesForPivot = cachedResult;
      } finally {
         valuesKey.set(pivotIndex, ownPivotValue);
      }

      return neighbourStatesForPivot;
   }

   private <T extends Comparable<T>> S[] computeStatesForPivot(final List<Comparable<?>> valuesKey, final Property<T> pivot, final int pivotIndex) {
      List<T> possiblePivotValues = pivot.getPossibleValues();
      int pivotValuesCount = possiblePivotValues.size();
      S[] result = (S[])(new StateHolder[pivotValuesCount]);

      for (int pivotValueIndex = 0; pivotValueIndex < pivotValuesCount; pivotValueIndex++) {
         T possiblePivotValue = (T)possiblePivotValues.get(pivotValueIndex);
         if (!$assertionsDisabled && pivot.getInternalIndex(possiblePivotValue) != pivotValueIndex) {
            throw new AssertionError();
         }

         valuesKey.set(pivotIndex, possiblePivotValue);
         S neighbourState = Objects.requireNonNull(this.statesByValues.get(valuesKey));
         result[pivotValueIndex] = neighbourState;
      }

      return result;
   }
}
