package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

abstract class StrongholdPieces$StrongholdPiece extends StructurePiece {
   protected StrongholdPieces$StrongholdPiece$SmallDoorType entryDoor = StrongholdPieces$StrongholdPiece$SmallDoorType.OPENING;

   protected StrongholdPieces$StrongholdPiece(final StructurePieceType type, final int genDepth, final BoundingBox boundingBox) {
      super(type, genDepth, boundingBox);
   }

   public StrongholdPieces$StrongholdPiece(final StructurePieceType type, final CompoundTag tag) {
      super(type, tag);
      this.entryDoor = tag.<StrongholdPieces$StrongholdPiece$SmallDoorType>read("EntryDoor", StrongholdPieces$StrongholdPiece$SmallDoorType.LEGACY_CODEC)
         .orElseThrow();
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      tag.store("EntryDoor", StrongholdPieces$StrongholdPiece$SmallDoorType.LEGACY_CODEC, this.entryDoor);
   }

   protected void generateSmallDoor(
      final WorldGenLevel level,
      final RandomSource random,
      final BoundingBox chunkBB,
      final StrongholdPieces$StrongholdPiece$SmallDoorType doorType,
      final int footX,
      final int footY,
      final int footZ
   ) {
      switch (doorType) {
         case OPENING:
            this.generateBox(level, chunkBB, footX, footY, footZ, footX + 3 - 1, footY + 3 - 1, footZ, CAVE_AIR, CAVE_AIR, false);
            break;
         case WOOD_DOOR:
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.OAK_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
            break;
         case GRATES:
            this.placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY + 1, footZ, chunkBB);
            this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, true), footX, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, true), footX, footY + 1, footZ, chunkBB);
            this.placeBlock(
               level,
               Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, true).setValue(IronBarsBlock.WEST, true),
               footX,
               footY + 2,
               footZ,
               chunkBB
            );
            this.placeBlock(
               level,
               Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, true).setValue(IronBarsBlock.WEST, true),
               footX + 1,
               footY + 2,
               footZ,
               chunkBB
            );
            this.placeBlock(
               level,
               Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, true).setValue(IronBarsBlock.WEST, true),
               footX + 2,
               footY + 2,
               footZ,
               chunkBB
            );
            this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, true), footX + 2, footY + 1, footZ, chunkBB);
            this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.EAST, true), footX + 2, footY, footZ, chunkBB);
            break;
         case IRON_DOOR:
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
            this.placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.IRON_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
            this.placeBlock(level, Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
            this.placeBlock(
               level, Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.NORTH), footX + 2, footY + 1, footZ + 1, chunkBB
            );
            this.placeBlock(
               level, Blocks.STONE_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.SOUTH), footX + 2, footY + 1, footZ - 1, chunkBB
            );
      }
   }

   protected StrongholdPieces$StrongholdPiece$SmallDoorType randomSmallDoor(final RandomSource random) {
      int selection = random.nextInt(5);
      switch (selection) {
         case 0:
         case 1:
         default:
            return StrongholdPieces$StrongholdPiece$SmallDoorType.OPENING;
         case 2:
            return StrongholdPieces$StrongholdPiece$SmallDoorType.WOOD_DOOR;
         case 3:
            return StrongholdPieces$StrongholdPiece$SmallDoorType.GRATES;
         case 4:
            return StrongholdPieces$StrongholdPiece$SmallDoorType.IRON_DOOR;
      }
   }

   @Nullable
   protected StructurePiece generateSmallDoorChildForward(
      final StrongholdPieces$StartPiece startPiece, final StructurePiecesBuilder builder, final RandomSource random, final int xOff, final int yOff
   ) {
      Direction orientation = this.getOrientation();
      if (orientation != null) {
         switch (orientation) {
            case NORTH:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + xOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() - 1,
                  orientation,
                  this.getGenDepth()
               );
            case SOUTH:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + xOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.maxZ() + 1,
                  orientation,
                  this.getGenDepth()
               );
            case WEST:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() - 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + xOff,
                  orientation,
                  this.getGenDepth()
               );
            case EAST:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.maxX() + 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + xOff,
                  orientation,
                  this.getGenDepth()
               );
         }
      }

      return null;
   }

   @Nullable
   protected StructurePiece generateSmallDoorChildLeft(
      final StrongholdPieces$StartPiece startPiece, final StructurePiecesBuilder builder, final RandomSource random, final int yOff, final int zOff
   ) {
      Direction orientation = this.getOrientation();
      if (orientation != null) {
         switch (orientation) {
            case NORTH:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() - 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.WEST,
                  this.getGenDepth()
               );
            case SOUTH:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() - 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.WEST,
                  this.getGenDepth()
               );
            case WEST:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() - 1,
                  Direction.NORTH,
                  this.getGenDepth()
               );
            case EAST:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() - 1,
                  Direction.NORTH,
                  this.getGenDepth()
               );
         }
      }

      return null;
   }

   @Nullable
   protected StructurePiece generateSmallDoorChildRight(
      final StrongholdPieces$StartPiece startPiece, final StructurePiecesBuilder builder, final RandomSource random, final int yOff, final int zOff
   ) {
      Direction orientation = this.getOrientation();
      if (orientation != null) {
         switch (orientation) {
            case NORTH:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.maxX() + 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.EAST,
                  this.getGenDepth()
               );
            case SOUTH:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.maxX() + 1,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.minZ() + zOff,
                  Direction.EAST,
                  this.getGenDepth()
               );
            case WEST:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.maxZ() + 1,
                  Direction.SOUTH,
                  this.getGenDepth()
               );
            case EAST:
               return StrongholdPieces.generateAndAddPiece(
                  startPiece,
                  builder,
                  random,
                  this.boundingBox.minX() + zOff,
                  this.boundingBox.minY() + yOff,
                  this.boundingBox.maxZ() + 1,
                  Direction.SOUTH,
                  this.getGenDepth()
               );
         }
      }

      return null;
   }

   protected static boolean isOkBox(final BoundingBox box) {
      return box.minY() > 10;
   }
}
