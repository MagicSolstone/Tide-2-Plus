package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.generator.ConstantFunction;
import org.slf4j.Logger;

public record BinaryFunction(BinaryFunction$Type type, DensityFunction left, DensityFunction right) implements DensityFunction {
   private static final Logger LOGGER = LogUtils.getLogger();

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      DensitySampler left = this.left.compileSampler(context);
      DensitySampler right = this.right.compileSampler(context);

      return (DensitySampler)(switch (this.type) {
         case ADD -> {
            if (this.left instanceof ConstantFunction(float var79)) {
               float var50 = var79;
               if (true) {
                  yield new BinaryFunction$ConstAddSampler(right, var50);
               }
            }

            if (this.right instanceof ConstantFunction(float var81)) {
               float var51 = var81;
               if (true) {
                  yield new BinaryFunction$ConstAddSampler(left, var51);
               }
            }

            yield new BinaryFunction$AddSampler(left, right);
         }
         case SUB -> {
            if (this.left instanceof ConstantFunction(float var75)) {
               float var48 = var75;
               if (true) {
                  yield new BinaryFunction$ConstSubSampler(var48, right);
               }
            }

            if (this.right instanceof ConstantFunction(float var77)) {
               float var49 = var77;
               if (true) {
                  yield new BinaryFunction$ConstAddSampler(left, -var49);
               }
            }

            yield new BinaryFunction$SubSampler(left, right);
         }
         case MUL -> {
            if (this.left instanceof ConstantFunction(float var71)) {
               float var46 = var71;
               if (true) {
                  yield new BinaryFunction$ConstMulSampler(right, var46);
               }
            }

            if (this.right instanceof ConstantFunction(float var73)) {
               float var47 = var73;
               if (true) {
                  yield new BinaryFunction$ConstMulSampler(left, var47);
               }
            }

            yield new BinaryFunction$MulSampler(left, right);
         }
         case DIV -> {
            if (this.left instanceof ConstantFunction(float var67)) {
               float rightValue = var67;
               if (true) {
                  yield new BinaryFunction$ConstDivSampler(rightValue, right);
               }
            }

            if (this.right instanceof ConstantFunction(float var69)) {
               float var45 = var69;
               if (true) {
                  yield new BinaryFunction$ConstMulSampler(left, 1.0F / var45);
               }
            }

            yield new BinaryFunction$DivSampler(left, right);
         }
         case MIN -> {
            Interval leftRange = this.left.range();
            Interval rightRange = this.right.range();
            if (leftRange.max() < rightRange.min()) {
               this.warnNonIntersecting();
               yield left;
            } else if (rightRange.max() < leftRange.min()) {
               this.warnNonIntersecting();
               yield right;
            } else {
               if (this.left instanceof ConstantFunction(float var63)) {
                  float var56 = var63;
                  if (true) {
                     yield new BinaryFunction$ConstMinSampler(right, var56);
                  }
               }

               if (this.right instanceof ConstantFunction(float var65)) {
                  float var57 = var65;
                  if (true) {
                     yield new BinaryFunction$ConstMinSampler(left, var57);
                  }
               }

               yield new BinaryFunction$MinSampler(left, right, rightRange.min());
            }
         }
         case MAX -> {
            Interval leftRange = this.left.range();
            Interval rightRange = this.right.range();
            if (leftRange.min() > rightRange.max()) {
               this.warnNonIntersecting();
               yield left;
            } else if (rightRange.min() > leftRange.max()) {
               this.warnNonIntersecting();
               yield right;
            } else {
               if (this.left instanceof ConstantFunction(float var59)) {
                  float var11 = var59;
                  if (true) {
                     yield new BinaryFunction$ConstMaxSampler(right, var11);
                  }
               }

               if (this.right instanceof ConstantFunction(float var61)) {
                  float var55 = var61;
                  if (true) {
                     yield new BinaryFunction$ConstMaxSampler(left, var55);
                  }
               }

               yield new BinaryFunction$MaxSampler(left, right, rightRange.max());
            }
         }
      });
   }

   private void warnNonIntersecting() {
      LOGGER.warn(
         "Compiling a {} function between two non-overlapping inputs: {} ({}) and {} ({})",
         new Object[]{this.type, this.left, this.left.range(), this.right, this.right.range()}
      );
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction left = rule.rewrite(this.left);
      DensityFunction right = rule.rewrite(this.right);
      return left == this.left && this.right == right ? this : new BinaryFunction(this.type, left, right);
   }

   @Override
   public Interval range() {
      Interval left = this.left.range();
      Interval right = this.right.range();

      return switch (this.type) {
         case ADD -> Interval.add(left, right);
         case SUB -> Interval.sub(left, right);
         case MUL -> Interval.mul(left, right);
         case DIV -> Interval.div(left, right);
         case MIN -> Interval.min(left, right);
         case MAX -> Interval.max(left, right);
      };
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.left.domainAxes() | this.right.domainAxes();
   }

   @Override
   public MapCodec<BinaryFunction> codec() {
      return this.type().codec;
   }
}
