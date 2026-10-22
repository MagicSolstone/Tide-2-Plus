package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface NeedleDirectionHelper$Wobbler {
   float rotation();

   boolean shouldUpdate(long tick);

   void update(long tick, float targetRotation);
}
