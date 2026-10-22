package net.minecraft.recipebook;

@FunctionalInterface
public interface PlaceRecipeHelper$Output<T> {
   void addItemToSlot(T item, int gridIndex, int gridXPos, int gridYPos);
}
