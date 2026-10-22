package net.minecraft.client.renderer.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.BannerBlock$AttachmentType;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class BannerRenderer$1 {
   static {
      try {
         $SwitchMap$net$minecraft$world$level$block$BannerBlock$AttachmentType[BannerBlock$AttachmentType.WALL.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$BannerBlock$AttachmentType[BannerBlock$AttachmentType.GROUND.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
