package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool$Projection;
import org.jspecify.annotations.Nullable;

public class Beardifier implements DensitySampler {
   public static final Interval RANGE = Interval.INFINITE;
   public static final ContextKey<Beardifier> CONTEXT_KEY = ContextKey.vanilla("beardifier");
   public static final int BEARD_KERNEL_RADIUS = 12;
   private static final int BEARD_KERNEL_SIZE = 24;
   private static final float[] BEARD_KERNEL = Util.make(new float[13824], kernel -> {
      for (int zi = 0; zi < 24; zi++) {
         for (int xi = 0; xi < 24; xi++) {
            for (int yi = 0; yi < 24; yi++) {
               kernel[zi * 24 * 24 + xi * 24 + yi] = (float)computeBeardContribution(xi - 12, yi - 12, zi - 12);
            }
         }
      }
   });
   private static final float MAX_BURY_DISTANCE = 6.0F;
   public static final Beardifier EMPTY = new Beardifier(List.of(), List.of(), null);
   private final List<Beardifier$Rigid> pieces;
   private final List<JigsawJunction> junctions;
   @Nullable
   private final BoundingBox affectedBox;

   public static Beardifier forStructuresInChunk(final StructureManager structureManager, final ChunkPos chunkPos) {
      List<StructureStart> structureStarts = structureManager.startsForStructure(
         chunkPos.x(), chunkPos.z(), s -> s.terrainAdaptation() != TerrainAdjustment.NONE
      );
      if (structureStarts.isEmpty()) {
         return EMPTY;
      } else {
         int chunkStartBlockX = chunkPos.getMinBlockX();
         int chunkStartBlockZ = chunkPos.getMinBlockZ();
         List<Beardifier$Rigid> rigids = new ArrayList<>();
         List<JigsawJunction> junctions = new ArrayList<>();
         BoundingBox anyPieceBoundingBox = null;

         for (StructureStart start : structureStarts) {
            TerrainAdjustment terrainAdjustment = start.getStructure().terrainAdaptation();

            for (StructurePiece piece : start.getPieces()) {
               if (piece.isCloseToChunk(chunkPos, 12)) {
                  if (piece instanceof PoolElementStructurePiece poolPiece) {
                     StructureTemplatePool$Projection projection = poolPiece.getElement().getProjection();
                     if (projection == StructureTemplatePool$Projection.RIGID) {
                        rigids.add(new Beardifier$Rigid(poolPiece.getBoundingBox(), terrainAdjustment, poolPiece.getGroundLevelDelta()));
                        anyPieceBoundingBox = includeBoundingBox(anyPieceBoundingBox, piece.getBoundingBox());
                     }

                     for (JigsawJunction junction : poolPiece.getJunctions()) {
                        int junctionX = junction.getSourceX();
                        int junctionZ = junction.getSourceZ();
                        if (junctionX > chunkStartBlockX - 12
                           && junctionZ > chunkStartBlockZ - 12
                           && junctionX < chunkStartBlockX + 15 + 12
                           && junctionZ < chunkStartBlockZ + 15 + 12) {
                           junctions.add(junction);
                           BoundingBox junctionBox = new BoundingBox(new BlockPos(junctionX, junction.getSourceGroundY(), junctionZ));
                           anyPieceBoundingBox = includeBoundingBox(anyPieceBoundingBox, junctionBox);
                        }
                     }
                  } else {
                     rigids.add(new Beardifier$Rigid(piece.getBoundingBox(), terrainAdjustment, 0));
                     anyPieceBoundingBox = includeBoundingBox(anyPieceBoundingBox, piece.getBoundingBox());
                  }
               }
            }
         }

         if (anyPieceBoundingBox == null) {
            return EMPTY;
         } else {
            BoundingBox affectedBox = anyPieceBoundingBox.inflatedBy(24);
            return new Beardifier(List.copyOf(rigids), List.copyOf(junctions), affectedBox);
         }
      }
   }

   private static BoundingBox includeBoundingBox(@Nullable final BoundingBox encompassingBox, final BoundingBox newBox) {
      return encompassingBox == null ? newBox : BoundingBox.encapsulating(encompassingBox, newBox);
   }

   @VisibleForTesting
   public Beardifier(final List<Beardifier$Rigid> pieces, final List<JigsawJunction> junctions, @Nullable final BoundingBox affectedBox) {
      this.pieces = pieces;
      this.junctions = junctions;
      this.affectedBox = affectedBox;
   }

   @Override
   public void sampleVolume(final SamplerContext context, final DensityBuffer outputBuffer, final DensityVolume volume) {
      outputBuffer.fill(0.0F);
      if (this.affectedBox != null && volume.intersects(this.affectedBox)) {
         int minX = Math.floorDiv(Math.max(0, this.affectedBox.minX() - volume.minBlockX()), volume.stepBlockX());
         int minY = Math.floorDiv(Math.max(0, this.affectedBox.minY() - volume.minBlockY()), volume.stepBlockY());
         int minZ = Math.floorDiv(Math.max(0, this.affectedBox.minZ() - volume.minBlockZ()), volume.stepBlockZ());
         int maxX = Math.min(volume.sizeX() - 1, Math.floorDiv(this.affectedBox.maxX() - volume.minBlockX(), volume.stepBlockX()));
         int maxY = Math.min(volume.sizeY() - 1, Math.floorDiv(this.affectedBox.maxY() - volume.minBlockY(), volume.stepBlockY()));
         int maxZ = Math.min(volume.sizeZ() - 1, Math.floorDiv(this.affectedBox.maxZ() - volume.minBlockZ(), volume.stepBlockZ()));

         for (int z = minZ; z <= maxZ; z++) {
            int blockZ = volume.blockZ(z);

            for (int x = minX; x <= maxX; x++) {
               int blockX = volume.blockX(x);

               for (int y = minY; y <= maxY; y++) {
                  int index = volume.indexUnchecked(x, y, z);
                  int blockY = volume.blockY(y);
                  outputBuffer.set(index, this.sampleValueUnchecked(blockX, blockY, blockZ));
               }
            }
         }
      }
   }

   @Override
   public float sampleValue(final SamplerContext context, final int blockX, final int blockY, final int blockZ) {
      return this.affectedBox != null && this.affectedBox.isInside(blockX, blockY, blockZ) ? this.sampleValueUnchecked(blockX, blockY, blockZ) : 0.0F;
   }

   private float sampleValueUnchecked(final int blockX, final int blockY, final int blockZ) {
      float noiseValue = 0.0F;

      for (Beardifier$Rigid rigid : this.pieces) {
         BoundingBox box = rigid.box();
         int groundLevelDelta = rigid.groundLevelDelta();
         int dx = Math.max(0, Math.max(box.minX() - blockX, blockX - box.maxX()));
         int dz = Math.max(0, Math.max(box.minZ() - blockZ, blockZ - box.maxZ()));
         int groundY = box.minY() + groundLevelDelta;
         int dyToGround = blockY - groundY;

         int dy = switch (rigid.terrainAdjustment()) {
            case NONE -> 0;
            case BURY, BEARD_THIN -> dyToGround;
            case BEARD_BOX -> Math.max(0, Math.max(groundY - blockY, blockY - box.maxY()));
            case ENCAPSULATE -> Math.max(0, Math.max(box.minY() - blockY, blockY - box.maxY()));
         };

         noiseValue += switch (rigid.terrainAdjustment()) {
            case NONE -> 0.0F;
            case BURY -> getBuryContribution(dx, dy / 2.0F, dz);
            case BEARD_THIN, BEARD_BOX -> getBeardContribution(dx, dy, dz, dyToGround) * 0.8F;
            case ENCAPSULATE -> getBuryContribution(dx / 2.0F, dy / 2.0F, dz / 2.0F) * 0.8F;
         };
      }

      for (JigsawJunction junction : this.junctions) {
         int dx = blockX - junction.getSourceX();
         int dy = blockY - junction.getSourceGroundY();
         int dz = blockZ - junction.getSourceZ();
         noiseValue += getBeardContribution(dx, dy, dz, dy) * 0.4F;
      }

      return noiseValue;
   }

   private static float getBuryContribution(final float dx, final float dy, final float dz) {
      float distanceSq = Mth.lengthSquared(dx, dy, dz);
      return distanceSq >= 36.0F ? 0.0F : 1.0F - Mth.sqrt(distanceSq) / 6.0F;
   }

   private static float getBeardContribution(final int dx, final int dy, final int dz, final int yToGround) {
      int xi = dx + 12;
      int yi = dy + 12;
      int zi = dz + 12;
      if (isInKernelRange(xi) && isInKernelRange(yi) && isInKernelRange(zi)) {
         float dyWithOffset = yToGround + 0.5F;
         float distanceSqr = Mth.lengthSquared((float)dx, dyWithOffset, (float)dz);
         float value = -dyWithOffset * (float)Mth.fastInvSqrt(distanceSqr / 2.0F) / 2.0F;
         return value * BEARD_KERNEL[zi * 24 * 24 + xi * 24 + yi];
      } else {
         return 0.0F;
      }
   }

   private static boolean isInKernelRange(final int xi) {
      return xi >= 0 && xi < 24;
   }

   private static double computeBeardContribution(final int dx, final int dy, final int dz) {
      return computeBeardContribution(dx, dy + 0.5, dz);
   }

   private static double computeBeardContribution(final int dx, final double dy, final int dz) {
      double distanceSqr = Mth.lengthSquared((double)dx, dy, (double)dz);
      return Math.pow(Math.E, -distanceSqr / 16.0);
   }
}
