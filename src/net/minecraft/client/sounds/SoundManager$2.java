package net.minecraft.client.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.Sound$Type;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class SoundManager$2 {
   static {
      try {
         $SwitchMap$net$minecraft$client$resources$sounds$Sound$Type[Sound$Type.FILE.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$sounds$Sound$Type[Sound$Type.SOUND_EVENT.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
