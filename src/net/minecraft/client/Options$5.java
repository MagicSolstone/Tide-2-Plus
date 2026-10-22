package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class Options$5 {
   static {
      try {
         $SwitchMap$net$minecraft$client$TextureFilteringMethod[TextureFilteringMethod.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$client$TextureFilteringMethod[TextureFilteringMethod.RGSS.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$client$TextureFilteringMethod[TextureFilteringMethod.ANISOTROPIC.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      $SwitchMap$net$minecraft$client$PrioritizeChunkUpdates = new int[PrioritizeChunkUpdates.values().length];

      try {
         $SwitchMap$net$minecraft$client$PrioritizeChunkUpdates[PrioritizeChunkUpdates.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$client$PrioritizeChunkUpdates[PrioritizeChunkUpdates.PLAYER_AFFECTED.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$client$PrioritizeChunkUpdates[PrioritizeChunkUpdates.NEARBY.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$net$minecraft$client$InactivityFpsLimit = new int[InactivityFpsLimit.values().length];

      try {
         $SwitchMap$net$minecraft$client$InactivityFpsLimit[InactivityFpsLimit.MINIMIZED.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$InactivityFpsLimit[InactivityFpsLimit.AFK.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
