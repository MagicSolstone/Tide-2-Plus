package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class EasingType$CubicBezier implements EasingType {
   public static final Codec<EasingType$CubicBezier> CODEC = RecordCodecBuilder.create(
      i -> i.group(EasingType$CubicBezierControls.CODEC.fieldOf("cubic_bezier").forGetter(b -> b.controls)).apply(i, EasingType$CubicBezier::new)
   );
   private static final int NEWTON_RAPHSON_ITERATIONS = 4;
   private static final float MAX_STEP = 0.25F;
   private final EasingType$CubicBezierControls controls;
   private final EasingType$CubicBezier$CubicCurve xCurve;
   private final EasingType$CubicBezier$CubicCurve yCurve;

   public EasingType$CubicBezier(final EasingType$CubicBezierControls controls) {
      this.controls = controls;
      this.xCurve = curveFromControls(controls.x1, controls.x2);
      this.yCurve = curveFromControls(controls.y1, controls.y2);
   }

   private static EasingType$CubicBezier$CubicCurve curveFromControls(final float v1, final float v2) {
      return new EasingType$CubicBezier$CubicCurve(3.0F * v1 - 3.0F * v2 + 1.0F, -6.0F * v1 + 3.0F * v2, 3.0F * v1);
   }

   @Override
   public float apply(final float x) {
      return this.yCurve.sample(this.solveT(x));
   }

   private float solveT(final float x) {
      float t = x;

      for (int i = 0; i < 4; i++) {
         float error = this.xCurve.sample(t) - x;
         if (Math.abs(error) < 1.0E-5F) {
            return t;
         }

         float gradient = this.xCurve.sampleGradient(t);
         if (gradient < 1.0E-5F) {
            break;
         }

         t -= Mth.clamp(error / gradient, -0.25F, 0.25F);
      }

      return this.solveTBisect(x, t);
   }

   private float solveTBisect(final float x, final float initialT) {
      float t0 = 0.0F;
      float t1 = 1.0F;

      float t;
      for (t = initialT; t0 < t1; t = (t1 + t0) / 2.0F) {
         float error = this.xCurve.sample(t) - x;
         if (Math.abs(error) < 1.0E-5F) {
            return t;
         }

         if (error < 0.0F) {
            t0 = t;
         } else {
            t1 = t;
         }
      }

      return t;
   }

   @Override
   public boolean equals(final Object obj) {
      return obj instanceof EasingType$CubicBezier bezier && this.controls.equals(bezier.controls);
   }

   @Override
   public int hashCode() {
      return this.controls.hashCode();
   }

   @Override
   public String toString() {
      return "CubicBezier(" + this.controls.x1 + ", " + this.controls.y1 + ", " + this.controls.x2 + ", " + this.controls.y2 + ")";
   }
}
