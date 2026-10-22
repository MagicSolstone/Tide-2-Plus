package net.minecraft.client.renderer.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.lighting.LayerLightSectionStorage$SectionType;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

@Environment(EnvType.CLIENT)
final class LightSectionDebugRenderer$SectionData {
   private final DiscreteVoxelShape lightAndBlocksShape;
   private final DiscreteVoxelShape lightShape;
   private final SectionPos minPos;

   private LightSectionDebugRenderer$SectionData(final LevelLightEngine engine, final SectionPos centerPos, final int radius, final LightLayer lightLayer) {
      int size = radius * 2 + 1;
      this.lightAndBlocksShape = new BitSetDiscreteVoxelShape(size, size, size);
      this.lightShape = new BitSetDiscreteVoxelShape(size, size, size);

      for (int z = 0; z < size; z++) {
         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               SectionPos pos = SectionPos.of(centerPos.x() + x - radius, centerPos.y() + y - radius, centerPos.z() + z - radius);
               LayerLightSectionStorage$SectionType type = engine.getDebugSectionType(lightLayer, pos);
               if (type == LayerLightSectionStorage$SectionType.LIGHT_AND_DATA) {
                  this.lightAndBlocksShape.fill(x, y, z);
                  this.lightShape.fill(x, y, z);
               } else if (type == LayerLightSectionStorage$SectionType.LIGHT_ONLY) {
                  this.lightShape.fill(x, y, z);
               }
            }
         }
      }

      this.minPos = SectionPos.of(centerPos.x() - radius, centerPos.y() - radius, centerPos.z() - radius);
   }
}
