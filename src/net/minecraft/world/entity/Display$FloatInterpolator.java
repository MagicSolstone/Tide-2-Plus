package net.minecraft.world.entity;

@FunctionalInterface
public interface Display$FloatInterpolator {
   static Display$FloatInterpolator constant(final float value) {
      return progress -> value;
   }

   float get(final float progress);
}
