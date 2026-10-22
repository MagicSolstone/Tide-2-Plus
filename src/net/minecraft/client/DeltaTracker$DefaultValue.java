package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DeltaTracker$DefaultValue implements DeltaTracker {
   private final float value;

   private DeltaTracker$DefaultValue(final float value) {
      this.value = value;
   }

   @Override
   public float getGameTimeDeltaTicks() {
      return this.value;
   }

   @Override
   public float getGameTimeDeltaPartialTick(final boolean ignored) {
      return this.value;
   }

   @Override
   public float getRealtimeDeltaTicks() {
      return this.value;
   }
}
