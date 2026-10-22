package net.minecraft.world.level.levelgen.densityfunction.generator;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public interface ShiftNoiseFunction extends DensityFunction {
   double COORDINATE_FACTOR = 0.25;
   float VALUE_FACTOR = 4.0F;

   Holder<NormalNoise> offsetNoise();

   @Override
   default Interval range() {
      return Interval.mul(this.offsetNoise().value().range(), Interval.ofExact(4.0F));
   }

   @Override
   MapCodec<? extends ShiftNoiseFunction> codec();
}
