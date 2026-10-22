package net.minecraft.advancements.predicates;

import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.material.Fluid;

public class FluidPredicate$Builder {
   private Optional<HolderSet<Fluid>> fluids = Optional.empty();
   private Optional<StatePropertiesPredicate> properties = Optional.empty();

   private FluidPredicate$Builder() {
   }

   public static FluidPredicate$Builder fluid() {
      return new FluidPredicate$Builder();
   }

   public FluidPredicate$Builder of(final Fluid fluid) {
      this.fluids = Optional.of(HolderSet.direct(fluid.builtInRegistryHolder()));
      return this;
   }

   public FluidPredicate$Builder of(final HolderSet<Fluid> fluids) {
      this.fluids = Optional.of(fluids);
      return this;
   }

   public FluidPredicate$Builder setProperties(final StatePropertiesPredicate properties) {
      this.properties = Optional.of(properties);
      return this;
   }

   public FluidPredicate build() {
      return new FluidPredicate(this.fluids, this.properties);
   }
}
