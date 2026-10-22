package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
class NeedleDirectionHelper$1 implements NeedleDirectionHelper$Wobbler {
   private float rotation;
   private float deltaRotation;
   private long lastUpdateTick;

   NeedleDirectionHelper$1(final float val$factor) {
      this.val$factor = val$factorx;
   }

   @Override
   public float rotation() {
      return this.rotation;
   }

   @Override
   public boolean shouldUpdate(final long tick) {
      return this.lastUpdateTick != tick;
   }

   @Override
   public void update(final long tick, final float targetRotation) {
      this.lastUpdateTick = tick;
      float tempDeltaRotation = Mth.positiveModulo(targetRotation - this.rotation + 0.5F, 1.0F) - 0.5F;
      this.deltaRotation += tempDeltaRotation * 0.1F;
      this.deltaRotation = this.deltaRotation * this.val$factor;
      this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0F);
   }
}
