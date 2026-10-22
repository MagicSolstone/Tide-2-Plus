package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public record ServerboundCommandSuggestionPacket(int id, String command) implements Packet<ServerGamePacketListener> {
   public static final StreamCodec<ByteBuf, ServerboundCommandSuggestionPacket> STREAM_CODEC = createStreamCodec(32500);
   public static final StreamCodec<ByteBuf, ServerboundCommandSuggestionPacket> CHAT_ONLY_STREAM_CODEC = new ServerboundCommandSuggestionPacket$1();

   private static StreamCodec<ByteBuf, ServerboundCommandSuggestionPacket> createStreamCodec(final int maxLength) {
      return StreamCodec.composite(
         ByteBufCodecs.VAR_INT,
         ServerboundCommandSuggestionPacket::id,
         ByteBufCodecs.stringUtf8(maxLength),
         ServerboundCommandSuggestionPacket::command,
         ServerboundCommandSuggestionPacket::new
      );
   }

   @Override
   public PacketType<ServerboundCommandSuggestionPacket> type() {
      return GamePacketTypes.SERVERBOUND_COMMAND_SUGGESTION;
   }

   public void handle(final ServerGamePacketListener listener) {
      listener.handleCustomCommandSuggestions(this);
   }
}
