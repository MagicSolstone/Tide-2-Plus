package net.minecraft.network.protocol.common;

public enum ServerboundResourcePackPacket$Action {
   SUCCESSFULLY_LOADED,
   DECLINED,
   FAILED_DOWNLOAD,
   ACCEPTED,
   DOWNLOADED,
   INVALID_URL,
   FAILED_RELOAD,
   DISCARDED;

   public boolean isTerminal() {
      return this != ACCEPTED && this != DOWNLOADED;
   }
}
