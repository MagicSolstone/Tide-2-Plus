package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction$Axis;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;

public record SliceFunction(Direction$Axis axis, int coordinate, DensityFunction input) implements DensityFunction {
   public static final MapCodec<SliceFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Direction$Axis.CODEC.fieldOf("axis").forGetter(SliceFunction::axis),
            Codec.INT.fieldOf("coordinate").forGetter(SliceFunction::coordinate),
            DensityFunction.CODEC.fieldOf("input").forGetter(SliceFunction::input)
         )
         .apply(i, SliceFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      if (this.input instanceof SliceFunction input) {
         SliceFunction var10000 = input;

         try {
            var17 = var10000.axis();
         } catch (Throwable var11) {
            throw new MatchException(var11.toString(), var11);
         }

         Direction$Axis z = var17;
         var10000 = input;

         try {
            var19 = var10000.coordinate();
         } catch (Throwable var10) {
            throw new MatchException(var10.toString(), var10);
         }

         int var14 = var19;
         if (true) {
            var10000 = input;

            try {
               var21 = var10000.input();
            } catch (Throwable var9) {
               throw new MatchException(var9.toString(), var9);
            }

            DensityFunction var15 = var21;
            if (this.axis == Direction$Axis.X && z == Direction$Axis.Z || this.axis == Direction$Axis.Z && z == Direction$Axis.X) {
               int x;
               int zx;
               if (this.axis == Direction$Axis.X) {
                  x = this.coordinate;
                  zx = var14;
               } else {
                  x = var14;
                  zx = this.coordinate;
               }

               return new SliceFunction$XzSampler(var15.compileSampler(context), x, zx);
            }
         }
      }

      DensitySampler input = this.input.compileSampler(context);

      return (DensitySampler)(switch (this.axis) {
         case X -> new SliceFunction$XSampler(input, this.coordinate);
         case Y -> new SliceFunction$YSampler(input, this.coordinate);
         case Z -> new SliceFunction$ZSampler(input, this.coordinate);
      });
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      return input == this.input ? this : new SliceFunction(this.axis, this.coordinate, input);
   }

   @Override
   public Interval range() {
      return this.input.range();
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.input.domainAxes() & ~DensityFunction.axesFrom(this.axis);
   }

   @Override
   public MapCodec<SliceFunction> codec() {
      return CODEC;
   }
}
