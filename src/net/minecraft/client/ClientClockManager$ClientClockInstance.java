package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.clock.ClockInstance;

@Environment(EnvType.CLIENT)
public class ClientClockManager$ClientClockInstance implements ClockInstance {
   private long totalTicks;
   private float partialTick;
   private float rate = 1.0F;

   @Override
   public long totalTicks() {
      return this.totalTicks;
   }

   @Override
   public float partialTick() {
      return this.partialTick;
   }

   @Override
   public float rate() {
      return this.rate;
   }

   @Override
   public boolean isPaused() {
      return this.rate == 0.0F;
   }
}
