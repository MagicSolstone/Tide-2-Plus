package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MessageSignature$Packed;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public record ClientboundDeleteChatPacket(MessageSignature$Packed messageSignature) implements Packet<ClientGamePacketListener> {
   public static final StreamCodec<ByteBuf, ClientboundDeleteChatPacket> STREAM_CODEC = StreamCodec.composite(
      MessageSignature$Packed.STREAM_CODEC, ClientboundDeleteChatPacket::messageSignature, ClientboundDeleteChatPacket::new
   );

   @Override
   public PacketType<ClientboundDeleteChatPacket> type() {
      return GamePacketTypes.CLIENTBOUND_DELETE_CHAT;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleDeleteChat(this);
   }
}
