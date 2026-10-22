package net.minecraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.LightLayer;

@FunctionalInterface
public interface LightCoordsUtil$BrightnessGetter {
   LightCoordsUtil$BrightnessGetter DEFAULT = (level, pos) -> {
      int sky = level.getBrightness(LightLayer.SKY, pos);
      int block = level.getBrightness(LightLayer.BLOCK, pos);
      return LightCoordsUtil.pack(block, sky);
   };

   int packedBrightness(BlockAndLightGetter level, BlockPos pos);
}
