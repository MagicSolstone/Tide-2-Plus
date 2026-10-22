package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;

public enum RoundFunction$Type {
   FLOOR("floor"),
   ROUND("round"),
   CEIL("ceil"),
   TRUNCATE("truncate");

   public final String id;
   public final MapCodec<RoundFunction> codec = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("input").forGetter(RoundFunction::input),
            DensityFunction.CODEC.optionalFieldOf("multiple", DensityFunctions.constant(1.0F)).forGetter(RoundFunction::multiple)
         )
         .apply(i, (input, multiple) -> new RoundFunction(this, input, multiple))
   );

   private RoundFunction$Type(final String id) {
      this.id = id;
   }
}
