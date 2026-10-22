package net.minecraft.network.protocol.common.custom;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamMemberEncoder;
import net.minecraft.resources.Identifier;

public interface CustomPacketPayload {
   CustomPacketPayload$Type<? extends CustomPacketPayload> type();

   static <B extends ByteBuf, T extends CustomPacketPayload> StreamCodec<B, T> codec(final StreamMemberEncoder<B, T> writer, final StreamDecoder<B, T> reader) {
      return StreamCodec.ofMember(writer, reader);
   }

   static <T extends CustomPacketPayload> CustomPacketPayload$Type<T> createType(final String id) {
      return new CustomPacketPayload$Type<>(Identifier.withDefaultNamespace(id));
   }

   static <B extends FriendlyByteBuf> StreamCodec<B, CustomPacketPayload> codec(
      final CustomPacketPayload$FallbackProvider<B> fallback, final List<CustomPacketPayload$TypeAndCodec<? super B, ?>> types
   ) {
      Map<Identifier, StreamCodec<? super B, ? extends CustomPacketPayload>> idToType = types.stream()
         .collect(Collectors.toUnmodifiableMap(t -> t.type().id(), CustomPacketPayload$TypeAndCodec::codec));
      return new CustomPacketPayload$1(idToType, fallback);
   }
}
