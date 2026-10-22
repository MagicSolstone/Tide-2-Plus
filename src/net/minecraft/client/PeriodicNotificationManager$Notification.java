package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record PeriodicNotificationManager$Notification(long delay, long period, String title, String message) {
   public PeriodicNotificationManager$Notification(final long delay, final long period, final String title, final String message) {
      this.delay = delay != 0L ? delay : period;
      this.period = period;
      this.title = title;
      this.message = message;
   }
}
