package net.minecraft.network.protocol;

import java.util.function.Consumer;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

class BundlerInfo$1 implements BundlerInfo {
   BundlerInfo$1(final PacketType val$bundlePacketType, final BundleDelimiterPacket val$delimiterPacket, final Function val$constructor) {
      this.val$bundlePacketType = val$bundlePacketTypex;
      this.val$delimiterPacket = val$delimiterPacketx;
      this.val$constructor = val$constructorx;
   }

   @Override
   public void unbundlePacket(final Packet<?> packet, final Consumer<Packet<?>> output) {
      if (packet.type() == this.val$bundlePacketType) {
         P bundlerPacket = (P)((BundlePacket)packet);
         output.accept(this.val$delimiterPacket);
         bundlerPacket.subPackets().forEach(output);
         output.accept(this.val$delimiterPacket);
      } else {
         output.accept(packet);
      }
   }

   @Nullable
   @Override
   public BundlerInfo$Bundler startPacketBundling(final Packet<?> packet) {
      return packet == this.val$delimiterPacket ? new BundlerInfo$1$1(this, this.val$delimiterPacket, this.val$constructor) : null;
   }
}
