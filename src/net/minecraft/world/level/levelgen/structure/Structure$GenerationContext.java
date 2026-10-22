package net.minecraft.world.level.levelgen.structure;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate$Sampler;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public record Structure$GenerationContext(
   RegistryAccess registryAccess,
   ChunkGenerator chunkGenerator,
   BiomeSource biomeSource,
   Climate$Sampler climateSampler,
   BiomeResolver biomeResolver,
   RandomState randomState,
   StructureTemplateManager structureTemplateManager,
   WorldgenRandom random,
   long seed,
   ChunkPos chunkPos,
   LevelHeightAccessor heightAccessor,
   Predicate<Holder<Biome>> validBiome
) {
   public Structure$GenerationContext(
      final RegistryAccess registryAccess,
      final ChunkGenerator chunkGenerator,
      final BiomeSource biomeSource,
      final Climate$Sampler climateSampler,
      final RandomState randomState,
      final StructureTemplateManager structureTemplateManager,
      final long seed,
      final ChunkPos chunkPos,
      final LevelHeightAccessor heightAccessor,
      final Predicate<Holder<Biome>> validBiome
   ) {
      this(
         registryAccess,
         chunkGenerator,
         biomeSource,
         climateSampler,
         biomeSource.createResolver(climateSampler),
         randomState,
         structureTemplateManager,
         makeRandom(seed, chunkPos),
         seed,
         chunkPos,
         heightAccessor,
         validBiome
      );
   }

   private static WorldgenRandom makeRandom(final long seed, final ChunkPos chunkPos) {
      WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
      random.setLargeFeatureSeed(seed, chunkPos.x(), chunkPos.z());
      return random;
   }

   public boolean isValidBiome(final Structure$GenerationStub stub) {
      BlockPos startPos = stub.position();
      return this.validBiome
         .test(this.biomeResolver.getNoiseBiome(QuartPos.fromBlock(startPos.getX()), QuartPos.fromBlock(startPos.getY()), QuartPos.fromBlock(startPos.getZ())));
   }

   public boolean couldStructureExistInColumn(final int blockX, final int blockZ, final int minBlockY, final int maxBlockY) {
      int quartX = QuartPos.fromBlock(blockX);
      int quartZ = QuartPos.fromBlock(blockZ);
      int minQuartY = QuartPos.fromBlock(minBlockY);
      int maxQuartY = QuartPos.fromBlock(maxBlockY);
      BiomeResolver columnResolver = this.biomeSource.createResolverForChunk(this.climateSampler, quartX, minQuartY, quartZ, 1, maxQuartY - minQuartY + 1, 1);

      for (int quartY = minQuartY; quartY <= maxQuartY; quartY++) {
         if (this.validBiome.test(columnResolver.getNoiseBiome(quartX, quartY, quartZ))) {
            return true;
         }
      }

      return false;
   }

   public boolean couldValidBiomeExistInTerrainColumn(final int blockX, final int blockZ) {
      return this.couldStructureExistInColumn(blockX, blockZ, this.heightAccessor.getMinY() - 1, this.heightAccessor.getMaxY());
   }

   public boolean couldValidBiomeExistOnTopOfChunkCenter() {
      return this.couldValidBiomeExistInTerrainColumn(this.chunkPos.getMiddleBlockX(), this.chunkPos.getMiddleBlockZ());
   }
}
