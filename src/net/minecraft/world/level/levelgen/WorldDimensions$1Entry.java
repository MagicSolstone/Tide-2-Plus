package net.minecraft.world.level.levelgen;

import java.util.Optional;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;

record WorldDimensions$1Entry(ResourceKey<LevelStem> key, LevelStem value) {
   private RegistrationInfo registrationInfo() {
      return new RegistrationInfo(Optional.empty(), WorldDimensions.checkStability(this.key, this.value));
   }
}
