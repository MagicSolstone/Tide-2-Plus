package net.minecraft.util;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import java.util.List;

public final class Interval {
   public static final Interval NaI = new Interval(Float.NaN, Float.NaN);
   public static final Interval INFINITE = new Interval(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
   private static final Interval NEGATIVE_ONE_TO_ONE = new Interval(-1.0F, 1.0F);
   private static final Interval ZERO_TO_ONE = new Interval(0.0F, 1.0F);
   private final float min;
   private final float max;

   private Interval(final float min, final float max) {
      this.min = min;
      this.max = max;
   }

   public static Interval of(final float min, final float max) {
      if (max < min) {
         throw new IllegalArgumentException("max (" + max + ") < min (" + min + ")");
      } else if (Float.isNaN(min) || Float.isNaN(max)) {
         throw new IllegalArgumentException("Bounds cannot include NaN [" + min + "; " + max + "]: use Interval.NaI explicitly");
      } else if (min == Float.NEGATIVE_INFINITY && max == Float.POSITIVE_INFINITY) {
         return INFINITE;
      } else {
         if (max == 1.0F) {
            if (min == 0.0F) {
               return ZERO_TO_ONE;
            }

            if (min == -1.0F) {
               return NEGATIVE_ONE_TO_ONE;
            }
         }

         return new Interval(min, max);
      }
   }

   public static Interval ofSymmetric(final float range) {
      return of(-range, range);
   }

   public static Interval ofExact(final float value) {
      return of(value, value);
   }

   public static Interval encapsulating(final List<Interval> intervals) {
      if (intervals.isEmpty()) {
         throw new IllegalArgumentException("At least one interval required");
      } else {
         float min = Float.POSITIVE_INFINITY;
         float max = Float.NEGATIVE_INFINITY;

         for (Interval interval : intervals) {
            if (!interval.isNaI()) {
               min = Math.min(interval.min, min);
               max = Math.max(interval.max, max);
            }
         }

         return max < min ? NaI : of(min, max);
      }
   }

   public static Interval encapsulating(final Interval... intervals) {
      return encapsulating(List.of(intervals));
   }

   public static Interval encapsulating(final float first, final float second) {
      if (Float.isNaN(first) && Float.isNaN(second)) {
         return NaI;
      } else if (Float.isNaN(first)) {
         return ofExact(second);
      } else {
         return Float.isNaN(second) ? ofExact(first) : of(Math.min(first, second), Math.max(first, second));
      }
   }

   private static Interval encapsulating(final Interval first, final float second) {
      if (Float.isNaN(second)) {
         return first;
      } else {
         return first.isNaI() ? ofExact(second) : of(Math.min(first.min(), second), Math.max(first.max(), second));
      }
   }

   public static Interval add(final Interval left, final Interval right) {
      float min = left.min + right.min;
      float max = left.max + right.max;
      return !Float.isNaN(min) && !Float.isNaN(max) ? of(min, max) : NaI;
   }

   public static Interval sub(final Interval left, final Interval right) {
      float min = left.min - right.max;
      float max = left.max - right.min;
      return !Float.isNaN(min) && !Float.isNaN(max) ? of(min, max) : NaI;
   }

   public static Interval mul(final Interval left, final Interval right) {
      if (!left.isNaI() && !right.isNaI()) {
         float minMin = mulBound(left.min, right.min);
         float minMax = mulBound(left.min, right.max);
         float maxMin = mulBound(left.max, right.min);
         float maxMax = mulBound(left.max, right.max);
         return of(Math.min(Math.min(minMin, minMax), Math.min(maxMin, maxMax)), Math.max(Math.max(minMin, minMax), Math.max(maxMin, maxMax)));
      } else {
         return NaI;
      }
   }

   private static float mulBound(final float left, final float right) {
      return left != 0.0F && right != 0.0F ? left * right : 0.0F;
   }

   public static Interval reciprocal(final Interval input) {
      if (!input.isNaI() && (input.min != 0.0F || input.max != 0.0F)) {
         if (!input.contains(0.0F)) {
            return of(1.0F / input.max, 1.0F / input.min);
         } else if (input.max == 0.0F) {
            return of(Float.NEGATIVE_INFINITY, 1.0F / input.min);
         } else {
            return input.min == 0.0F ? of(1.0F / input.max, Float.POSITIVE_INFINITY) : INFINITE;
         }
      } else {
         return NaI;
      }
   }

   public static Interval div(final Interval left, final Interval right) {
      return mul(left, reciprocal(right));
   }

   public static Interval min(final Interval left, final Interval right) {
      return !left.isNaI() && !right.isNaI() ? of(Math.min(left.min, right.min), Math.min(left.max, right.max)) : NaI;
   }

   public static Interval max(final Interval left, final Interval right) {
      return !left.isNaI() && !right.isNaI() ? of(Math.max(left.min, right.min), Math.max(left.max, right.max)) : NaI;
   }

   public static Interval clamp(final Interval input, final float min, final float max) {
      if (min > max) {
         throw new IllegalArgumentException("min (" + min + ") > max (" + max + ")");
      } else if (input.isNaI()) {
         return NaI;
      } else if (input.min >= max) {
         return of(max, max);
      } else {
         return input.max <= min ? of(min, min) : of(Math.max(input.min, min), Math.min(input.max, max));
      }
   }

   public static Interval abs(final Interval input) {
      if (input.isNaI()) {
         return NaI;
      } else {
         float max = Math.max(Math.abs(input.min), Math.abs(input.max));
         return input.contains(0.0F) ? of(0.0F, max) : of(Math.min(Math.abs(input.min), Math.abs(input.max)), max);
      }
   }

   public static Interval square(final Interval input) {
      if (input.isNaI()) {
         return NaI;
      } else {
         float max = Math.max(Mth.square(input.min), Mth.square(input.max));
         return input.contains(0.0F) ? of(0.0F, max) : of(Math.min(Mth.square(input.min), Mth.square(input.max)), max);
      }
   }

   public static Interval pow(final Interval base, final Interval exponent) {
      if (!base.isNaI() && !exponent.isNaI()) {
         if (base.min() == base.max()) {
            return pow(base.min(), exponent);
         } else {
            Interval result = encapsulating(pow(base.min(), exponent), pow(base.max(), exponent));
            if (base.contains(0.0F)) {
               if (base.max() > 0.0F) {
                  result = encapsulating(result, pow(0.0F, exponent));
               }

               if (base.min() < 0.0F) {
                  result = encapsulating(result, pow(-0.0F, exponent));
               }
            }

            return result;
         }
      } else {
         return NaI;
      }
   }

   private static Interval pow(final float base, final Interval exponent) {
      if (!Float.isNaN(base) && !exponent.isNaI()) {
         if (exponent.min() == exponent.max()) {
            float value = (float)Math.pow(base, exponent.min());
            return Float.isNaN(value) ? NaI : ofExact(value);
         } else if (base == 0.0F) {
            return mul(powZeroBase(exponent), ofExact(Math.copySign(1.0F, base)));
         } else if (base == 1.0F) {
            return ofExact(1.0F);
         } else {
            return base > 0.0F ? powPositiveBase(base, exponent) : powNegativeBase(base, exponent);
         }
      } else {
         return NaI;
      }
   }

   private static Interval powPositiveBase(final float base, final Interval exponent) {
      return Float.isFinite(exponent.min()) && Float.isFinite(exponent.max())
         ? encapsulating((float)Math.pow(base, exponent.min()), (float)Math.pow(base, exponent.max()))
         : powInfiniteExponent(base, exponent);
   }

   private static Interval powZeroBase(final Interval exponent) {
      if (exponent.contains(0.0F)) {
         if (exponent.max() == 0.0F) {
            return of(1.0F, Float.POSITIVE_INFINITY);
         } else {
            return exponent.min() == 0.0F ? ZERO_TO_ONE : of(0.0F, Float.POSITIVE_INFINITY);
         }
      } else {
         return exponent.max() < 0.0F ? ofExact(Float.POSITIVE_INFINITY) : ofExact(0.0F);
      }
   }

   private static Interval powInfiniteExponent(final float base, final Interval exponent) {
      if (Float.isInfinite(exponent.min()) && Float.isInfinite(exponent.max())) {
         return of(0.0F, Float.POSITIVE_INFINITY);
      } else if (Float.isInfinite(exponent.min())) {
         return base < 1.0F ? of((float)Math.pow(base, exponent.max()), Float.POSITIVE_INFINITY) : of(0.0F, (float)Math.pow(base, exponent.max()));
      } else {
         return base < 1.0F ? of(0.0F, (float)Math.pow(base, exponent.min())) : of((float)Math.pow(base, exponent.min()), Float.POSITIVE_INFINITY);
      }
   }

   private static Interval powNegativeBase(final float base, final Interval exponent) {
      float exponentMinInt = (float)Math.ceil(exponent.min());
      float exponentMaxInt = (float)Math.floor(exponent.max());
      if (exponentMaxInt < exponentMinInt) {
         return NaI;
      } else {
         float baseToMinInt = (float)Math.pow(base, exponentMinInt);
         float baseToMaxInt = (float)Math.pow(base, exponentMaxInt);
         Interval result = encapsulating(baseToMinInt, baseToMaxInt);
         if (Float.isInfinite(exponentMinInt)) {
            result = encapsulating(result, -baseToMinInt);
         } else if (exponentMinInt + 1.0F < exponentMaxInt) {
            result = encapsulating(result, (float)Math.pow(base, exponentMinInt + 1.0F));
         }

         if (Float.isInfinite(exponentMaxInt)) {
            result = encapsulating(result, -baseToMaxInt);
         } else if (exponentMaxInt - 1.0F > exponentMinInt) {
            result = encapsulating(result, (float)Math.pow(base, exponentMaxInt - 1.0F));
         }

         return result;
      }
   }

   public static Interval log(final Interval input) {
      if (input.max() < 0.0F) {
         return NaI;
      } else {
         Interval clippedInput = max(input, ofExact(0.0F));
         return mapMonotonic(clippedInput, x -> (float)Math.log(x));
      }
   }

   public static Interval mapMonotonic(final Interval input, final FloatUnaryOperator monotonicOp) {
      if (input.isNaI()) {
         return NaI;
      } else {
         float mappedMin = monotonicOp.apply(input.min);
         float mappedMax = monotonicOp.apply(input.max);
         if (!Float.isNaN(mappedMin) && !Float.isNaN(mappedMax)) {
            return of(Math.min(mappedMin, mappedMax), Math.max(mappedMin, mappedMax));
         } else {
            throw new IllegalStateException("Monotonic operator should not produce NaN");
         }
      }
   }

   public static Interval lerp(final Interval alpha, final Interval first, final Interval second) {
      return !alpha.isNaI() && !first.isNaI() && !second.isNaI()
         ? encapsulating(
            lerp(alpha, first.min, second.min), lerp(alpha, first.max, second.min), lerp(alpha, first.min, second.max), lerp(alpha, first.max, second.max)
         )
         : NaI;
   }

   public static Interval lerp(final Interval alpha, final float first, final float second) {
      if (alpha.isNaI() || Float.isNaN(first) || Float.isNaN(second)) {
         return NaI;
      } else {
         return Float.isFinite(first) && Float.isFinite(second) ? lerpFiniteBounds(alpha, first, second) : lerpInfiniteBounds(alpha, first, second);
      }
   }

   private static Interval lerpFiniteBounds(final Interval alpha, final float first, final float second) {
      return encapsulating(lerpFiniteBound(alpha.min, first, second), lerpFiniteBound(alpha.max, first, second));
   }

   private static float lerpFiniteBound(final float alpha, final float first, final float second) {
      return first + mulBound(alpha, second - first);
   }

   private static Interval lerpInfiniteBounds(final Interval alpha, final float first, final float second) {
      if (first == second) {
         return ofExact(first);
      } else {
         float newMin = lerpInfiniteBound(alpha.min, first, second);
         float newMax = lerpInfiniteBound(alpha.max, first, second);
         return !Float.isNaN(newMin) && !Float.isNaN(newMax) ? encapsulating(newMin, newMax) : NaI;
      }
   }

   private static float lerpInfiniteBound(final float alpha, final float first, final float second) {
      float firstPart = mulBound(1.0F - alpha, first);
      float secondPart = mulBound(alpha, second);
      if (!Float.isInfinite(firstPart) || !Float.isInfinite(secondPart)) {
         return firstPart + secondPart;
      } else if (alpha <= 0.0F) {
         return second > first ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
      } else if (alpha >= 1.0F) {
         return second > first ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
      } else {
         return Float.NaN;
      }
   }

   public static Interval sign(final Interval input) {
      if (input.isNaI()) {
         return NaI;
      } else if (input.min() == input.max()) {
         return ofExact(Math.signum(input.min()));
      } else if (input.contains(0.0F)) {
         if (input.min() == 0.0F) {
            return of(0.0F, 1.0F);
         } else {
            return input.max() == 0.0F ? of(-1.0F, 0.0F) : of(-1.0F, 1.0F);
         }
      } else {
         return ofExact(input.min() > 0.0F ? 1.0F : -1.0F);
      }
   }

   public boolean contains(final float value) {
      return value >= this.min && value <= this.max;
   }

   public boolean intersects(final Interval other) {
      return this.min <= other.max && this.max >= other.min;
   }

   public boolean isNaI() {
      return this == NaI;
   }

   public float min() {
      return this.min;
   }

   public float max() {
      return this.max;
   }

   @Override
   public boolean equals(final Object obj) {
      return obj == this ? true : obj instanceof Interval interval && this.min == interval.min && this.max == interval.max;
   }

   @Override
   public int hashCode() {
      int hash = Float.hashCode(this.min);
      return hash * 31 + Float.hashCode(this.max);
   }

   @Override
   public String toString() {
      return this.isNaI() ? "[NaN]" : "[" + this.min + "; " + this.max + "]";
   }
}
