package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public enum BinaryFunction$Type {
   ADD("add"),
   SUB("sub"),
   MUL("mul"),
   DIV("div"),
   MIN("min"),
   MAX("max");

   public final String id;
   public final MapCodec<BinaryFunction> codec = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("left").forGetter(BinaryFunction::left), DensityFunction.CODEC.fieldOf("right").forGetter(BinaryFunction::right)
         )
         .apply(i, (left, right) -> new BinaryFunction(this, left, right))
   );

   private BinaryFunction$Type(final String id) {
      this.id = id;
   }
}
