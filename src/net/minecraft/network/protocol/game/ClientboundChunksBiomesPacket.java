package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.world.level.chunk.LevelChunk;

public record ClientboundChunksBiomesPacket(List<ClientboundChunksBiomesPacket$ChunkBiomeData> chunkBiomeData) implements Packet<ClientGamePacketListener> {
   public static final StreamCodec<ByteBuf, ClientboundChunksBiomesPacket> STREAM_CODEC = StreamCodec.composite(
      ClientboundChunksBiomesPacket$ChunkBiomeData.STREAM_CODEC.apply(ByteBufCodecs.list()),
      ClientboundChunksBiomesPacket::chunkBiomeData,
      ClientboundChunksBiomesPacket::new
   );
   private static final int TWO_MEGABYTES = 2097152;

   public static ClientboundChunksBiomesPacket forChunks(final List<LevelChunk> chunks) {
      return new ClientboundChunksBiomesPacket(chunks.stream().map(ClientboundChunksBiomesPacket$ChunkBiomeData::new).toList());
   }

   @Override
   public PacketType<ClientboundChunksBiomesPacket> type() {
      return GamePacketTypes.CLIENTBOUND_CHUNKS_BIOMES;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleChunksBiomes(this);
   }
}
