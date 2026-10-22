package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$RoomCrossing extends StrongholdPieces$StrongholdPiece {
   protected static final int WIDTH = 11;
   protected static final int HEIGHT = 7;
   protected static final int DEPTH = 11;
   protected final int type;

   public StrongholdPieces$RoomCrossing(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, genDepth, boundingBox);
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
      this.type = random.nextInt(5);
   }

   public StrongholdPieces$RoomCrossing(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, tag);
      this.type = tag.getIntOr("Type", 0);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putInt("Type", this.type);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateSmallDoorChildForward((StrongholdPieces$StartPiece)startPiece, builder, random, 4, 1);
      this.generateSmallDoorChildLeft((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 4);
      this.generateSmallDoorChildRight((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 4);
   }

   @Nullable
   public static StrongholdPieces$RoomCrossing createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 7, 11, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$RoomCrossing(genDepth, random, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 10, 6, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
      this.generateBox(level, chunkBB, 4, 1, 10, 6, 3, 10, CAVE_AIR, CAVE_AIR, false);
      this.generateBox(level, chunkBB, 0, 1, 4, 0, 3, 6, CAVE_AIR, CAVE_AIR, false);
      this.generateBox(level, chunkBB, 10, 1, 4, 10, 3, 6, CAVE_AIR, CAVE_AIR, false);
      switch (this.type) {
         case 0:
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
            this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST), 4, 3, 5, chunkBB);
            this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), 6, 3, 5, chunkBB);
            this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH), 5, 3, 4, chunkBB);
            this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH), 5, 3, 6, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, chunkBB);
            this.placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, chunkBB);
            break;
         case 1:
            for (int i = 0; i < 5; i++) {
               this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + i, chunkBB);
               this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + i, chunkBB);
               this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 3, chunkBB);
               this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 7, chunkBB);
            }

            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
            this.placeBlock(level, Blocks.WATER.defaultBlockState(), 5, 4, 5, chunkBB);
            break;
         case 2:
            for (int z = 1; z <= 9; z++) {
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, z, chunkBB);
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, z, chunkBB);
            }

            for (int x = 1; x <= 9; x++) {
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 1, chunkBB);
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 9, chunkBB);
            }

            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, chunkBB);
            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, chunkBB);
            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, chunkBB);
            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, chunkBB);
            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, chunkBB);
            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, chunkBB);
            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, chunkBB);
            this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, chunkBB);

            for (int y = 1; y <= 3; y++) {
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 4, chunkBB);
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 4, chunkBB);
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 6, chunkBB);
               this.placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 6, chunkBB);
            }

            this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState(), 5, 3, 5, chunkBB);

            for (int z = 2; z <= 8; z++) {
               this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, z, chunkBB);
               this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, z, chunkBB);
               if (z <= 3 || z >= 7) {
                  this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, z, chunkBB);
                  this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, z, chunkBB);
                  this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, z, chunkBB);
               }

               this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, z, chunkBB);
               this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, z, chunkBB);
            }

            BlockState ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST);
            this.placeBlock(level, ladder, 9, 1, 3, chunkBB);
            this.placeBlock(level, ladder, 9, 2, 3, chunkBB);
            this.placeBlock(level, ladder, 9, 3, 3, chunkBB);
            this.createChest(level, chunkBB, random, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING);
      }
   }
}
