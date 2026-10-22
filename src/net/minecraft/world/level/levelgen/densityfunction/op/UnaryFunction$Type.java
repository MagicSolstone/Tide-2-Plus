package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public enum UnaryFunction$Type {
   ABS("abs"),
   SQUARE("square"),
   CUBE("cube"),
   SQRT("sqrt"),
   HALF_NEGATIVE("half_negative"),
   QUARTER_NEGATIVE("quarter_negative"),
   RECIPROCAL("reciprocal"),
   NEGATE("negate"),
   SQUEEZE("squeeze"),
   LOG("log"),
   SIGN("sign");

   public final String id;
   public final MapCodec<UnaryFunction> codec = RecordCodecBuilder.mapCodec(
      i -> i.group(DensityFunction.CODEC.fieldOf("input").forGetter(UnaryFunction::input)).apply(i, input -> new UnaryFunction(this, input))
   );

   private UnaryFunction$Type(final String id) {
      this.id = id;
   }
}
