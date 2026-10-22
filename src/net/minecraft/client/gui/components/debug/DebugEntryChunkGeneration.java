package net.minecraft.client.gui.components.debug;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate$Sampler;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugEntryChunkGeneration implements DebugScreenEntry {
   private static final Identifier GROUP = Identifier.withDefaultNamespace("chunk_generation");
   private final List<String> result = new ArrayList<>();
   @Nullable
   private BlockPos lastPos = null;

   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      Minecraft minecraft = Minecraft.getInstance();
      Entity entity = minecraft.getCameraEntity();
      ServerLevel serverLevel = serverOrClientLevel instanceof ServerLevel level ? level : null;
      if (entity != null && serverLevel != null) {
         BlockPos feetPos = entity.blockPosition();
         if (!feetPos.equals(this.lastPos)) {
            this.update(serverChunk, feetPos, serverLevel);
         }

         displayer.addToGroup(GROUP, this.result);
      }
   }

   private void update(@Nullable final LevelChunk serverChunk, final BlockPos feetPos, final ServerLevel serverLevel) {
      this.result.clear();
      this.lastPos = feetPos;
      ServerChunkCache chunkSource = serverLevel.getChunkSource();
      SamplerContext samplerContext = SamplerContext.builder().enableCaches().build();
      ChunkGenerator generator = chunkSource.getGenerator();
      RandomState randomState = chunkSource.randomState();
      generator.addDebugScreenInfo(this.result, randomState, feetPos, samplerContext);
      Climate$Sampler sampler = randomState.createClimateSampler(samplerContext);
      BiomeSource biomeSource = generator.getBiomeSource();
      biomeSource.addDebugInfo(this.result, feetPos, sampler);
      if (serverChunk != null && serverChunk.isOldNoiseGeneration()) {
         this.result.add("Blending: Old");
      }
   }
}
