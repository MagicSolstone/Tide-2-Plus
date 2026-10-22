package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public abstract class ResettingWorldTask extends LongRunningTask {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final long serverId;
   private final Component title;
   private final Runnable callback;

   public ResettingWorldTask(final long serverId, final Component title, final Runnable callback) {
      this.serverId = serverId;
      this.title = title;
      this.callback = callback;
   }

   protected abstract void sendResetRequest(final RealmsClient client, final long serverId) throws RealmsServiceException;

   @Override
   public void run() {
      RealmsClient client = RealmsClient.getOrCreate();
      int i = 0;

      while (i < 25) {
         try {
            if (this.aborted()) {
               return;
            }

            this.sendResetRequest(client, this.serverId);
            if (this.aborted()) {
               return;
            }

            this.callback.run();
            return;
         } catch (RetryCallException var4) {
            if (this.aborted()) {
               return;
            }

            pause(var4.delaySeconds);
            i++;
         } catch (Exception var5) {
            if (this.aborted()) {
               return;
            }

            LOGGER.error("Couldn't reset world");
            this.error(var5);
            return;
         }
      }
   }

   @Override
   public Component getTitle() {
      return this.title;
   }
}
