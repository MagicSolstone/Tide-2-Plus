package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class NetherFortressPieces$CastleCorridorTBalconyPiece extends NetherFortressPieces$NetherBridgePiece {
   private static final int WIDTH = 9;
   private static final int HEIGHT = 7;
   private static final int DEPTH = 9;

   public NetherFortressPieces$CastleCorridorTBalconyPiece(final int genDepth, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, genDepth, boundingBox);
      this.setOrientation(direction);
   }

   public NetherFortressPieces$CastleCorridorTBalconyPiece(final CompoundTag tag) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, tag);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      int zOff = 1;
      Direction orientation = this.getOrientation();
      if (orientation == Direction.WEST || orientation == Direction.NORTH) {
         zOff = 5;
      }

      this.generateChildLeft((NetherFortressPieces$StartPiece)startPiece, builder, random, 0, zOff, random.nextInt(8) > 0);
      this.generateChildRight((NetherFortressPieces$StartPiece)startPiece, builder, random, 0, zOff, random.nextInt(8) > 0);
   }

   @Nullable
   public static NetherFortressPieces$CastleCorridorTBalconyPiece createPiece(
      final StructurePiecesBuilder builder, final int footX, final int footY, final int footZ, final Direction direction, final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -3, 0, 0, 9, 7, 9, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new NetherFortressPieces$CastleCorridorTBalconyPiece(genDepth, box, direction) : null;
   }

   @Override
   public void postProcess(
      final WorldGenLevel level,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final RandomSource random,
      final BoundingBox chunkBB,
      final ChunkPos chunkPos,
      final BlockPos referencePos
   ) {
      BlockState nsFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.SOUTH, true);
      BlockState weFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, true).setValue(FenceBlock.EAST, true);
      this.generateBox(level, chunkBB, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 3, 0, 1, 4, 0, weFence, weFence, false);
      this.generateBox(level, chunkBB, 7, 3, 0, 7, 4, 0, weFence, weFence, false);
      this.generateBox(level, chunkBB, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 3, 8, 7, 3, 8, weFence, weFence, false);
      this.placeBlock(level, Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, true).setValue(FenceBlock.SOUTH, true), 0, 3, 8, chunkBB);
      this.placeBlock(level, Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, true).setValue(FenceBlock.SOUTH, true), 8, 3, 8, chunkBB);
      this.generateBox(level, chunkBB, 0, 3, 6, 0, 3, 7, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 8, 3, 6, 8, 3, 7, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 4, 5, 1, 5, 5, weFence, weFence, false);
      this.generateBox(level, chunkBB, 7, 4, 5, 7, 5, 5, weFence, weFence, false);

      for (int z = 0; z <= 5; z++) {
         for (int x = 0; x <= 8; x++) {
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
         }
      }
   }
}
