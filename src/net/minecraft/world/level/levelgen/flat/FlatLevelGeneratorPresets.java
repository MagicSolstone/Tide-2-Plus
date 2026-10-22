package net.minecraft.world.level.levelgen.flat;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class FlatLevelGeneratorPresets {
   public static final ResourceKey<FlatLevelGeneratorPreset> CLASSIC_FLAT = register("classic_flat");
   public static final ResourceKey<FlatLevelGeneratorPreset> TUNNELERS_DREAM = register("tunnelers_dream");
   public static final ResourceKey<FlatLevelGeneratorPreset> WATER_WORLD = register("water_world");
   public static final ResourceKey<FlatLevelGeneratorPreset> OVERWORLD = register("overworld");
   public static final ResourceKey<FlatLevelGeneratorPreset> SNOWY_KINGDOM = register("snowy_kingdom");
   public static final ResourceKey<FlatLevelGeneratorPreset> BOTTOMLESS_PIT = register("bottomless_pit");
   public static final ResourceKey<FlatLevelGeneratorPreset> DESERT = register("desert");
   public static final ResourceKey<FlatLevelGeneratorPreset> REDSTONE_READY = register("redstone_ready");
   public static final ResourceKey<FlatLevelGeneratorPreset> TEST_WORLD = register("test_world");
   public static final ResourceKey<FlatLevelGeneratorPreset> THE_VOID = register("the_void");

   public static void bootstrap(final BootstrapContext<FlatLevelGeneratorPreset> context) {
      new FlatLevelGeneratorPresets$Bootstrap(context).run();
   }

   private static ResourceKey<FlatLevelGeneratorPreset> register(final String name) {
      return ResourceKey.create(Registries.FLAT_LEVEL_GENERATOR_PRESET, Identifier.withDefaultNamespace(name));
   }
}
