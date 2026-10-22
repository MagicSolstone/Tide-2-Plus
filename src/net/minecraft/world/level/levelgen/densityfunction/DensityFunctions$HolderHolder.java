package net.minecraft.world.level.levelgen.densityfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.util.Interval;

public record DensityFunctions$HolderHolder(Holder<DensityFunction> function) implements DensityFunction {
   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return this.function.value().compileSampler(context);
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction newFunction = rule.rewrite(this.function.value());
      return (DensityFunction)(newFunction == this.function.value() ? this : newFunction);
   }

   @Override
   public Interval range() {
      return this.function.value().range();
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.function.value().domainAxes();
   }

   @Override
   public MapCodec<DensityFunctions$HolderHolder> codec() {
      throw new UnsupportedOperationException("Calling .codec() on HolderHolder");
   }
}
