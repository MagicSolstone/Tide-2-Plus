package net.minecraft.client.renderer.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class MovingBlockFeatureRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$renderer$chunk$ChunkSectionLayer[ChunkSectionLayer.SOLID.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$renderer$chunk$ChunkSectionLayer[ChunkSectionLayer.CUTOUT.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$renderer$chunk$ChunkSectionLayer[ChunkSectionLayer.TRANSLUCENT.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
