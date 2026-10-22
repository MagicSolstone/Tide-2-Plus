package net.minecraft.client;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.SystemToast$SystemToastId;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class PeriodicNotificationManager$NotificationTask extends TimerTask {
   private final Minecraft minecraft = Minecraft.getInstance();
   private final List<PeriodicNotificationManager$Notification> notifications;
   private final long period;
   private final AtomicLong elapsed;

   public PeriodicNotificationManager$NotificationTask(
      final List<PeriodicNotificationManager$Notification> notifications, final long elapsed, final long period
   ) {
      this.notifications = notifications;
      this.period = period;
      this.elapsed = new AtomicLong(elapsed);
   }

   public PeriodicNotificationManager$NotificationTask reset(final List<PeriodicNotificationManager$Notification> notifications, final long period) {
      this.cancel();
      return new PeriodicNotificationManager$NotificationTask(notifications, this.elapsed.get(), period);
   }

   @Override
   public void run() {
      long currentMinute = this.elapsed.getAndAdd(this.period);
      long nextMinute = this.elapsed.get();

      for (PeriodicNotificationManager$Notification notification : this.notifications) {
         if (currentMinute >= notification.delay) {
            long elapsedPeriods = currentMinute / notification.period;
            long currentPeriods = nextMinute / notification.period;
            if (elapsedPeriods != currentPeriods) {
               this.minecraft
                  .execute(
                     () -> SystemToast.add(
                        Minecraft.getInstance().gui.toastManager(),
                        SystemToast$SystemToastId.PERIODIC_NOTIFICATION,
                        Component.translatable(notification.title, elapsedPeriods),
                        Component.translatable(notification.message, elapsedPeriods)
                     )
                  );
               return;
            }
         }
      }
   }
}
