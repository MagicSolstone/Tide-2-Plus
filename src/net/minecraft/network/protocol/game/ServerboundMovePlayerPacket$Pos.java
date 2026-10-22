package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.world.phys.Vec3;

public class ServerboundMovePlayerPacket$Pos extends ServerboundMovePlayerPacket {
   public static final StreamCodec<FriendlyByteBuf, ServerboundMovePlayerPacket$Pos> STREAM_CODEC = Packet.codec(
      ServerboundMovePlayerPacket$Pos::write, ServerboundMovePlayerPacket$Pos::read
   );

   public ServerboundMovePlayerPacket$Pos(final Vec3 pos, final boolean onGround, final boolean horizontalCollision) {
      super(pos.x, pos.y, pos.z, 0.0F, 0.0F, onGround, horizontalCollision, true, false);
   }

   public ServerboundMovePlayerPacket$Pos(final double x, final double y, final double z, final boolean onGround, final boolean horizontalCollision) {
      super(x, y, z, 0.0F, 0.0F, onGround, horizontalCollision, true, false);
   }

   private static ServerboundMovePlayerPacket$Pos read(final FriendlyByteBuf input) {
      double x = input.readDouble();
      double y = input.readDouble();
      double z = input.readDouble();
      short flags = input.readUnsignedByte();
      boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
      boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
      return new ServerboundMovePlayerPacket$Pos(x, y, z, onGround, horizontalCollision);
   }

   private void write(final FriendlyByteBuf output) {
      output.writeDouble(this.x);
      output.writeDouble(this.y);
      output.writeDouble(this.z);
      output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
   }

   @Override
   public PacketType<ServerboundMovePlayerPacket$Pos> type() {
      return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS;
   }
}
