package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$PrisonHall extends StrongholdPieces$StrongholdPiece {
   protected static final int WIDTH = 9;
   protected static final int HEIGHT = 5;
   protected static final int DEPTH = 11;

   public StrongholdPieces$PrisonHall(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_PRISON_HALL, genDepth, boundingBox);
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
   }

   public StrongholdPieces$PrisonHall(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_PRISON_HALL, tag);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateSmallDoorChildForward((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 1);
   }

   @Nullable
   public static StrongholdPieces$PrisonHall createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 9, 5, 11, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$PrisonHall(genDepth, random, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 8, 4, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
      this.generateBox(level, chunkBB, 1, 1, 10, 3, 3, 10, CAVE_AIR, CAVE_AIR, false);
      this.generateBox(level, chunkBB, 4, 1, 1, 4, 3, 1, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 4, 1, 3, 4, 3, 3, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 4, 1, 7, 4, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 4, 1, 9, 4, 3, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);

      for (int y = 1; y <= 3; y++) {
         this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, true).setValue(IronBarsBlock.SOUTH, true), 4, y, 4, chunkBB);
         this.placeBlock(
            level,
            Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, true).setValue(IronBarsBlock.SOUTH, true).setValue(IronBarsBlock.EAST, true),
            4,
            y,
            5,
            chunkBB
         );
         this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, true).setValue(IronBarsBlock.SOUTH, true), 4, y, 6, chunkBB);
         this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, true).setValue(IronBarsBlock.EAST, true), 5, y, 5, chunkBB);
         this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, true).setValue(IronBarsBlock.EAST, true), 6, y, 5, chunkBB);
         this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, true).setValue(IronBarsBlock.EAST, true), 7, y, 5, chunkBB);
      }

      this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, true).setValue(IronBarsBlock.SOUTH, true), 4, 3, 2, chunkBB);
      this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, true).setValue(IronBarsBlock.SOUTH, true), 4, 3, 8, chunkBB);
      BlockState doorBottom = Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST);
      BlockState doorTop = Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST).setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
      this.placeBlock(level, doorBottom, 4, 1, 2, chunkBB);
      this.placeBlock(level, doorTop, 4, 2, 2, chunkBB);
      this.placeBlock(level, doorBottom, 4, 1, 8, chunkBB);
      this.placeBlock(level, doorTop, 4, 2, 8, chunkBB);
   }
}
