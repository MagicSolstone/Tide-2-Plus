package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public record FluidPredicate(Optional<HolderSet<Fluid>> fluids, Optional<StatePropertiesPredicate> properties) {
   public static final Codec<FluidPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.FLUID).optionalFieldOf("fluids").forGetter(FluidPredicate::fluids),
            StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(FluidPredicate::properties)
         )
         .apply(i, FluidPredicate::new)
   );

   public boolean matches(final ServerLevel level, final BlockPos pos) {
      if (!level.isLoaded(pos)) {
         return false;
      } else {
         FluidState state = level.getFluidState(pos);
         return this.fluids.isPresent() && !state.is(this.fluids.get()) ? false : !this.properties.isPresent() || this.properties.get().matches(state);
      }
   }
}
