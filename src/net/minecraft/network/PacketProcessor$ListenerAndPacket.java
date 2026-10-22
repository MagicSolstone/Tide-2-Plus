package net.minecraft.network;

import net.minecraft.ReportedException;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;

record PacketProcessor$ListenerAndPacket<T extends PacketListener>(T listener, Packet<T> packet) {
   public void handle() {
      if (this.listener.shouldHandleMessage(this.packet)) {
         try {
            this.packet.handle(this.listener);
         } catch (Exception var3) {
            if (var3 instanceof ReportedException re && re.getCause() instanceof OutOfMemoryError) {
               throw PacketUtils.makeReportedException(var3, this.packet, this.listener);
            }

            this.listener.onPacketError(this.packet, var3);
         }
      } else {
         PacketProcessor.LOGGER.debug("Ignoring packet due to disconnection: {}", this.packet);
      }
   }
}
