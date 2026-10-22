package net.minecraft.world.level.levelgen.densityfunction.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction$Axis;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.TilingMode;

public record GradientFunction(Direction$Axis axis, TilingMode tiling, int fromCoordinate, int toCoordinate, float fromValue, float toValue)
   implements DensityFunction {
   public static final MapCodec<GradientFunction> CODEC = RecordCodecBuilder.mapCodec(
         i -> i.group(
               Direction$Axis.CODEC.fieldOf("axis").forGetter(GradientFunction::axis),
               TilingMode.CODEC.optionalFieldOf("tiling", TilingMode.CLAMP_TO_EDGE).forGetter(GradientFunction::tiling),
               Codec.INT.fieldOf("from_coordinate").forGetter(GradientFunction::fromCoordinate),
               Codec.INT.fieldOf("to_coordinate").forGetter(GradientFunction::toCoordinate),
               DensityFunctions.NOISE_VALUE_CODEC.fieldOf("from_value").forGetter(GradientFunction::fromValue),
               DensityFunctions.NOISE_VALUE_CODEC.fieldOf("to_value").forGetter(GradientFunction::toValue)
            )
            .apply(i, GradientFunction::new)
      )
      .validate(GradientFunction::validate);

   private static DataResult<GradientFunction> validate(final GradientFunction gradient) {
      return gradient.fromCoordinate == gradient.toCoordinate
         ? DataResult.error(() -> "from_coordinate cannot be equal to to_coordinate")
         : DataResult.success(gradient);
   }

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      int coordinateRange = this.toCoordinate - this.fromCoordinate;
      float coordinateFactor = (this.toValue - this.fromValue) / coordinateRange;

      return (DensitySampler)(switch (this.tiling) {
         case CLAMP_TO_EDGE -> {
            int minCoordinate = Math.min(this.fromCoordinate, this.toCoordinate);
            int maxCoordinate = Math.max(this.fromCoordinate, this.toCoordinate);
            yield new GradientFunction$ClampedSampler(this.axis, this.fromCoordinate, minCoordinate, maxCoordinate, this.fromValue, coordinateFactor);
         }
         case REPEAT -> new GradientFunction$RepeatSampler(this.axis, this.fromCoordinate, coordinateRange, this.fromValue, coordinateFactor);
         case MIRRORED_REPEAT -> new GradientFunction$MirroredRepeatSampler(this.axis, this.fromCoordinate, coordinateRange, this.fromValue, coordinateFactor);
      });
   }

   @Override
   public Interval range() {
      return Interval.encapsulating(this.fromValue, this.toValue);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return DensityFunction.axesFrom(this.axis);
   }

   @Override
   public MapCodec<GradientFunction> codec() {
      return CODEC;
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      return this;
   }
}
