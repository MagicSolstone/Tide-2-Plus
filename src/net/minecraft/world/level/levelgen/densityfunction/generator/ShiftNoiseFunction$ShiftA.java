package net.minecraft.world.level.levelgen.densityfunction.generator;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.op.BinaryFunction$ConstMulSampler;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public record ShiftNoiseFunction$ShiftA(Holder<NormalNoise> offsetNoise) implements ShiftNoiseFunction {
   public static final MapCodec<ShiftNoiseFunction$ShiftA> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(NormalNoise.CODEC.fieldOf("noise").forGetter(ShiftNoiseFunction$ShiftA::offsetNoise)).apply(i, ShiftNoiseFunction$ShiftA::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      Noise noise = context.createNoiseSampler(this.offsetNoise);
      return new BinaryFunction$ConstMulSampler(new NoiseFunction$Sampler(noise, 0.25, 0.0), 4.0F);
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      return new ShiftNoiseFunction$ShiftA(this.offsetNoise);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return 5;
   }

   @Override
   public MapCodec<ShiftNoiseFunction$ShiftA> codec() {
      return CODEC;
   }
}
