package net.minecraft.network.protocol.game;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.TestInstanceBlockEntity$Data;
import net.minecraft.world.level.block.entity.TestInstanceBlockEntity$Status;

public record ServerboundTestInstanceBlockActionPacket(BlockPos pos, ServerboundTestInstanceBlockActionPacket$Action action, TestInstanceBlockEntity$Data data)
   implements Packet<ServerGamePacketListener> {
   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundTestInstanceBlockActionPacket> STREAM_CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC,
      ServerboundTestInstanceBlockActionPacket::pos,
      ServerboundTestInstanceBlockActionPacket$Action.STREAM_CODEC,
      ServerboundTestInstanceBlockActionPacket::action,
      TestInstanceBlockEntity$Data.STREAM_CODEC,
      ServerboundTestInstanceBlockActionPacket::data,
      ServerboundTestInstanceBlockActionPacket::new
   );

   public ServerboundTestInstanceBlockActionPacket(
      final BlockPos pos,
      final ServerboundTestInstanceBlockActionPacket$Action action,
      final Optional<ResourceKey<GameTestInstance>> test,
      final Vec3i size,
      final Rotation rotation,
      final boolean ignoreEntities
   ) {
      this(pos, action, new TestInstanceBlockEntity$Data(test, size, rotation, ignoreEntities, TestInstanceBlockEntity$Status.CLEARED, Optional.empty()));
   }

   @Override
   public PacketType<ServerboundTestInstanceBlockActionPacket> type() {
      return GamePacketTypes.SERVERBOUND_TEST_INSTANCE_BLOCK_ACTION;
   }

   public void handle(final ServerGamePacketListener listener) {
      listener.handleTestInstanceBlockAction(this);
   }
}
