package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class PlayerTabOverlay$HealthState {
   private static final long DISPLAY_UPDATE_DELAY = 20L;
   private static final long DECREASE_BLINK_DURATION = 20L;
   private static final long INCREASE_BLINK_DURATION = 10L;
   private int lastValue;
   private int displayedValue;
   private long lastUpdateTick;
   private long blinkUntilTick;

   public PlayerTabOverlay$HealthState(final int value) {
      this.displayedValue = value;
      this.lastValue = value;
   }

   public void update(final int value, final long tick) {
      if (value != this.lastValue) {
         long blinkDuration = value < this.lastValue ? 20L : 10L;
         this.blinkUntilTick = tick + blinkDuration;
         this.lastValue = value;
         this.lastUpdateTick = tick;
      }

      if (tick - this.lastUpdateTick > 20L) {
         this.displayedValue = value;
      }
   }

   public int displayedValue() {
      return this.displayedValue;
   }

   public boolean isBlinking(final long tick) {
      return this.blinkUntilTick > tick && (this.blinkUntilTick - tick) % 6L >= 3L;
   }
}
