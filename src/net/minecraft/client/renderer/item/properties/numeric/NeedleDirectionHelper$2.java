package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class NeedleDirectionHelper$2 implements NeedleDirectionHelper$Wobbler {
   private float targetValue;

   @Override
   public float rotation() {
      return this.targetValue;
   }

   @Override
   public boolean shouldUpdate(final long tick) {
      return true;
   }

   @Override
   public void update(final long tick, final float targetRotation) {
      this.targetValue = targetRotation;
   }
}
