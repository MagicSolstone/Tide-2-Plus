package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;

public interface BlockStateProviderTypes {
   static MapCodec<? extends BlockStateProvider> bootstrap(final Registry<MapCodec<? extends BlockStateProvider>> registry) {
      Registry.register(registry, "copy_properties", CopyPropertiesProvider.CODEC);
      Registry.register(registry, "dual_noise", DualNoiseProvider.CODEC);
      Registry.register(registry, "noise", NoiseProvider.CODEC);
      Registry.register(registry, "noise_threshold", NoiseThresholdProvider.CODEC);
      Registry.register(registry, "random_block", RandomBlockProvider.CODEC);
      Registry.register(registry, "randomized_int", RandomizedIntStateProvider.CODEC);
      Registry.register(registry, "rotated", RotatedBlockProvider.CODEC);
      Registry.register(registry, "rule_based", RuleBasedStateProvider.CODEC);
      Registry.register(registry, "simple", SimpleStateProvider.CODEC);
      return Registry.register(registry, "weighted", WeightedStateProvider.CODEC);
   }
}
