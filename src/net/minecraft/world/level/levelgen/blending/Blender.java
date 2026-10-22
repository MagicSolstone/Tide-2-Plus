package net.minecraft.world.level.levelgen.blending;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.CompositeDirection$Direction8;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Plane;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.data.worldgen.NoiseData;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask$Filter;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;

public class Blender {
   public static final ContextKey<Blender> CONTEXT_KEY = ContextKey.vanilla("blender");
   public static final ContextKey<DensitySampler> ALPHA_KEY = ContextKey.vanilla("blender/alpha");
   public static final ContextKey<DensitySampler> OFFSET_KEY = ContextKey.vanilla("blender/offset");
   private static final Blender EMPTY = new Blender$1(new Long2ObjectOpenHashMap(), new Long2ObjectOpenHashMap(), null);
   private static final Noise SHIFT_NOISE = NoiseData.DEFAULT_SHIFT.create(new XoroshiroRandomSource(42L));
   private static final int HEIGHT_BLENDING_RANGE_CELLS = QuartPos.fromSection(7) - 1;
   private static final int HEIGHT_BLENDING_RANGE_CHUNKS = QuartPos.toSection(HEIGHT_BLENDING_RANGE_CELLS + 3);
   private static final int DENSITY_BLENDING_RANGE_CELLS = 2;
   private static final int DENSITY_BLENDING_RANGE_CHUNKS = QuartPos.toSection(5);
   private static final double OLD_CHUNK_XZ_RADIUS = 8.0;
   private final Long2ObjectOpenHashMap<BlendingData> heightAndBiomeBlendingData;
   private final Long2ObjectOpenHashMap<BlendingData> densityBlendingData;
   @Nullable
   private final CarvingMask$Filter carvingFilter;

   public static Blender empty() {
      return EMPTY;
   }

   public static Blender of(@Nullable final WorldGenRegion region) {
      if (!SharedConstants.DEBUG_DISABLE_BLENDING && region != null) {
         ChunkPos centerPos = region.getCenter();
         if (!region.isOldChunkAround(centerPos, HEIGHT_BLENDING_RANGE_CHUNKS)) {
            return EMPTY;
         } else {
            Long2ObjectOpenHashMap<BlendingData> heightAndBiomeData = new Long2ObjectOpenHashMap();
            Long2ObjectOpenHashMap<BlendingData> densityData = new Long2ObjectOpenHashMap();
            int maxDistSq = Mth.square(HEIGHT_BLENDING_RANGE_CHUNKS + 1);

            for (int dx = -HEIGHT_BLENDING_RANGE_CHUNKS; dx <= HEIGHT_BLENDING_RANGE_CHUNKS; dx++) {
               for (int dz = -HEIGHT_BLENDING_RANGE_CHUNKS; dz <= HEIGHT_BLENDING_RANGE_CHUNKS; dz++) {
                  if (dx * dx + dz * dz <= maxDistSq) {
                     int chunkX = centerPos.x() + dx;
                     int chunkZ = centerPos.z() + dz;
                     BlendingData blendingData = BlendingData.getOrUpdateBlendingData(region, chunkX, chunkZ);
                     if (blendingData != null) {
                        heightAndBiomeData.put(ChunkPos.pack(chunkX, chunkZ), blendingData);
                        if (dx >= -DENSITY_BLENDING_RANGE_CHUNKS
                           && dx <= DENSITY_BLENDING_RANGE_CHUNKS
                           && dz >= -DENSITY_BLENDING_RANGE_CHUNKS
                           && dz <= DENSITY_BLENDING_RANGE_CHUNKS) {
                           densityData.put(ChunkPos.pack(chunkX, chunkZ), blendingData);
                        }
                     }
                  }
               }
            }

            ChunkAccess centerChunk = region.getChunk(centerPos.x(), centerPos.z());
            CarvingMask$Filter carvingFilter = createAroundOldChunksCarvingMaskFilter(region, centerChunk);
            return heightAndBiomeData.isEmpty() && densityData.isEmpty() && carvingFilter == null
               ? EMPTY
               : new Blender(heightAndBiomeData, densityData, carvingFilter);
         }
      } else {
         return EMPTY;
      }
   }

   private Blender(
      final Long2ObjectOpenHashMap<BlendingData> heightAndBiomeBlendingData,
      final Long2ObjectOpenHashMap<BlendingData> densityBlendingData,
      @Nullable final CarvingMask$Filter carvingFilter
   ) {
      this.heightAndBiomeBlendingData = heightAndBiomeBlendingData;
      this.densityBlendingData = densityBlendingData;
      this.carvingFilter = carvingFilter;
   }

   public boolean isEmpty() {
      return this.heightAndBiomeBlendingData.isEmpty() && this.densityBlendingData.isEmpty() && this.carvingFilter == null;
   }

   public Blender$OutputBuffer blendOffsetAndFactor(final DensityVolume volume) {
      int minCellX = QuartPos.fromBlock(volume.minBlockX());
      int minCellZ = QuartPos.fromBlock(volume.minBlockZ());
      int maxCellX = QuartPos.fromBlock(volume.maxBlockX()) + 1;
      int maxCellZ = QuartPos.fromBlock(volume.maxBlockZ()) + 1;
      int cellCountX = maxCellX - minCellX + 1;
      int cellCountZ = maxCellZ - minCellZ + 1;
      float[] alphas = new float[cellCountX * cellCountZ];
      float[] offsets = new float[cellCountX * cellCountZ];

      for (int cellZ = 0; cellZ < cellCountZ; cellZ++) {
         for (int cellX = 0; cellX < cellCountX; cellX++) {
            int index = cellX + cellZ * cellCountX;
            Blender$BlendingOutput output = this.blendOffsetAndFactor(QuartPos.toBlock(minCellX + cellX), QuartPos.toBlock(minCellZ + cellZ));
            alphas[index] = output.alpha;
            offsets[index] = output.blendingOffset;
         }
      }

      return new Blender$OutputBuffer(this, minCellX, minCellZ, cellCountX, cellCountZ, alphas, offsets);
   }

   protected Blender$BlendingOutput blendOffsetAndFactor(final int blockX, final int blockZ) {
      int cellX = QuartPos.fromBlock(blockX);
      int cellZ = QuartPos.fromBlock(blockZ);
      float fixedHeight = this.getBlendingDataValue(cellX, 0, cellZ, BlendingData::getHeight);
      if (fixedHeight != Float.MAX_VALUE) {
         return new Blender$BlendingOutput(0.0F, (float)heightToOffset(fixedHeight));
      } else {
         MutableDouble totalWeight = new MutableDouble(0.0);
         MutableDouble weightedHeights = new MutableDouble(0.0);
         MutableDouble closestDistance = new MutableDouble(Double.POSITIVE_INFINITY);
         this.heightAndBiomeBlendingData
            .forEach(
               (chunkPos, blendingData) -> blendingData.iterateHeights(
                  QuartPos.fromSection(ChunkPos.getX(chunkPos)), QuartPos.fromSection(ChunkPos.getZ(chunkPos)), (testCellX, testCellZ, height) -> {
                     double distance = Mth.length((float)(cellX - testCellX), (float)(cellZ - testCellZ));
                     if (!(distance > HEIGHT_BLENDING_RANGE_CELLS)) {
                        if (distance < closestDistance.doubleValue()) {
                           closestDistance.setValue(distance);
                        }

                        double weight = 1.0 / (distance * distance * distance * distance);
                        weightedHeights.add(height * weight);
                        totalWeight.add(weight);
                     }
                  }
               )
            );
         if (closestDistance.doubleValue() == Double.POSITIVE_INFINITY) {
            return new Blender$BlendingOutput(1.0F, 0.0F);
         } else {
            double averageHeight = weightedHeights.doubleValue() / totalWeight.doubleValue();
            double alpha = Mth.clamp(closestDistance.doubleValue() / (HEIGHT_BLENDING_RANGE_CELLS + 1), 0.0, 1.0);
            alpha = 3.0 * alpha * alpha - 2.0 * alpha * alpha * alpha;
            return new Blender$BlendingOutput((float)alpha, (float)heightToOffset(averageHeight));
         }
      }
   }

   private static double heightToOffset(final double height) {
      double dimensionFactor = 1.0;
      double targetY = height + 0.5;
      double targetYMod = Mth.positiveModulo(targetY, 8.0);
      return 1.0 * (32.0 * (targetY - 128.0) - 3.0 * (targetY - 120.0) * targetYMod + 3.0 * targetYMod * targetYMod) / (128.0 * (32.0 - 3.0 * targetYMod));
   }

   public float blendDensity(final int blockX, final int blockY, final int blockZ, final float noiseValue) {
      int cellX = QuartPos.fromBlock(blockX);
      int cellY = blockY / 8;
      int cellZ = QuartPos.fromBlock(blockZ);
      float fixedDensity = this.getBlendingDataValue(cellX, cellY, cellZ, BlendingData::getDensity);
      if (fixedDensity != Float.MAX_VALUE) {
         return fixedDensity;
      } else {
         MutableDouble totalWeight = new MutableDouble(0.0);
         MutableDouble weightedHeights = new MutableDouble(0.0);
         MutableDouble closestDistance = new MutableDouble(Double.POSITIVE_INFINITY);
         this.densityBlendingData
            .forEach(
               (chunkPos, blendingData) -> blendingData.iterateDensities(
                  QuartPos.fromSection(ChunkPos.getX(chunkPos)),
                  QuartPos.fromSection(ChunkPos.getZ(chunkPos)),
                  cellY - 1,
                  cellY + 1,
                  (testCellX, testCellY, testCellZ, density) -> {
                     double distance = Mth.length((float)(cellX - testCellX), (float)((cellY - testCellY) * 2), (float)(cellZ - testCellZ));
                     if (!(distance > 2.0)) {
                        if (distance < closestDistance.doubleValue()) {
                           closestDistance.setValue(distance);
                        }

                        double weight = 1.0 / (distance * distance * distance * distance);
                        weightedHeights.add(density * weight);
                        totalWeight.add(weight);
                     }
                  }
               )
            );
         if (closestDistance.doubleValue() == Double.POSITIVE_INFINITY) {
            return noiseValue;
         } else {
            double averageDensity = weightedHeights.doubleValue() / totalWeight.doubleValue();
            double alpha = Mth.clamp(closestDistance.doubleValue() / 3.0, 0.0, 1.0);
            return (float)Mth.lerp(alpha, averageDensity, (double)noiseValue);
         }
      }
   }

   private float getBlendingDataValue(final int cellX, final int cellY, final int cellZ, final Blender$CellValueGetter cellValueGetter) {
      int chunkX = QuartPos.toSection(cellX);
      int chunkZ = QuartPos.toSection(cellZ);
      boolean minX = (cellX & 3) == 0;
      boolean minZ = (cellZ & 3) == 0;
      float value = this.getBlendingDataValue(cellValueGetter, chunkX, chunkZ, cellX, cellY, cellZ);
      if (value == Float.MAX_VALUE) {
         if (minX && minZ) {
            value = this.getBlendingDataValue(cellValueGetter, chunkX - 1, chunkZ - 1, cellX, cellY, cellZ);
         }

         if (value == Float.MAX_VALUE) {
            if (minX) {
               value = this.getBlendingDataValue(cellValueGetter, chunkX - 1, chunkZ, cellX, cellY, cellZ);
            }

            if (value == Float.MAX_VALUE && minZ) {
               value = this.getBlendingDataValue(cellValueGetter, chunkX, chunkZ - 1, cellX, cellY, cellZ);
            }
         }
      }

      return value;
   }

   private float getBlendingDataValue(
      final Blender$CellValueGetter cellValueGetter, final int chunkX, final int chunkZ, final int cellX, final int cellY, final int cellZ
   ) {
      BlendingData blendingData = (BlendingData)this.heightAndBiomeBlendingData.get(ChunkPos.pack(chunkX, chunkZ));
      return blendingData != null
         ? cellValueGetter.get(blendingData, cellX - QuartPos.fromSection(chunkX), cellY, cellZ - QuartPos.fromSection(chunkZ))
         : Float.MAX_VALUE;
   }

   public BiomeResolver getBiomeResolver(final BiomeResolver biomeResolver) {
      return (quartX, quartY, quartZ) -> {
         Holder<Biome> biome = this.blendBiome(quartX, quartY, quartZ);
         return biome == null ? biomeResolver.getNoiseBiome(quartX, quartY, quartZ) : biome;
      };
   }

   private Holder<Biome> blendBiome(final int quartX, final int quartY, final int quartZ) {
      MutableFloat closestDistance = new MutableFloat(Float.POSITIVE_INFINITY);
      MutableObject<Holder<Biome>> closestBiome = new MutableObject();
      this.heightAndBiomeBlendingData
         .forEach(
            (chunkPos, blendingData) -> blendingData.iterateBiomes(
               QuartPos.fromSection(ChunkPos.getX(chunkPos)), quartY, QuartPos.fromSection(ChunkPos.getZ(chunkPos)), (testCellX, testCellZ, biome) -> {
                  float distance = Mth.length((float)(quartX - testCellX), (float)(quartZ - testCellZ));
                  if (!(distance > HEIGHT_BLENDING_RANGE_CELLS)) {
                     if (distance < closestDistance.floatValue()) {
                        closestBiome.setValue(biome);
                        closestDistance.setValue(distance);
                     }
                  }
               }
            )
         );
      if (closestDistance.floatValue() == Float.POSITIVE_INFINITY) {
         return null;
      } else {
         float shiftNoise = SHIFT_NOISE.get(quartX, 0.0, quartZ) * 12.0F;
         float alpha = Mth.clamp((closestDistance.floatValue() + shiftNoise) / (HEIGHT_BLENDING_RANGE_CELLS + 1), 0.0F, 1.0F);
         return alpha > 0.5F ? null : (Holder)closestBiome.get();
      }
   }

   public static void generateBorderTicks(final WorldGenRegion region, final ChunkAccess chunk) {
      if (!SharedConstants.DEBUG_DISABLE_BLENDING) {
         ChunkPos chunkPos = chunk.getPos();
         boolean oldNoiseGeneration = chunk.isOldNoiseGeneration();
         BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos();
         BlockPos chunkOrigin = new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ());
         BlendingData blendingData = chunk.getBlendingData();
         if (blendingData != null) {
            int oldMinY = blendingData.getAreaWithOldGeneration().getMinY();
            int oldMaxY = blendingData.getAreaWithOldGeneration().getMaxY();
            if (oldNoiseGeneration) {
               for (int x = 0; x < 16; x++) {
                  for (int z = 0; z < 16; z++) {
                     generateBorderTick(chunk, pos.setWithOffset(chunkOrigin, x, oldMinY - 1, z));
                     generateBorderTick(chunk, pos.setWithOffset(chunkOrigin, x, oldMinY, z));
                     generateBorderTick(chunk, pos.setWithOffset(chunkOrigin, x, oldMaxY, z));
                     generateBorderTick(chunk, pos.setWithOffset(chunkOrigin, x, oldMaxY + 1, z));
                  }
               }
            }

            for (Direction direction : Direction$Plane.HORIZONTAL) {
               if (region.getChunk(chunkPos.x() + direction.getStepX(), chunkPos.z() + direction.getStepZ()).isOldNoiseGeneration() != oldNoiseGeneration) {
                  int minX = direction == Direction.EAST ? 15 : 0;
                  int maxX = direction == Direction.WEST ? 0 : 15;
                  int minZ = direction == Direction.SOUTH ? 15 : 0;
                  int maxZ = direction == Direction.NORTH ? 0 : 15;

                  for (int x = minX; x <= maxX; x++) {
                     for (int z = minZ; z <= maxZ; z++) {
                        int maxY = Math.min(oldMaxY, chunk.getHeight(Heightmap$Types.MOTION_BLOCKING, x, z)) + 1;

                        for (int y = oldMinY; y < maxY; y++) {
                           generateBorderTick(chunk, pos.setWithOffset(chunkOrigin, x, y, z));
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void generateBorderTick(final ChunkAccess chunk, final BlockPos pos) {
      BlockState blockState = chunk.getBlockState(pos);
      if (blockState.is(BlockTags.LEAVES)) {
         chunk.markPosForPostProcessing(pos);
      }

      FluidState fluidState = chunk.getFluidState(pos);
      if (!fluidState.isEmpty()) {
         chunk.markPosForPostProcessing(pos);
      }
   }

   @Nullable
   public CarvingMask$Filter getCarvingFilter() {
      return this.carvingFilter;
   }

   @Nullable
   private static CarvingMask$Filter createAroundOldChunksCarvingMaskFilter(final WorldGenLevel region, final ChunkAccess chunk) {
      ChunkPos chunkPos = chunk.getPos();
      Builder<CompositeDirection$Direction8, BlendingData> builder = ImmutableMap.builder();

      for (CompositeDirection$Direction8 direction8 : CompositeDirection$Direction8.values()) {
         int testChunkX = chunkPos.x() + direction8.getStepX();
         int testChunkZ = chunkPos.z() + direction8.getStepZ();
         BlendingData blendingData = region.getChunk(testChunkX, testChunkZ).getBlendingData();
         if (blendingData != null) {
            builder.put(direction8, blendingData);
         }
      }

      ImmutableMap<CompositeDirection$Direction8, BlendingData> oldSidesBlendingData = builder.build();
      BlendingData centerBlendingData = chunk.getBlendingData();
      if (centerBlendingData == null && oldSidesBlendingData.isEmpty()) {
         return null;
      } else {
         Blender$DistanceGetter distanceGetter = makeOldChunkDistanceGetter(centerBlendingData, oldSidesBlendingData);
         return (x, y, z) -> {
            double shiftedX = x + 0.5 + SHIFT_NOISE.get(x, y, z) * 4.0F;
            double shiftedY = y + 0.5 + SHIFT_NOISE.get(y, z, x) * 4.0F;
            double shiftedZ = z + 0.5 + SHIFT_NOISE.get(z, x, y) * 4.0F;
            return distanceGetter.getDistance(shiftedX, shiftedY, shiftedZ) < 4.0;
         };
      }
   }

   public static Blender$DistanceGetter makeOldChunkDistanceGetter(
      @Nullable final BlendingData centerBlendingData, final Map<CompositeDirection$Direction8, BlendingData> oldSidesBlendingData
   ) {
      List<Blender$DistanceGetter> distanceGetters = Lists.newArrayList();
      if (centerBlendingData != null) {
         distanceGetters.add(makeOffsetOldChunkDistanceGetter(null, centerBlendingData));
      }

      oldSidesBlendingData.forEach((side, blendingData) -> distanceGetters.add(makeOffsetOldChunkDistanceGetter(side, blendingData)));
      return (x, y, z) -> {
         double closest = Double.POSITIVE_INFINITY;

         for (Blender$DistanceGetter getter : distanceGetters) {
            double distance = getter.getDistance(x, y, z);
            if (distance < closest) {
               closest = distance;
            }
         }

         return closest;
      };
   }

   private static Blender$DistanceGetter makeOffsetOldChunkDistanceGetter(@Nullable final CompositeDirection$Direction8 offset, final BlendingData blendingData) {
      double offsetX = 0.0;
      double offsetZ = 0.0;
      if (offset != null) {
         for (Direction direction : offset.getDirections()) {
            offsetX += direction.getStepX() * 16;
            offsetZ += direction.getStepZ() * 16;
         }
      }

      double finalOffsetX = offsetX;
      double finalOffsetZ = offsetZ;
      double oldChunkYRadius = blendingData.getAreaWithOldGeneration().getHeight() / 2.0;
      double oldChunkCenterY = blendingData.getAreaWithOldGeneration().getMinY() + oldChunkYRadius;
      return (x, y, z) -> distanceToCube(x - 8.0 - finalOffsetX, y - oldChunkCenterY, z - 8.0 - finalOffsetZ, 8.0, oldChunkYRadius, 8.0);
   }

   private static double distanceToCube(final double x, final double y, final double z, final double radiusX, final double radiusY, final double radiusZ) {
      double deltaX = Math.abs(x) - radiusX;
      double deltaY = Math.abs(y) - radiusY;
      double deltaZ = Math.abs(z) - radiusZ;
      return Mth.length(Math.max(0.0, deltaX), Math.max(0.0, deltaY), Math.max(0.0, deltaZ));
   }
}
