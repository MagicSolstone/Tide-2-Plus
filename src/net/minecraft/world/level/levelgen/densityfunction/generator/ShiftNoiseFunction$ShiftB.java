package net.minecraft.world.level.levelgen.densityfunction.generator;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public record ShiftNoiseFunction$ShiftB(Holder<NormalNoise> offsetNoise) implements ShiftNoiseFunction {
   public static final MapCodec<ShiftNoiseFunction$ShiftB> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(NormalNoise.CODEC.fieldOf("noise").forGetter(ShiftNoiseFunction$ShiftB::offsetNoise)).apply(i, ShiftNoiseFunction$ShiftB::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      Noise noise = context.createNoiseSampler(this.offsetNoise);
      return new ShiftNoiseFunction$ShiftB$1(this, noise);
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      return new ShiftNoiseFunction$ShiftB(this.offsetNoise);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return 5;
   }

   @Override
   public MapCodec<ShiftNoiseFunction$ShiftB> codec() {
      return CODEC;
   }
}
