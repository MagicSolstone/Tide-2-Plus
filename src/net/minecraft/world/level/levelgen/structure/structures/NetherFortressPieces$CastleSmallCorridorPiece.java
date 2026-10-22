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

public class NetherFortressPieces$CastleSmallCorridorPiece extends NetherFortressPieces$NetherBridgePiece {
   private static final int WIDTH = 5;
   private static final int HEIGHT = 7;
   private static final int DEPTH = 5;

   public NetherFortressPieces$CastleSmallCorridorPiece(final int genDepth, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, genDepth, boundingBox);
      this.setOrientation(direction);
   }

   public NetherFortressPieces$CastleSmallCorridorPiece(final CompoundTag tag) {
      super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, tag);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateChildForward((NetherFortressPieces$StartPiece)startPiece, builder, random, 1, 0, true);
   }

   @Nullable
   public static NetherFortressPieces$CastleSmallCorridorPiece createPiece(
      final StructurePiecesBuilder builder, final int footX, final int footY, final int footZ, final Direction direction, final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new NetherFortressPieces$CastleSmallCorridorPiece(genDepth, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
      BlockState nsFence = Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.SOUTH, true);
      this.generateBox(level, chunkBB, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 0, 3, 1, 0, 4, 1, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 0, 3, 3, 0, 4, 3, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 4, 3, 1, 4, 4, 1, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 4, 3, 3, 4, 4, 3, nsFence, nsFence, false);
      this.generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);

      for (int x = 0; x <= 4; x++) {
         for (int z = 0; z <= 4; z++) {
            this.fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
         }
      }
   }
}
