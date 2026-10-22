package net.minecraft.network.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

class BundlerInfo$1$1 implements BundlerInfo$Bundler {
   private final List<Packet<? super T>> bundlePackets;

   BundlerInfo$1$1(final BundlerInfo$1 this$0, final BundleDelimiterPacket val$delimiterPacket, final Function val$constructor) {
      this.val$delimiterPacket = val$delimiterPacketx;
      this.val$constructor = val$constructorx;
      Objects.requireNonNull(this$0);
      super();
      this.bundlePackets = new ArrayList<>();
   }

   @Nullable
   @Override
   public Packet<?> addPacket(final Packet<?> packet) {
      if (packet == this.val$delimiterPacket) {
         return (Packet<?>)this.val$constructor.apply(this.bundlePackets);
      } else if (this.bundlePackets.size() >= 4096) {
         throw new IllegalStateException("Too many packets in a bundle");
      } else {
         this.bundlePackets.add(packet);
         return null;
      }
   }
}
