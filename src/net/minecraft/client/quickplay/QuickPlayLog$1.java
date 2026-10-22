package net.minecraft.client.quickplay;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
class QuickPlayLog$1 extends QuickPlayLog {
   QuickPlayLog$1(final String quickPlayPath) {
      super(quickPlayPath);
   }

   @Override
   public void log(final Minecraft minecraft) {
   }

   @Override
   public void setWorldData(final QuickPlayLog$Type type, final String id, final String name) {
   }
}
