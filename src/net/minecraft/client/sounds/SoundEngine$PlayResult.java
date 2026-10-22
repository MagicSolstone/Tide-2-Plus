package net.minecraft.client.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum SoundEngine$PlayResult {
   STARTED,
   STARTED_SILENTLY,
   NOT_STARTED;
}
