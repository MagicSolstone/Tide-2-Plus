package net.minecraft.world.entity.player;

@FunctionalInterface
public interface StackedContents$IngredientInfo<T> {
   boolean acceptsItem(T item);
}
