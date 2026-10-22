package net.minecraft.client.renderer.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.levelgen.Heightmap$Types;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class HeightMapRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$levelgen$Heightmap$Types[Heightmap$Types.WORLD_SURFACE_WG.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$Heightmap$Types[Heightmap$Types.OCEAN_FLOOR_WG.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$Heightmap$Types[Heightmap$Types.WORLD_SURFACE.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$Heightmap$Types[Heightmap$Types.OCEAN_FLOOR.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$Heightmap$Types[Heightmap$Types.MOTION_BLOCKING.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$levelgen$Heightmap$Types[Heightmap$Types.MOTION_BLOCKING_NO_LEAVES.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
