package net.minecraft.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@VisibleForDebug
public record CubicSpline$Multipoint<I extends BoundedFloatFunction<?>>(I coordinate, float[] locations, List<CubicSpline<I>> values, float[] derivatives)
   implements CubicSpline<I> {
   public CubicSpline$Multipoint(I coordinate, float[] locations, List<CubicSpline<I>> values, float[] derivatives) {
      validateSizes(locations, values, derivatives);
      this.coordinate = coordinate;
      this.locations = locations;
      this.values = values;
      this.derivatives = derivatives;
   }

   @Override
   public Interval range() {
      int lastIndex = this.locations.length - 1;
      float minValue = Float.POSITIVE_INFINITY;
      float maxValue = Float.NEGATIVE_INFINITY;
      Interval inputRange = this.coordinate.range();
      if (inputRange.isNaI()) {
         return inputRange;
      } else {
         if (inputRange.min() < this.locations[0]) {
            Interval firstRange = this.values.getFirst().range();
            float edge1 = linearExtend(inputRange.min(), this.locations, firstRange.min(), this.derivatives, 0);
            float edge2 = linearExtend(inputRange.min(), this.locations, firstRange.max(), this.derivatives, 0);
            minValue = Math.min(minValue, Math.min(edge1, edge2));
            maxValue = Math.max(maxValue, Math.max(edge1, edge2));
         }

         if (inputRange.max() > this.locations[lastIndex]) {
            Interval lastRange = this.values.get(lastIndex).range();
            float edge1 = linearExtend(inputRange.max(), this.locations, lastRange.min(), this.derivatives, lastIndex);
            float edge2 = linearExtend(inputRange.max(), this.locations, lastRange.max(), this.derivatives, lastIndex);
            minValue = Math.min(minValue, Math.min(edge1, edge2));
            maxValue = Math.max(maxValue, Math.max(edge1, edge2));
         }

         List<Interval> valueRanges = List.copyOf(Lists.transform(this.values, CubicSpline::range));

         for (Interval range : valueRanges) {
            minValue = Math.min(minValue, range.min());
            maxValue = Math.max(maxValue, range.max());
         }

         for (int i = 0; i < lastIndex; i++) {
            float x1 = this.locations[i];
            float x2 = this.locations[i + 1];
            float xDiff = x2 - x1;
            Interval range1 = valueRanges.get(i);
            Interval range2 = valueRanges.get(i + 1);
            float min1 = range1.min();
            float max1 = range1.max();
            float min2 = range2.min();
            float max2 = range2.max();
            float d1 = this.derivatives[i];
            float d2 = this.derivatives[i + 1];
            if (d1 != 0.0F || d2 != 0.0F) {
               float p1 = d1 * xDiff;
               float p2 = d2 * xDiff;
               float minLerp1 = Math.min(min1, min2);
               float maxLerp1 = Math.max(max1, max2);
               float minA = p1 - max2 + min1;
               float maxA = p1 - min2 + max1;
               float minB = -p2 + min2 - max1;
               float maxB = -p2 + max2 - min1;
               float minLerp2 = Math.min(minA, minB);
               float maxLerp2 = Math.max(maxA, maxB);
               minValue = Math.min(minValue, minLerp1 + 0.25F * minLerp2);
               maxValue = Math.max(maxValue, maxLerp1 + 0.25F * maxLerp2);
            }
         }

         return Interval.of(minValue, maxValue);
      }
   }

   private static float linearExtend(final float input, final float[] locations, final float value, final float[] derivatives, final int index) {
      float derivative = derivatives[index];
      return derivative == 0.0F ? value : value + derivative * (input - locations[index]);
   }

   private static <I> void validateSizes(final float[] locations, final List<CubicSpline<I>> values, final float[] derivatives) {
      if (locations.length != values.size() || locations.length != derivatives.length) {
         throw new IllegalArgumentException("All lengths must be equal, got: " + locations.length + " " + values.size() + " " + derivatives.length);
      } else if (locations.length == 0) {
         throw new IllegalArgumentException("Cannot create a multipoint spline with no points");
      }
   }

   public static <C, I extends BoundedFloatFunction<C>> float sample(final CubicSpline$Multipoint<I> sampler, final C c) {
      return sample(sampler.coordinate, sampler.derivatives, sampler.locations, sampler.values, c);
   }

   private static <C, I extends BoundedFloatFunction<C>> float sample(
      final I coordinate, final float[] derivatives, final float[] locations, final List<CubicSpline<I>> values, final C c
   ) {
      float input = coordinate.apply(c);
      int start = findIntervalStart(locations, input);
      int lastIndex = locations.length - 1;
      if (start < 0) {
         return linearExtend(input, locations, CubicSpline.sample(values.getFirst(), c), derivatives, 0);
      } else if (start == lastIndex) {
         return linearExtend(input, locations, CubicSpline.sample(values.get(lastIndex), c), derivatives, lastIndex);
      } else {
         float x1 = locations[start];
         float x2 = locations[start + 1];
         float t = (input - x1) / (x2 - x1);
         CubicSpline<I> f1 = values.get(start);
         CubicSpline<I> f2 = values.get(start + 1);
         float d1 = derivatives[start];
         float d2 = derivatives[start + 1];
         float y1 = CubicSpline.sample(f1, c);
         float y2 = CubicSpline.sample(f2, c);
         float a = d1 * (x2 - x1) - (y2 - y1);
         float b = -d2 * (x2 - x1) + (y2 - y1);
         return Mth.lerp(t, y1, y2) + t * (1.0F - t) * Mth.lerp(t, a, b);
      }
   }

   private static int findIntervalStart(final float[] locations, final float input) {
      return Mth.binarySearch(0, locations.length, i -> input < locations[i]) - 1;
   }

   @VisibleForTesting
   @Override
   public String parityString() {
      return "Spline{coordinate="
         + this.coordinate
         + ", locations="
         + toString(this.locations)
         + ", derivatives="
         + toString(this.derivatives)
         + ", values="
         + this.values.stream().map(CubicSpline::parityString).collect(Collectors.joining(", ", "[", "]"))
         + "}";
   }

   private static String toString(final float[] arr) {
      return "["
         + IntStream.range(0, arr.length).mapToDouble(i -> arr[i]).mapToObj(f -> String.format(Locale.ROOT, "%.3f", f)).collect(Collectors.joining(", "))
         + "]";
   }

   @Override
   public void forEachCoordinate(final Consumer<I> consumer) {
      consumer.accept(this.coordinate);

      for (CubicSpline<I> spline : this.values) {
         spline.forEachCoordinate(consumer);
      }
   }

   @Override
   public <R extends BoundedFloatFunction<?>> CubicSpline<R> mapCoordinates(final Function<I, R> mapper) {
      return new CubicSpline$Multipoint<>(
         mapper.apply(this.coordinate), this.locations, this.values.stream().map(v -> v.mapCoordinates(mapper)).toList(), this.derivatives
      );
   }

   @Override
   public boolean equals(final Object obj) {
      return !(obj instanceof CubicSpline$Multipoint<?> multipoint)
         ? false
         : Objects.equals(this.coordinate, multipoint.coordinate)
            && Arrays.equals(this.locations, multipoint.locations)
            && Arrays.equals(this.derivatives, multipoint.derivatives)
            && Objects.equals(this.values, multipoint.values);
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.coordinate, Arrays.hashCode(this.locations), this.values, Arrays.hashCode(this.derivatives));
   }

   public static <I extends BoundedFloatFunction<?>> Codec<CubicSpline$Multipoint<I>> codec(
      final Codec<I> coordinateCodec, final Codec<CubicSpline<I>> subSplineCodec
   ) {
      return RecordCodecBuilder.create(
         i -> i.group(
               coordinateCodec.fieldOf("coordinate").forGetter(CubicSpline$Multipoint::coordinate),
               ExtraCodecs.nonEmptyList(CubicSpline$Multipoint$Point.codec(subSplineCodec).listOf())
                  .fieldOf("points")
                  .forGetter(CubicSpline$Multipoint::packToPoints)
            )
            .apply(i, CubicSpline$Multipoint::createFromPoints)
      );
   }

   private List<CubicSpline$Multipoint$Point<I>> packToPoints() {
      int pointCount = this.locations.length;
      List<CubicSpline$Multipoint$Point<I>> list = new ArrayList<>(pointCount);

      for (int p = 0; p < pointCount; p++) {
         list.add(new CubicSpline$Multipoint$Point<>(this.locations[p], this.values.get(p), this.derivatives[p]));
      }

      return list;
   }

   private static <I extends BoundedFloatFunction<?>> CubicSpline$Multipoint<I> createFromPoints(
      final I coordinate, final List<CubicSpline$Multipoint$Point<I>> points
   ) {
      int pointCount = points.size();
      float[] locations = new float[pointCount];
      com.google.common.collect.ImmutableList.Builder<CubicSpline<I>> values = ImmutableList.builderWithExpectedSize(pointCount);
      float[] derivatives = new float[pointCount];

      for (int p = 0; p < pointCount; p++) {
         CubicSpline$Multipoint$Point<I> point = points.get(p);
         locations[p] = point.location();
         values.add(point.value());
         derivatives[p] = point.derivative();
      }

      return new CubicSpline$Multipoint<>(coordinate, locations, values.build(), derivatives);
   }
}
