package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$FiveCrossing extends StrongholdPieces$StrongholdPiece {
   protected static final int WIDTH = 10;
   protected static final int HEIGHT = 9;
   protected static final int DEPTH = 11;
   private final boolean leftLow;
   private final boolean leftHigh;
   private final boolean rightLow;
   private final boolean rightHigh;

   public StrongholdPieces$FiveCrossing(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, genDepth, boundingBox);
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
      this.leftLow = random.nextBoolean();
      this.leftHigh = random.nextBoolean();
      this.rightLow = random.nextBoolean();
      this.rightHigh = random.nextInt(3) > 0;
   }

   public StrongholdPieces$FiveCrossing(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, tag);
      this.leftLow = tag.getBooleanOr("leftLow", false);
      this.leftHigh = tag.getBooleanOr("leftHigh", false);
      this.rightLow = tag.getBooleanOr("rightLow", false);
      this.rightHigh = tag.getBooleanOr("rightHigh", false);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("leftLow", this.leftLow);
      tag.putBoolean("leftHigh", this.leftHigh);
      tag.putBoolean("rightLow", this.rightLow);
      tag.putBoolean("rightHigh", this.rightHigh);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      int zOffA = 3;
      int zOffB = 5;
      Direction orientation = this.getOrientation();
      if (orientation == Direction.WEST || orientation == Direction.NORTH) {
         zOffA = 8 - zOffA;
         zOffB = 8 - zOffB;
      }

      this.generateSmallDoorChildForward((StrongholdPieces$StartPiece)startPiece, builder, random, 5, 1);
      if (this.leftLow) {
         this.generateSmallDoorChildLeft((StrongholdPieces$StartPiece)startPiece, builder, random, zOffA, 1);
      }

      if (this.leftHigh) {
         this.generateSmallDoorChildLeft((StrongholdPieces$StartPiece)startPiece, builder, random, zOffB, 7);
      }

      if (this.rightLow) {
         this.generateSmallDoorChildRight((StrongholdPieces$StartPiece)startPiece, builder, random, zOffA, 1);
      }

      if (this.rightHigh) {
         this.generateSmallDoorChildRight((StrongholdPieces$StartPiece)startPiece, builder, random, zOffB, 7);
      }
   }

   @Nullable
   public static StrongholdPieces$FiveCrossing createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -3, 0, 10, 9, 11, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$FiveCrossing(genDepth, random, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 9, 8, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 3, 0);
      if (this.leftLow) {
         this.generateBox(level, chunkBB, 0, 3, 1, 0, 5, 3, CAVE_AIR, CAVE_AIR, false);
      }

      if (this.rightLow) {
         this.generateBox(level, chunkBB, 9, 3, 1, 9, 5, 3, CAVE_AIR, CAVE_AIR, false);
      }

      if (this.leftHigh) {
         this.generateBox(level, chunkBB, 0, 5, 7, 0, 7, 9, CAVE_AIR, CAVE_AIR, false);
      }

      if (this.rightHigh) {
         this.generateBox(level, chunkBB, 9, 5, 7, 9, 7, 9, CAVE_AIR, CAVE_AIR, false);
      }

      this.generateBox(level, chunkBB, 5, 1, 10, 7, 3, 10, CAVE_AIR, CAVE_AIR, false);
      this.generateBox(level, chunkBB, 1, 2, 1, 8, 2, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 4, 1, 5, 4, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 8, 1, 5, 8, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 1, 4, 7, 3, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 1, 3, 5, 3, 3, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 5, 1, 7, 7, 1, 8, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
      this.generateBox(
         level,
         chunkBB,
         5,
         5,
         7,
         7,
         5,
         9,
         Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE),
         Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE),
         false
      );
      this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH), 6, 5, 6, chunkBB);
   }
}
