package net.minecraft.client.resources.server;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class DownloadedPackSource$1 implements PackLoadFeedback {
   @Override
   public void reportUpdate(final UUID id, final PackLoadFeedback$Update update) {
      DownloadedPackSource.LOGGER.debug("Downloaded pack {} changed state to {}", id, update);
   }

   @Override
   public void reportFinalResult(final UUID id, final PackLoadFeedback$FinalResult result) {
      DownloadedPackSource.LOGGER.debug("Downloaded pack {} finished with state {}", id, result);
   }
}
