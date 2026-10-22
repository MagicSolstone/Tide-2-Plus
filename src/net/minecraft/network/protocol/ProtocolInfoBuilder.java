package net.minecraft.network.protocol;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.ClientboundPacketListener;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.ProtocolInfo$Details;
import net.minecraft.network.ServerboundPacketListener;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import org.jspecify.annotations.Nullable;

public class ProtocolInfoBuilder<T extends PacketListener, B extends ByteBuf, C> {
   private final ConnectionProtocol protocol;
   private final PacketFlow flow;
   private final List<ProtocolInfoBuilder$CodecEntry<T, ?, B, C>> codecs = new ArrayList<>();
   @Nullable
   private BundlerInfo bundlerInfo;

   public ProtocolInfoBuilder(final ConnectionProtocol protocol, final PacketFlow flow) {
      this.protocol = protocol;
      this.flow = flow;
   }

   public <P extends Packet<? super T>> ProtocolInfoBuilder<T, B, C> addPacket(final PacketType<P> type, final StreamCodec<? super B, P> serializer) {
      this.codecs.add(new ProtocolInfoBuilder$CodecEntry<>(type, serializer, null));
      return this;
   }

   public <P extends Packet<? super T>> ProtocolInfoBuilder<T, B, C> addPacket(
      final PacketType<P> type, final StreamCodec<? super B, P> serializer, final CodecModifier<B, P, C> modifier
   ) {
      this.codecs.add(new ProtocolInfoBuilder$CodecEntry<>(type, serializer, modifier));
      return this;
   }

   public <P extends BundlePacket<? super T>, D extends BundleDelimiterPacket<? super T>> ProtocolInfoBuilder<T, B, C> withBundlePacket(
      final PacketType<P> bundlerPacket, final Function<Iterable<Packet<? super T>>, P> constructor, final D delimiterPacket
   ) {
      StreamCodec<ByteBuf, D> delimitedCodec = StreamCodec.unit(delimiterPacket);
      PacketType<D> delimiterType = (PacketType<D>)delimiterPacket.type();
      this.codecs.add(new ProtocolInfoBuilder$CodecEntry<>(delimiterType, delimitedCodec, null));
      this.bundlerInfo = BundlerInfo.createForPacket(bundlerPacket, constructor, delimiterPacket);
      return this;
   }

   private StreamCodec<ByteBuf, Packet<? super T>> buildPacketCodec(
      final Function<ByteBuf, B> contextWrapper, final List<ProtocolInfoBuilder$CodecEntry<T, ?, B, C>> codecs, final C context
   ) {
      ProtocolCodecBuilder<ByteBuf, T> codecBuilder = new ProtocolCodecBuilder<>(this.flow);

      for (ProtocolInfoBuilder$CodecEntry<T, ?, B, C> codec : codecs) {
         codec.addToBuilder(codecBuilder, contextWrapper, context);
      }

      return codecBuilder.build();
   }

   private static ProtocolInfo$Details buildDetails(
      final ConnectionProtocol protocol, final PacketFlow flow, final List<? extends ProtocolInfoBuilder$CodecEntry<?, ?, ?, ?>> codecs
   ) {
      return new ProtocolInfoBuilder$1(protocol, flow, codecs);
   }

   public SimpleUnboundProtocol<T, B> buildUnbound(final C context) {
      List<ProtocolInfoBuilder$CodecEntry<T, ?, B, C>> codecs = List.copyOf(this.codecs);
      BundlerInfo bundlerInfo = this.bundlerInfo;
      ProtocolInfo$Details details = buildDetails(this.protocol, this.flow, codecs);
      return new ProtocolInfoBuilder$2(this, codecs, context, bundlerInfo, details);
   }

   public UnboundProtocol<T, B, C> buildUnbound() {
      List<ProtocolInfoBuilder$CodecEntry<T, ?, B, C>> codecs = List.copyOf(this.codecs);
      BundlerInfo bundlerInfo = this.bundlerInfo;
      ProtocolInfo$Details details = buildDetails(this.protocol, this.flow, codecs);
      return new ProtocolInfoBuilder$3(this, codecs, bundlerInfo, details);
   }

   private static <L extends PacketListener, B extends ByteBuf> SimpleUnboundProtocol<L, B> protocol(
      final ConnectionProtocol id, final PacketFlow flow, final Consumer<ProtocolInfoBuilder<L, B, Unit>> config
   ) {
      ProtocolInfoBuilder<L, B, Unit> builder = new ProtocolInfoBuilder<>(id, flow);
      config.accept(builder);
      return builder.buildUnbound(Unit.INSTANCE);
   }

   public static <T extends ServerboundPacketListener, B extends ByteBuf> SimpleUnboundProtocol<T, B> serverboundProtocol(
      final ConnectionProtocol id, final Consumer<ProtocolInfoBuilder<T, B, Unit>> config
   ) {
      return protocol(id, PacketFlow.SERVERBOUND, config);
   }

   public static <T extends ClientboundPacketListener, B extends ByteBuf> SimpleUnboundProtocol<T, B> clientboundProtocol(
      final ConnectionProtocol id, final Consumer<ProtocolInfoBuilder<T, B, Unit>> config
   ) {
      return protocol(id, PacketFlow.CLIENTBOUND, config);
   }

   private static <L extends PacketListener, B extends ByteBuf, C> UnboundProtocol<L, B, C> contextProtocol(
      final ConnectionProtocol id, final PacketFlow flow, final Consumer<ProtocolInfoBuilder<L, B, C>> config
   ) {
      ProtocolInfoBuilder<L, B, C> builder = new ProtocolInfoBuilder<>(id, flow);
      config.accept(builder);
      return builder.buildUnbound();
   }

   public static <T extends ServerboundPacketListener, B extends ByteBuf, C> UnboundProtocol<T, B, C> contextServerboundProtocol(
      final ConnectionProtocol id, final Consumer<ProtocolInfoBuilder<T, B, C>> config
   ) {
      return contextProtocol(id, PacketFlow.SERVERBOUND, config);
   }

   public static <T extends ClientboundPacketListener, B extends ByteBuf, C> UnboundProtocol<T, B, C> contextClientboundProtocol(
      final ConnectionProtocol id, final Consumer<ProtocolInfoBuilder<T, B, C>> config
   ) {
      return contextProtocol(id, PacketFlow.CLIENTBOUND, config);
   }
}
