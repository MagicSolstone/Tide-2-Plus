package net.minecraft.util;

@FunctionalInterface
public interface StaticCache2D$MappingFunction<T, U> {
   U apply(T value, int x, int z);
}
