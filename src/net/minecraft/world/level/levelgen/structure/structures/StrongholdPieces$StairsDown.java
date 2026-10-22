package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$StairsDown extends StrongholdPieces$StrongholdPiece {
   private static final int WIDTH = 5;
   private static final int HEIGHT = 11;
   private static final int DEPTH = 5;
   private final boolean isSource;

   public StrongholdPieces$StairsDown(final StructurePieceType type, final int genDepth, final int west, final int north, final Direction direction) {
      super(type, genDepth, makeBoundingBox(west, 64, north, direction, 5, 11, 5));
      this.isSource = true;
      this.setOrientation(direction);
      this.entryDoor = StrongholdPieces$StrongholdPiece$SmallDoorType.OPENING;
   }

   public StrongholdPieces$StairsDown(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, genDepth, boundingBox);
      this.isSource = false;
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
   }

   public StrongholdPieces$StairsDown(final StructurePieceType type, final CompoundTag tag) {
      super(type, tag);
      this.isSource = tag.getBooleanOr("Source", false);
   }

   public StrongholdPieces$StairsDown(final CompoundTag tag) {
      this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, tag);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("Source", this.isSource);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      if (this.isSource) {
         StrongholdPieces.imposedPiece = StrongholdPieces$FiveCrossing.class;
      }

      this.generateSmallDoorChildForward((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 1);
   }

   @Nullable
   public static StrongholdPieces$StairsDown createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 5, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$StairsDown(genDepth, random, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 4, 10, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
      this.generateSmallDoor(level, random, chunkBB, StrongholdPieces$StrongholdPiece$SmallDoorType.OPENING, 1, 1, 4);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, chunkBB);
      this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, chunkBB);
      this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, chunkBB);
      this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, chunkBB);
      this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, chunkBB);
      this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, chunkBB);
      this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, chunkBB);
   }
}
