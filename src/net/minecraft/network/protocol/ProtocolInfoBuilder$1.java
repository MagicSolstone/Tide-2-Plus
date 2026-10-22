package net.minecraft.network.protocol;

import java.util.List;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.ProtocolInfo$Details;
import net.minecraft.network.ProtocolInfo$Details$PacketVisitor;

class ProtocolInfoBuilder$1 implements ProtocolInfo$Details {
   ProtocolInfoBuilder$1(final ConnectionProtocol val$protocol, final PacketFlow val$flow, final List val$codecs) {
      this.val$protocol = val$protocolx;
      this.val$flow = val$flowx;
      this.val$codecs = val$codecsx;
   }

   @Override
   public ConnectionProtocol id() {
      return this.val$protocol;
   }

   @Override
   public PacketFlow flow() {
      return this.val$flow;
   }

   @Override
   public void listPackets(final ProtocolInfo$Details$PacketVisitor output) {
      for (int i = 0; i < this.val$codecs.size(); i++) {
         ProtocolInfoBuilder$CodecEntry<?, ?, ?, ?> entry = (ProtocolInfoBuilder$CodecEntry<?, ?, ?, ?>)this.val$codecs.get(i);
         output.accept(entry.type, i);
      }
   }
}
