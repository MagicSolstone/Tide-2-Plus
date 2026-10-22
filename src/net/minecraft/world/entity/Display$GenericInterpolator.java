package net.minecraft.world.entity;

@FunctionalInterface
public interface Display$GenericInterpolator<T> {
   static <T> Display$GenericInterpolator<T> constant(final T value) {
      return progress -> value;
   }

   T get(final float progress);
}
