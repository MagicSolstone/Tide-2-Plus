package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public class ServerboundMovePlayerPacket$StatusOnly extends ServerboundMovePlayerPacket {
   public static final StreamCodec<FriendlyByteBuf, ServerboundMovePlayerPacket$StatusOnly> STREAM_CODEC = Packet.codec(
      ServerboundMovePlayerPacket$StatusOnly::write, ServerboundMovePlayerPacket$StatusOnly::read
   );

   public ServerboundMovePlayerPacket$StatusOnly(final boolean onGround, final boolean horizontalCollision) {
      super(0.0, 0.0, 0.0, 0.0F, 0.0F, onGround, horizontalCollision, false, false);
   }

   private static ServerboundMovePlayerPacket$StatusOnly read(final FriendlyByteBuf input) {
      short flags = input.readUnsignedByte();
      boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
      boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
      return new ServerboundMovePlayerPacket$StatusOnly(onGround, horizontalCollision);
   }

   private void write(final FriendlyByteBuf output) {
      output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
   }

   @Override
   public PacketType<ServerboundMovePlayerPacket$StatusOnly> type() {
      return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_STATUS_ONLY;
   }
}
