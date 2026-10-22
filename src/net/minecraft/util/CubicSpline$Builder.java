package net.minecraft.util;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import java.util.List;

public final class CubicSpline$Builder<I extends BoundedFloatFunction<?>> {
   private final I coordinate;
   private final Float2FloatFunction valueTransformer;
   private final FloatList locations = new FloatArrayList();
   private final List<CubicSpline<I>> values = Lists.newArrayList();
   private final FloatList derivatives = new FloatArrayList();

   private CubicSpline$Builder(final I coordinate) {
      this(coordinate, Float2FloatFunction.identity());
   }

   private CubicSpline$Builder(final I coordinate, final Float2FloatFunction valueTransformer) {
      this.coordinate = coordinate;
      this.valueTransformer = valueTransformer;
   }

   public CubicSpline$Builder<I> addPoint(final float location, final float value) {
      return this.addPoint(location, new CubicSpline$Constant<>((Float)this.valueTransformer.apply(value)), 0.0F);
   }

   public CubicSpline$Builder<I> addPoint(final float location, final float value, final float derivative) {
      return this.addPoint(location, new CubicSpline$Constant<>((Float)this.valueTransformer.apply(value)), derivative);
   }

   public CubicSpline$Builder<I> addPoint(final float location, final CubicSpline<I> sampler) {
      return this.addPoint(location, sampler, 0.0F);
   }

   private CubicSpline$Builder<I> addPoint(final float location, final CubicSpline<I> sampler, final float derivative) {
      if (!this.locations.isEmpty() && location <= this.locations.getFloat(this.locations.size() - 1)) {
         throw new IllegalArgumentException("Please register points in ascending order");
      } else {
         this.locations.add(location);
         this.values.add(sampler);
         this.derivatives.add(derivative);
         return this;
      }
   }

   public CubicSpline<I> build() {
      if (this.locations.isEmpty()) {
         throw new IllegalStateException("No elements added");
      } else {
         return new CubicSpline$Multipoint<>(this.coordinate, this.locations.toFloatArray(), List.copyOf(this.values), this.derivatives.toFloatArray());
      }
   }
}
