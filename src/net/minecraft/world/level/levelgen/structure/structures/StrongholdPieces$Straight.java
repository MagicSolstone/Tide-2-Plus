package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$Straight extends StrongholdPieces$StrongholdPiece {
   private static final int WIDTH = 5;
   private static final int HEIGHT = 5;
   private static final int DEPTH = 7;
   private final boolean leftChild;
   private final boolean rightChild;

   public StrongholdPieces$Straight(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_STRAIGHT, genDepth, boundingBox);
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
      this.leftChild = random.nextInt(2) == 0;
      this.rightChild = random.nextInt(2) == 0;
   }

   public StrongholdPieces$Straight(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_STRAIGHT, tag);
      this.leftChild = tag.getBooleanOr("Left", false);
      this.rightChild = tag.getBooleanOr("Right", false);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("Left", this.leftChild);
      tag.putBoolean("Right", this.rightChild);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateSmallDoorChildForward((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 1);
      if (this.leftChild) {
         this.generateSmallDoorChildLeft((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 2);
      }

      if (this.rightChild) {
         this.generateSmallDoorChildRight((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 2);
      }
   }

   @Nullable
   public static StrongholdPieces$Straight createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$Straight(genDepth, random, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
      this.generateSmallDoor(level, random, chunkBB, StrongholdPieces$StrongholdPiece$SmallDoorType.OPENING, 1, 1, 6);
      BlockState eastTorch = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST);
      BlockState westTorch = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST);
      this.maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 1, eastTorch);
      this.maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 1, westTorch);
      this.maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 5, eastTorch);
      this.maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 5, westTorch);
      if (this.leftChild) {
         this.generateBox(level, chunkBB, 0, 1, 2, 0, 3, 4, CAVE_AIR, CAVE_AIR, false);
      }

      if (this.rightChild) {
         this.generateBox(level, chunkBB, 4, 1, 2, 4, 3, 4, CAVE_AIR, CAVE_AIR, false);
      }
   }
}
