package net.minecraft.world.entity;

@FunctionalInterface
public interface Display$IntInterpolator {
   static Display$IntInterpolator constant(final int value) {
      return progress -> value;
   }

   int get(final float progress);
}
