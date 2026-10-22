package net.minecraft.world.level.levelgen.densityfunction.op;

import com.google.common.collect.Comparators;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import org.apache.commons.lang3.mutable.MutableBoolean;

public record IntervalSelectFunction(DensityFunction input, FloatList thresholds, List<DensityFunction> functions) implements DensityFunction {
   private static final Codec<FloatList> THRESHOLDS_CODEC = DensityFunctions.NOISE_VALUE_CODEC.listOf().xmap(FloatArrayList::new, Function.identity());
   public static final MapCodec<IntervalSelectFunction> CODEC = RecordCodecBuilder.mapCodec(
         i -> i.group(
               DensityFunction.CODEC.fieldOf("input").forGetter(IntervalSelectFunction::input),
               THRESHOLDS_CODEC.fieldOf("thresholds").forGetter(IntervalSelectFunction::thresholds),
               DensityFunction.CODEC.listOf(2, Integer.MAX_VALUE).fieldOf("functions").forGetter(IntervalSelectFunction::functions)
            )
            .apply(i, IntervalSelectFunction::new)
      )
      .validate(IntervalSelectFunction::validate);

   private DataResult<IntervalSelectFunction> validate() {
      if (this.thresholds.size() != this.functions.size() - 1) {
         return DataResult.error(
            () -> "Expected " + (this.functions.size() - 1) + " thresholds for " + this.functions.size() + " functions, but got " + this.thresholds.size()
         );
      } else {
         return !Comparators.isInOrder(this.thresholds, Float::compare)
            ? DataResult.error(() -> "Threshold values must be ordered from smallest to largest")
            : DataResult.success(this);
      }
   }

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      DensitySampler input = this.input.compileSampler(context);
      if (this.thresholds.size() == 1) {
         float threshold = this.thresholds.getFloat(0);
         DensitySampler ifBelow = this.functions.getFirst().compileSampler(context);
         DensitySampler ifAbove = this.functions.getLast().compileSampler(context);
         return new IntervalSelectFunction$SingleThresholdSampler(input, threshold, ifBelow, ifAbove);
      } else {
         return new IntervalSelectFunction$Sampler(
            input, this.thresholds.toFloatArray(), this.functions.stream().map(function -> function.compileSampler(context)).toArray(DensitySampler[]::new)
         );
      }
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      MutableBoolean functionsChanged = new MutableBoolean();
      List<DensityFunction> functions = this.functions.stream().map(function -> {
         DensityFunction newFunction = rule.rewrite(function);
         if (newFunction != function) {
            functionsChanged.setTrue();
         }

         return newFunction;
      }).toList();
      return input == this.input && !functionsChanged.booleanValue() ? this : new IntervalSelectFunction(input, this.thresholds, functions);
   }

   @Override
   public Interval range() {
      return Interval.encapsulating(Lists.transform(this.functions, DensityFunction::range));
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      int axes = this.input.domainAxes();

      for (DensityFunction function : this.functions) {
         axes |= function.domainAxes();
      }

      return axes;
   }

   @Override
   public MapCodec<IntervalSelectFunction> codec() {
      return CODEC;
   }
}
