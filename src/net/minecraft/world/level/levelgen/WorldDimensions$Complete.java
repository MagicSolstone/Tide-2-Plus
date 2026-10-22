package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Lifecycle;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess$Frozen;
import net.minecraft.core.RegistryAccess$ImmutableRegistryAccess;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.PrimaryLevelData$SpecialWorldProperty;

public record WorldDimensions$Complete(Registry<LevelStem> dimensions, PrimaryLevelData$SpecialWorldProperty specialWorldProperty) {
   public Lifecycle lifecycle() {
      return this.dimensions.registryLifecycle();
   }

   public RegistryAccess$Frozen dimensionsRegistryAccess() {
      return new RegistryAccess$ImmutableRegistryAccess(List.of(this.dimensions)).freeze();
   }
}
