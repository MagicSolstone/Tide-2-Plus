package net.minecraft.util;

class BoundedFloatFunction$1 implements BoundedFloatFunction<C> {
   BoundedFloatFunction$1(final float val$value, final Interval val$range) {
      this.val$value = val$valuex;
      this.val$range = val$rangex;
   }

   @Override
   public float apply(final C c) {
      return this.val$value;
   }

   @Override
   public Interval range() {
      return this.val$range;
   }
}
