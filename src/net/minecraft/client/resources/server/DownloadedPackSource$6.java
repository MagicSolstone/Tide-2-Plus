package net.minecraft.client.resources.server;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket$Action;

@Environment(EnvType.CLIENT)
class DownloadedPackSource$6 implements PackLoadFeedback {
   DownloadedPackSource$6(final Connection val$connection) {
      this.val$connection = val$connectionx;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void reportUpdate(final UUID id, final PackLoadFeedback$Update result) {
      DownloadedPackSource.LOGGER.debug("Pack {} changed status to {}", id, result);

      ServerboundResourcePackPacket$Action response = switch (result) {
         case ACCEPTED -> ServerboundResourcePackPacket$Action.ACCEPTED;
         case DOWNLOADED -> ServerboundResourcePackPacket$Action.DOWNLOADED;
      };
      this.val$connection.send(new ServerboundResourcePackPacket(id, response));
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void reportFinalResult(final UUID id, final PackLoadFeedback$FinalResult result) {
      DownloadedPackSource.LOGGER.debug("Pack {} changed status to {}", id, result);

      ServerboundResourcePackPacket$Action response = switch (result) {
         case APPLIED -> ServerboundResourcePackPacket$Action.SUCCESSFULLY_LOADED;
         case DOWNLOAD_FAILED -> ServerboundResourcePackPacket$Action.FAILED_DOWNLOAD;
         case DECLINED -> ServerboundResourcePackPacket$Action.DECLINED;
         case DISCARDED -> ServerboundResourcePackPacket$Action.DISCARDED;
         case ACTIVATION_FAILED -> ServerboundResourcePackPacket$Action.FAILED_RELOAD;
      };
      this.val$connection.send(new ServerboundResourcePackPacket(id, response));
   }
}
