package net.minecraft.client.renderer.debug;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

@Environment(EnvType.CLIENT)
final class ChunkDebugRenderer$ChunkData {
   private final Map<ChunkPos, String> clientData;
   private final CompletableFuture<Map<ChunkPos, String>> serverData;

   private ChunkDebugRenderer$ChunkData(final ChunkDebugRenderer this$0, final IntegratedServer server, final double camX, final double camZ) {
      Objects.requireNonNull(this$0);
      super();
      ClientLevel clientLevel = this$0.minecraft.level;
      ResourceKey<Level> dimension = clientLevel.dimension();
      int cx = SectionPos.posToSectionCoord(camX);
      int cz = SectionPos.posToSectionCoord(camZ);
      Builder<ChunkPos, String> builder = ImmutableMap.builder();
      ClientChunkCache clientChunkSource = clientLevel.getChunkSource();

      for (int x = cx - 12; x <= cx + 12; x++) {
         for (int z = cz - 12; z <= cz + 12; z++) {
            ChunkPos pos = new ChunkPos(x, z);
            String result = "";
            LevelChunk clientChunk = clientChunkSource.getChunk(x, z, false);
            result = result + "Client: ";
            if (clientChunk == null) {
               result = result + "0n/a\n";
            } else {
               result = result + (clientChunk.isEmpty() ? " E" : "");
               result = result + "\n";
            }

            builder.put(pos, result);
         }
      }

      this.clientData = builder.build();
      this.serverData = server.submit(() -> {
         ServerLevel serverLevel = server.getLevel(dimension);
         if (serverLevel == null) {
            return ImmutableMap.of();
         } else {
            Builder<ChunkPos, String> serverBuilder = ImmutableMap.builder();
            ServerChunkCache serverChunkSource = serverLevel.getChunkSource();

            for (int xx = cx - 12; xx <= cx + 12; xx++) {
               for (int zx = cz - 12; zx <= cz + 12; zx++) {
                  ChunkPos posx = new ChunkPos(xx, zx);
                  serverBuilder.put(posx, "Server: " + serverChunkSource.getChunkDebugData(posx));
               }
            }

            return serverBuilder.build();
         }
      });
   }
}
