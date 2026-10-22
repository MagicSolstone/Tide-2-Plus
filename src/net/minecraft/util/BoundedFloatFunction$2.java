package net.minecraft.util;

class BoundedFloatFunction$2 implements BoundedFloatFunction<Float> {
   public float apply(final Float value) {
      return value;
   }

   @Override
   public Interval range() {
      return Interval.INFINITE;
   }
}
