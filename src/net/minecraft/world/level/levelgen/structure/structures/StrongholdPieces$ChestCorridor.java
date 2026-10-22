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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$ChestCorridor extends StrongholdPieces$StrongholdPiece {
   private static final int WIDTH = 5;
   private static final int HEIGHT = 5;
   private static final int DEPTH = 7;
   private boolean hasPlacedChest;

   public StrongholdPieces$ChestCorridor(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, genDepth, boundingBox);
      this.setOrientation(direction);
      this.entryDoor = this.randomSmallDoor(random);
   }

   public StrongholdPieces$ChestCorridor(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, tag);
      this.hasPlacedChest = tag.getBooleanOr("Chest", false);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("Chest", this.hasPlacedChest);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      this.generateSmallDoorChildForward((StrongholdPieces$StartPiece)startPiece, builder, random, 1, 1);
   }

   @Nullable
   public static StrongholdPieces$ChestCorridor createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$ChestCorridor(genDepth, random, box, direction) : null;
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
      this.generateBox(level, chunkBB, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), false);
      this.placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 1, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 5, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 2, chunkBB);
      this.placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 4, chunkBB);

      for (int z = 2; z <= 4; z++) {
         this.placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2, 1, z, chunkBB);
      }

      if (!this.hasPlacedChest && chunkBB.isInside(this.getWorldPos(3, 2, 3))) {
         this.hasPlacedChest = true;
         this.createChest(level, chunkBB, random, 3, 2, 3, BuiltInLootTables.STRONGHOLD_CORRIDOR);
      }
   }
}
