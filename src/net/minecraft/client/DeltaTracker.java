package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DeltaTracker {
   DeltaTracker ZERO = new DeltaTracker$DefaultValue(0.0F);
   DeltaTracker ONE = new DeltaTracker$DefaultValue(1.0F);

   float getGameTimeDeltaTicks();

   float getGameTimeDeltaPartialTick(boolean ignoreFrozenGame);

   float getRealtimeDeltaTicks();
}
