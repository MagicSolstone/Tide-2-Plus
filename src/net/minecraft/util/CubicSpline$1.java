package net.minecraft.util;

class CubicSpline$1 implements BoundedFloatFunction<C> {
   CubicSpline$1(final CubicSpline$Multipoint val$multipoint) {
      this.val$multipoint = val$multipointx;
   }

   @Override
   public float apply(final C c) {
      return CubicSpline$Multipoint.sample(this.val$multipoint, c);
   }

   @Override
   public Interval range() {
      return this.val$multipoint.range();
   }
}
