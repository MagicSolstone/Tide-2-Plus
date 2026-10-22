package net.minecraft.client.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class MusicManager$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$sounds$SoundEngine$PlayResult[SoundEngine$PlayResult.STARTED.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$sounds$SoundEngine$PlayResult[SoundEngine$PlayResult.STARTED_SILENTLY.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
