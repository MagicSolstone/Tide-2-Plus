package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public record ServerboundSetGameRulePacket(List<ServerboundSetGameRulePacket$Entry> entries) implements Packet<ServerGamePacketListener> {
   public static final StreamCodec<ByteBuf, ServerboundSetGameRulePacket> STREAM_CODEC = StreamCodec.composite(
      ServerboundSetGameRulePacket$Entry.STREAM_CODEC.apply(ByteBufCodecs.list()), ServerboundSetGameRulePacket::entries, ServerboundSetGameRulePacket::new
   );

   @Override
   public PacketType<ServerboundSetGameRulePacket> type() {
      return GamePacketTypes.SERVERBOUND_SET_GAME_RULE;
   }

   public void handle(final ServerGamePacketListener listener) {
      listener.handleSetGameRule(this);
   }
}
