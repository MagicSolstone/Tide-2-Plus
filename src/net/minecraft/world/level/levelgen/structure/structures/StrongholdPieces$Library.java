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
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$Library extends StrongholdPieces$StrongholdPiece {
   protected static final int WIDTH = 14;
   protected static final int HEIGHT = 6;
   protected static final int TALL_HEIGHT = 11;
   protected static final int DEPTH = 15;
   private final boolean isTall;

   public StrongholdPieces$Library(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_LIBRARY, genDepth, boundingBox);
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
      this.isTall = boundingBox.getYSpan() > 6;
   }

   public StrongholdPieces$Library(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_LIBRARY, tag);
      this.isTall = tag.getBooleanOr("Tall", false);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("Tall", this.isTall);
   }

   @Nullable
   public static StrongholdPieces$Library createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 11, 15, direction);
      if (!isOkBox(box) || builder.findCollisionPiece(box) != null) {
         box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 6, 15, direction);
         if (!isOkBox(box) || builder.findCollisionPiece(box) != null) {
            return null;
         }
      }

      return new StrongholdPieces$Library(genDepth, random, box, direction);
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
      int currentHeight = 11;
      if (!this.isTall) {
         currentHeight = 6;
      }

      this.generateBox(level, chunkBB, 0, 0, 0, 13, currentHeight - 1, 14, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
      this.generateMaybeBox(
         level, chunkBB, random, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false
      );
      int bookLeft = 1;
      int bookRight = 12;

      for (int d = 1; d <= 13; d++) {
         if ((d - 1) % 4 == 0) {
            this.generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
            this.generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
            this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), 2, 3, d, chunkBB);
            this.placeBlock(level, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST), 11, 3, d, chunkBB);
            if (this.isTall) {
               this.generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
               this.generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
            }
         } else {
            this.generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
            this.generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
            if (this.isTall) {
               this.generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
               this.generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
            }
         }
      }

      for (int dx = 3; dx < 12; dx += 2) {
         this.generateBox(level, chunkBB, 3, 1, dx, 4, 3, dx, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
         this.generateBox(level, chunkBB, 6, 1, dx, 7, 3, dx, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
         this.generateBox(level, chunkBB, 9, 1, dx, 10, 3, dx, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
      }

      if (this.isTall) {
         this.generateBox(level, chunkBB, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
         this.generateBox(level, chunkBB, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
         this.generateBox(level, chunkBB, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
         this.generateBox(level, chunkBB, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
         this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, chunkBB);
         this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, chunkBB);
         this.placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, chunkBB);
         BlockState weFence = Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, true).setValue(FenceBlock.EAST, true);
         BlockState nsFence = Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.SOUTH, true);
         this.generateBox(level, chunkBB, 3, 6, 3, 3, 6, 11, nsFence, nsFence, false);
         this.generateBox(level, chunkBB, 10, 6, 3, 10, 6, 9, nsFence, nsFence, false);
         this.generateBox(level, chunkBB, 4, 6, 2, 9, 6, 2, weFence, weFence, false);
         this.generateBox(level, chunkBB, 4, 6, 12, 7, 6, 12, weFence, weFence, false);
         this.placeBlock(level, Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.EAST, true), 3, 6, 2, chunkBB);
         this.placeBlock(level, Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, true).setValue(FenceBlock.EAST, true), 3, 6, 12, chunkBB);
         this.placeBlock(level, Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.WEST, true), 10, 6, 2, chunkBB);

         for (int i = 0; i <= 2; i++) {
            this.placeBlock(
               level, Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.SOUTH, true).setValue(FenceBlock.WEST, true), 8 + i, 6, 12 - i, chunkBB
            );
            if (i != 2) {
               this.placeBlock(
                  level, Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.NORTH, true).setValue(FenceBlock.EAST, true), 8 + i, 6, 11 - i, chunkBB
               );
            }
         }

         BlockState ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
         this.placeBlock(level, ladder, 10, 1, 13, chunkBB);
         this.placeBlock(level, ladder, 10, 2, 13, chunkBB);
         this.placeBlock(level, ladder, 10, 3, 13, chunkBB);
         this.placeBlock(level, ladder, 10, 4, 13, chunkBB);
         this.placeBlock(level, ladder, 10, 5, 13, chunkBB);
         this.placeBlock(level, ladder, 10, 6, 13, chunkBB);
         this.placeBlock(level, ladder, 10, 7, 13, chunkBB);
         int x = 7;
         int z = 7;
         BlockState eFence = Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.EAST, true);
         this.placeBlock(level, eFence, 6, 9, 7, chunkBB);
         BlockState wFence = Blocks.OAK_FENCE.defaultBlockState().setValue(FenceBlock.WEST, true);
         this.placeBlock(level, wFence, 7, 9, 7, chunkBB);
         this.placeBlock(level, eFence, 6, 8, 7, chunkBB);
         this.placeBlock(level, wFence, 7, 8, 7, chunkBB);
         BlockState nsweFence = nsFence.setValue(FenceBlock.WEST, true).setValue(FenceBlock.EAST, true);
         this.placeBlock(level, nsweFence, 6, 7, 7, chunkBB);
         this.placeBlock(level, nsweFence, 7, 7, 7, chunkBB);
         this.placeBlock(level, eFence, 5, 7, 7, chunkBB);
         this.placeBlock(level, wFence, 8, 7, 7, chunkBB);
         this.placeBlock(level, eFence.setValue(FenceBlock.NORTH, true), 6, 7, 6, chunkBB);
         this.placeBlock(level, eFence.setValue(FenceBlock.SOUTH, true), 6, 7, 8, chunkBB);
         this.placeBlock(level, wFence.setValue(FenceBlock.NORTH, true), 7, 7, 6, chunkBB);
         this.placeBlock(level, wFence.setValue(FenceBlock.SOUTH, true), 7, 7, 8, chunkBB);
         BlockState torch = Blocks.TORCH.defaultBlockState();
         this.placeBlock(level, torch, 5, 8, 7, chunkBB);
         this.placeBlock(level, torch, 8, 8, 7, chunkBB);
         this.placeBlock(level, torch, 6, 8, 6, chunkBB);
         this.placeBlock(level, torch, 6, 8, 8, chunkBB);
         this.placeBlock(level, torch, 7, 8, 6, chunkBB);
         this.placeBlock(level, torch, 7, 8, 8, chunkBB);
      }

      this.createChest(level, chunkBB, random, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
      if (this.isTall) {
         this.placeBlock(level, CAVE_AIR, 12, 9, 1, chunkBB);
         this.createChest(level, chunkBB, random, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
      }
   }
}
