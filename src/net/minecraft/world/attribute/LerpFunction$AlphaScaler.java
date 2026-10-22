package net.minecraft.world.attribute;

@FunctionalInterface
public interface LerpFunction$AlphaScaler<T> {
   T apply(T item, float alpha);
}
