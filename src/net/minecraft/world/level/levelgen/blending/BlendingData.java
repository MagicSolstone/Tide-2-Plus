package net.minecraft.world.level.levelgen.blending;

import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.CompositeDirection$Direction8;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import org.jspecify.annotations.Nullable;

public class BlendingData {
   private static final float BLENDING_DENSITY_FACTOR = 0.1F;
   protected static final int CELL_WIDTH = 4;
   protected static final int CELL_HEIGHT = 8;
   protected static final int CELL_RATIO = 2;
   private static final float SOLID_DENSITY = 1.0F;
   private static final float AIR_DENSITY = -1.0F;
   private static final int CELLS_PER_SECTION_Y = 2;
   private static final int QUARTS_PER_SECTION = QuartPos.fromBlock(16);
   private static final int CELL_HORIZONTAL_MAX_INDEX_INSIDE = QUARTS_PER_SECTION - 1;
   private static final int CELL_HORIZONTAL_MAX_INDEX_OUTSIDE = QUARTS_PER_SECTION;
   private static final int CELL_COLUMN_INSIDE_COUNT = 2 * CELL_HORIZONTAL_MAX_INDEX_INSIDE + 1;
   private static final int CELL_COLUMN_OUTSIDE_COUNT = 2 * CELL_HORIZONTAL_MAX_INDEX_OUTSIDE + 1;
   private static final int CELL_COLUMN_COUNT = CELL_COLUMN_INSIDE_COUNT + CELL_COLUMN_OUTSIDE_COUNT;
   private final LevelHeightAccessor areaWithOldGeneration;
   private static final List<Block> SURFACE_BLOCKS = List.of(
      Blocks.PODZOL,
      Blocks.GRAVEL,
      Blocks.GRASS_BLOCK,
      Blocks.STONE,
      Blocks.COARSE_DIRT,
      Blocks.SAND,
      Blocks.RED_SAND,
      Blocks.MYCELIUM,
      Blocks.SNOW_BLOCK,
      Blocks.TERRACOTTA,
      Blocks.DIRT
   );
   protected static final float NO_VALUE = Float.MAX_VALUE;
   private boolean hasCalculatedData;
   private final float[] heights;
   private final List<List<Holder<Biome>>> biomes;
   private final transient float[][] densities;

   private BlendingData(final int minSection, final int maxSection, final Optional<float[]> heights) {
      this.heights = heights.orElseGet(() -> Util.make(new float[CELL_COLUMN_COUNT], i -> Arrays.fill(i, Float.MAX_VALUE)));
      this.densities = new float[CELL_COLUMN_COUNT][];
      ObjectArrayList<List<Holder<Biome>>> biomes = new ObjectArrayList(CELL_COLUMN_COUNT);
      biomes.size(CELL_COLUMN_COUNT);
      this.biomes = biomes;
      int minY = SectionPos.sectionToBlockCoord(minSection);
      int height = SectionPos.sectionToBlockCoord(maxSection) - minY;
      this.areaWithOldGeneration = LevelHeightAccessor.create(minY, height);
   }

   @Nullable
   public static BlendingData unpack(@Nullable final BlendingData$Packed packed) {
      return packed == null ? null : new BlendingData(packed.minSection(), packed.maxSection(), packed.heights());
   }

   public BlendingData$Packed pack() {
      boolean hasHeight = false;

      for (float height : this.heights) {
         if (height != Float.MAX_VALUE) {
            hasHeight = true;
            break;
         }
      }

      return new BlendingData$Packed(
         this.areaWithOldGeneration.getMinSectionY(),
         this.areaWithOldGeneration.getMaxSectionY() + 1,
         hasHeight ? Optional.of(FloatArrays.copy(this.heights)) : Optional.empty()
      );
   }

   @Nullable
   public static BlendingData getOrUpdateBlendingData(final WorldGenRegion region, final int chunkX, final int chunkZ) {
      ChunkAccess chunk = region.getChunk(chunkX, chunkZ);
      BlendingData blendingData = chunk.getBlendingData();
      if (blendingData != null && !chunk.getHighestGeneratedStatus().isBefore(ChunkStatus.BIOMES)) {
         blendingData.calculateData(chunk, sideByGenerationAge(region, chunkX, chunkZ, false));
         return blendingData;
      } else {
         return null;
      }
   }

   public static Set<CompositeDirection$Direction8> sideByGenerationAge(
      final WorldGenLevel region, final int chunkX, final int chunkZ, final boolean wantedOldGen
   ) {
      Set<CompositeDirection$Direction8> sides = EnumSet.noneOf(CompositeDirection$Direction8.class);

      for (CompositeDirection$Direction8 direction8 : CompositeDirection$Direction8.values()) {
         int testChunkX = chunkX + direction8.getStepX();
         int testChunkZ = chunkZ + direction8.getStepZ();
         if (region.getChunk(testChunkX, testChunkZ).isOldNoiseGeneration() == wantedOldGen) {
            sides.add(direction8);
         }
      }

      return sides;
   }

   private void calculateData(final ChunkAccess chunk, final Set<CompositeDirection$Direction8> newSides) {
      if (!this.hasCalculatedData) {
         if (newSides.contains(CompositeDirection$Direction8.NORTH)
            || newSides.contains(CompositeDirection$Direction8.WEST)
            || newSides.contains(CompositeDirection$Direction8.NORTH_WEST)) {
            this.addValuesForColumn(getInsideIndex(0, 0), chunk, 0, 0);
         }

         if (newSides.contains(CompositeDirection$Direction8.NORTH)) {
            for (int i = 1; i < QUARTS_PER_SECTION; i++) {
               this.addValuesForColumn(getInsideIndex(i, 0), chunk, 4 * i, 0);
            }
         }

         if (newSides.contains(CompositeDirection$Direction8.WEST)) {
            for (int i = 1; i < QUARTS_PER_SECTION; i++) {
               this.addValuesForColumn(getInsideIndex(0, i), chunk, 0, 4 * i);
            }
         }

         if (newSides.contains(CompositeDirection$Direction8.EAST)) {
            for (int i = 1; i < QUARTS_PER_SECTION; i++) {
               this.addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, i), chunk, 15, 4 * i);
            }
         }

         if (newSides.contains(CompositeDirection$Direction8.SOUTH)) {
            for (int i = 0; i < QUARTS_PER_SECTION; i++) {
               this.addValuesForColumn(getOutsideIndex(i, CELL_HORIZONTAL_MAX_INDEX_OUTSIDE), chunk, 4 * i, 15);
            }
         }

         if (newSides.contains(CompositeDirection$Direction8.EAST) && newSides.contains(CompositeDirection$Direction8.NORTH_EAST)) {
            this.addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, 0), chunk, 15, 0);
         }

         if (newSides.contains(CompositeDirection$Direction8.EAST)
            && newSides.contains(CompositeDirection$Direction8.SOUTH)
            && newSides.contains(CompositeDirection$Direction8.SOUTH_EAST)) {
            this.addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, CELL_HORIZONTAL_MAX_INDEX_OUTSIDE), chunk, 15, 15);
         }

         this.hasCalculatedData = true;
      }
   }

   private void addValuesForColumn(final int index, final ChunkAccess chunk, final int blockX, final int blockZ) {
      if (this.heights[index] == Float.MAX_VALUE) {
         this.heights[index] = this.getHeightAtXZ(chunk, blockX, blockZ);
      }

      this.densities[index] = this.getDensityColumn(chunk, blockX, blockZ, Mth.floor(this.heights[index]));
      this.biomes.set(index, this.getBiomeColumn(chunk, blockX, blockZ));
   }

   private int getHeightAtXZ(final ChunkAccess chunk, final int blockX, final int blockZ) {
      int height;
      if (chunk.hasPrimedHeightmap(Heightmap$Types.WORLD_SURFACE_WG)) {
         height = Math.min(chunk.getHeight(Heightmap$Types.WORLD_SURFACE_WG, blockX, blockZ), this.areaWithOldGeneration.getMaxY());
      } else {
         height = this.areaWithOldGeneration.getMaxY();
      }

      int minY = this.areaWithOldGeneration.getMinY();
      BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos(blockX, height, blockZ);

      while (pos.getY() > minY) {
         if (SURFACE_BLOCKS.contains(chunk.getBlockState(pos).getBlock())) {
            return pos.getY();
         }

         pos.move(Direction.DOWN);
      }

      return minY;
   }

   private static float read1(final ChunkAccess chunk, final BlockPos$MutableBlockPos pos) {
      return isGround(chunk, pos.move(Direction.DOWN)) ? 1.0F : -1.0F;
   }

   private static float read7(final ChunkAccess chunk, final BlockPos$MutableBlockPos pos) {
      float sum = 0.0F;

      for (int i = 0; i < 7; i++) {
         sum += read1(chunk, pos);
      }

      return sum;
   }

   private float[] getDensityColumn(final ChunkAccess chunk, final int x, final int z, final int height) {
      float[] densities = new float[this.cellCountPerColumn()];
      Arrays.fill(densities, -1.0F);
      BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos(x, this.areaWithOldGeneration.getMaxY() + 1, z);
      float last7 = read7(chunk, pos);

      for (int cellIndex = densities.length - 2; cellIndex >= 0; cellIndex--) {
         float one = read1(chunk, pos);
         float current7 = read7(chunk, pos);
         densities[cellIndex] = (last7 + one + current7) / 15.0F;
         last7 = current7;
      }

      int highestCellWithSurfaceIndex = this.getCellYIndex(Mth.floorDiv(height, 8));
      if (highestCellWithSurfaceIndex >= 0 && highestCellWithSurfaceIndex < densities.length - 1) {
         float inCellIndex = (height + 0.5F) % 8.0F / 8.0F;
         float amplitudeAboveToMakeSurfaceBeAtHeight = (1.0F - inCellIndex) / inCellIndex;
         float max = Math.max(amplitudeAboveToMakeSurfaceBeAtHeight, 1.0F) * 0.25F;
         densities[highestCellWithSurfaceIndex + 1] = -amplitudeAboveToMakeSurfaceBeAtHeight / max;
         densities[highestCellWithSurfaceIndex] = 1.0F / max;
      }

      return densities;
   }

   private List<Holder<Biome>> getBiomeColumn(final ChunkAccess chunk, final int blockX, final int blockZ) {
      ObjectArrayList<Holder<Biome>> biomes = new ObjectArrayList(this.quartCountPerColumn());
      biomes.size(this.quartCountPerColumn());

      for (int quartIndex = 0; quartIndex < biomes.size(); quartIndex++) {
         int quartY = quartIndex + QuartPos.fromBlock(this.areaWithOldGeneration.getMinY());
         biomes.set(quartIndex, chunk.getNoiseBiome(QuartPos.fromBlock(blockX), quartY, QuartPos.fromBlock(blockZ)));
      }

      return biomes;
   }

   private static boolean isGround(final ChunkAccess chunk, final BlockPos pos) {
      BlockState state = chunk.getBlockState(pos);
      if (state.isAir()) {
         return false;
      } else if (state.is(BlockTags.LEAVES)) {
         return false;
      } else if (state.is(BlockTags.LOGS)) {
         return false;
      } else {
         return state.is(Blocks.BROWN_MUSHROOM_BLOCK) || state.is(Blocks.RED_MUSHROOM_BLOCK) ? false : !state.getCollisionShape(chunk, pos).isEmpty();
      }
   }

   protected float getHeight(final int cellX, final int cellY, final int cellZ) {
      if (cellX == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE || cellZ == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE) {
         return this.heights[getOutsideIndex(cellX, cellZ)];
      } else {
         return cellX != 0 && cellZ != 0 ? Float.MAX_VALUE : this.heights[getInsideIndex(cellX, cellZ)];
      }
   }

   private float getDensity(@Nullable final float[] densityColumn, final int cellY) {
      if (densityColumn == null) {
         return Float.MAX_VALUE;
      } else {
         int yIndex = this.getCellYIndex(cellY);
         return yIndex >= 0 && yIndex < densityColumn.length ? densityColumn[yIndex] * 0.1F : Float.MAX_VALUE;
      }
   }

   protected float getDensity(final int cellX, final int cellY, final int cellZ) {
      if (cellY == this.getMinY()) {
         return 0.1F;
      } else if (cellX == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE || cellZ == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE) {
         return this.getDensity(this.densities[getOutsideIndex(cellX, cellZ)], cellY);
      } else {
         return cellX != 0 && cellZ != 0 ? Float.MAX_VALUE : this.getDensity(this.densities[getInsideIndex(cellX, cellZ)], cellY);
      }
   }

   protected void iterateBiomes(final int minCellX, final int quartY, final int minCellZ, final BlendingData$BiomeConsumer biomeConsumer) {
      if (quartY >= QuartPos.fromBlock(this.areaWithOldGeneration.getMinY()) && quartY <= QuartPos.fromBlock(this.areaWithOldGeneration.getMaxY())) {
         int quartIndex = quartY - QuartPos.fromBlock(this.areaWithOldGeneration.getMinY());

         for (int i = 0; i < this.biomes.size(); i++) {
            List<Holder<Biome>> biomeCell = this.biomes.get(i);
            if (biomeCell != null) {
               Holder<Biome> value = biomeCell.get(quartIndex);
               if (value != null) {
                  biomeConsumer.consume(minCellX + getX(i), minCellZ + getZ(i), value);
               }
            }
         }
      }
   }

   protected void iterateHeights(final int minCellX, final int minCellZ, final BlendingData$HeightConsumer heightConsumer) {
      for (int i = 0; i < this.heights.length; i++) {
         float value = this.heights[i];
         if (value != Float.MAX_VALUE) {
            heightConsumer.consume(minCellX + getX(i), minCellZ + getZ(i), value);
         }
      }
   }

   protected void iterateDensities(
      final int minCellX, final int minCellZ, final int fromCellY, final int toCellY, final BlendingData$DensityConsumer densityConsumer
   ) {
      int minCellY = this.getColumnMinY();
      int minYIndex = Math.max(0, fromCellY - minCellY);
      int maxYIndex = Math.min(this.cellCountPerColumn(), toCellY - minCellY);

      for (int i = 0; i < this.densities.length; i++) {
         float[] densityColumn = this.densities[i];
         if (densityColumn != null) {
            int testCellX = minCellX + getX(i);
            int testCellZ = minCellZ + getZ(i);

            for (int yIndex = minYIndex; yIndex < maxYIndex; yIndex++) {
               densityConsumer.consume(testCellX, yIndex + minCellY, testCellZ, densityColumn[yIndex] * 0.1F);
            }
         }
      }
   }

   private int cellCountPerColumn() {
      return this.areaWithOldGeneration.getSectionsCount() * 2;
   }

   private int quartCountPerColumn() {
      return QuartPos.fromSection(this.areaWithOldGeneration.getSectionsCount());
   }

   private int getColumnMinY() {
      return this.getMinY() + 1;
   }

   private int getMinY() {
      return this.areaWithOldGeneration.getMinSectionY() * 2;
   }

   private int getCellYIndex(final int cellY) {
      return cellY - this.getColumnMinY();
   }

   private static int getInsideIndex(final int x, final int z) {
      return CELL_HORIZONTAL_MAX_INDEX_INSIDE - x + z;
   }

   private static int getOutsideIndex(final int x, final int z) {
      return CELL_COLUMN_INSIDE_COUNT + x + CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - z;
   }

   private static int getX(final int index) {
      if (index < CELL_COLUMN_INSIDE_COUNT) {
         return zeroIfNegative(CELL_HORIZONTAL_MAX_INDEX_INSIDE - index);
      } else {
         int offsetIndex = index - CELL_COLUMN_INSIDE_COUNT;
         return CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - zeroIfNegative(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - offsetIndex);
      }
   }

   private static int getZ(final int index) {
      if (index < CELL_COLUMN_INSIDE_COUNT) {
         return zeroIfNegative(index - CELL_HORIZONTAL_MAX_INDEX_INSIDE);
      } else {
         int offsetIndex = index - CELL_COLUMN_INSIDE_COUNT;
         return CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - zeroIfNegative(offsetIndex - CELL_HORIZONTAL_MAX_INDEX_OUTSIDE);
      }
   }

   private static int zeroIfNegative(final int value) {
      return value & ~(value >> 31);
   }

   public LevelHeightAccessor getAreaWithOldGeneration() {
      return this.areaWithOldGeneration;
   }
}
