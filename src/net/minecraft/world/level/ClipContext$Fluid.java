package net.minecraft.world.level;

import java.util.function.Predicate;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;

public enum ClipContext$Fluid {
   NONE(state -> false),
   SOURCE_ONLY(FluidState::isSource),
   ANY(state -> !state.isEmpty()),
   WATER(fluidState -> fluidState.is(FluidTags.WATER));

   private final Predicate<FluidState> canPick;

   private ClipContext$Fluid(final Predicate<FluidState> canPick) {
      this.canPick = canPick;
   }

   public boolean canPick(final FluidState fluidState) {
      return this.canPick.test(fluidState);
   }
}
