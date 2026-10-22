package net.minecraft.network.protocol;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.PacketListener;
import org.jspecify.annotations.Nullable;

public interface BundlerInfo {
   int BUNDLE_SIZE_LIMIT = 4096;

   static <T extends PacketListener, P extends BundlePacket<? super T>> BundlerInfo createForPacket(
      final PacketType<P> bundlePacketType, final Function<Iterable<Packet<? super T>>, P> constructor, final BundleDelimiterPacket<? super T> delimiterPacket
   ) {
      return new BundlerInfo$1(bundlePacketType, delimiterPacket, constructor);
   }

   void unbundlePacket(Packet<?> packet, Consumer<Packet<?>> output);

   @Nullable
   BundlerInfo$Bundler startPacketBundling(Packet<?> packet);
}
