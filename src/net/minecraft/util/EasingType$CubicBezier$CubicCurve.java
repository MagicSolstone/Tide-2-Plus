package net.minecraft.util;

record EasingType$CubicBezier$CubicCurve(float a, float b, float c) {
   public float sample(final float t) {
      return ((this.a * t + this.b) * t + this.c) * t;
   }

   public float sampleGradient(final float t) {
      return (3.0F * this.a * t + 2.0F * this.b) * t + this.c;
   }
}
