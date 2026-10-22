package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class MineshaftPieces$MineShaftCrossing extends MineshaftPieces$MineShaftPiece {
   private final Direction direction;
   private final boolean isTwoFloored;

   public MineshaftPieces$MineShaftCrossing(final CompoundTag tag) {
      super(StructurePieceType.MINE_SHAFT_CROSSING, tag);
      this.isTwoFloored = tag.getBooleanOr("tf", false);
      this.direction = tag.<Direction>read("D", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("tf", this.isTwoFloored);
      tag.store("D", Direction.LEGACY_ID_CODEC_2D, this.direction);
   }

   public MineshaftPieces$MineShaftCrossing(
      final int genDepth, final BoundingBox boundingBox, @Nullable final Direction direction, final MineshaftStructure$Type type
   ) {
      super(StructurePieceType.MINE_SHAFT_CROSSING, genDepth, type, boundingBox);
      this.direction = direction;
      this.isTwoFloored = boundingBox.getYSpan() > 3;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public static BoundingBox findCrossing(
      final StructurePiecesBuilder builder, final RandomSource random, final int footX, final int footY, final int footZ, final Direction direction
   ) {
      int y1;
      if (random.nextInt(4) == 0) {
         y1 = 6;
      } else {
         y1 = 2;
      }
      BoundingBox box = switch (direction) {
         default -> new BoundingBox(-1, 0, -4, 3, y1, 0);
         case SOUTH -> new BoundingBox(-1, 0, 0, 3, y1, 4);
         case WEST -> new BoundingBox(-4, 0, -1, 0, y1, 3);
         case EAST -> new BoundingBox(0, 0, -1, 4, y1, 3);
      };
      box.move(footX, footY, footZ);
      return builder.findCollisionPiece(box) != null ? null : box;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      int depth = this.getGenDepth();
      switch (this.direction) {
         case NORTH:
         default:
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, depth
            );
            break;
         case SOUTH:
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, depth
            );
            break;
         case WEST:
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, depth
            );
            break;
         case EAST:
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, depth
            );
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, depth
            );
      }

      if (this.isTwoFloored) {
         if (random.nextBoolean()) {
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() - 1, Direction.NORTH, depth
            );
         }

         if (random.nextBoolean()) {
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.WEST, depth
            );
         }

         if (random.nextBoolean()) {
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.minZ() + 1, Direction.EAST, depth
            );
         }

         if (random.nextBoolean()) {
            MineshaftPieces.generateAndAddPiece(
               startPiece, builder, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3 + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, depth
            );
         }
      }
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
      if (!this.isInInvalidLocation(level, chunkBB)) {
         BlockState planks = this.type.getPlanksState();
         if (this.isTwoFloored) {
            this.generateBox(
               level,
               chunkBB,
               this.boundingBox.minX() + 1,
               this.boundingBox.minY(),
               this.boundingBox.minZ(),
               this.boundingBox.maxX() - 1,
               this.boundingBox.minY() + 3 - 1,
               this.boundingBox.maxZ(),
               CAVE_AIR,
               CAVE_AIR,
               false
            );
            this.generateBox(
               level,
               chunkBB,
               this.boundingBox.minX(),
               this.boundingBox.minY(),
               this.boundingBox.minZ() + 1,
               this.boundingBox.maxX(),
               this.boundingBox.minY() + 3 - 1,
               this.boundingBox.maxZ() - 1,
               CAVE_AIR,
               CAVE_AIR,
               false
            );
            this.generateBox(
               level,
               chunkBB,
               this.boundingBox.minX() + 1,
               this.boundingBox.maxY() - 2,
               this.boundingBox.minZ(),
               this.boundingBox.maxX() - 1,
               this.boundingBox.maxY(),
               this.boundingBox.maxZ(),
               CAVE_AIR,
               CAVE_AIR,
               false
            );
            this.generateBox(
               level,
               chunkBB,
               this.boundingBox.minX(),
               this.boundingBox.maxY() - 2,
               this.boundingBox.minZ() + 1,
               this.boundingBox.maxX(),
               this.boundingBox.maxY(),
               this.boundingBox.maxZ() - 1,
               CAVE_AIR,
               CAVE_AIR,
               false
            );
            this.generateBox(
               level,
               chunkBB,
               this.boundingBox.minX() + 1,
               this.boundingBox.minY() + 3,
               this.boundingBox.minZ() + 1,
               this.boundingBox.maxX() - 1,
               this.boundingBox.minY() + 3,
               this.boundingBox.maxZ() - 1,
               CAVE_AIR,
               CAVE_AIR,
               false
            );
         } else {
            this.generateBox(
               level,
               chunkBB,
               this.boundingBox.minX() + 1,
               this.boundingBox.minY(),
               this.boundingBox.minZ(),
               this.boundingBox.maxX() - 1,
               this.boundingBox.maxY(),
               this.boundingBox.maxZ(),
               CAVE_AIR,
               CAVE_AIR,
               false
            );
            this.generateBox(
               level,
               chunkBB,
               this.boundingBox.minX(),
               this.boundingBox.minY(),
               this.boundingBox.minZ() + 1,
               this.boundingBox.maxX(),
               this.boundingBox.maxY(),
               this.boundingBox.maxZ() - 1,
               CAVE_AIR,
               CAVE_AIR,
               false
            );
         }

         this.placeSupportPillar(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
         this.placeSupportPillar(level, chunkBB, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
         this.placeSupportPillar(level, chunkBB, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
         this.placeSupportPillar(level, chunkBB, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
         int y = this.boundingBox.minY() - 1;

         for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); x++) {
            for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); z++) {
               this.setPlanksBlock(level, chunkBB, planks, x, y, z);
            }
         }
      }
   }

   private void placeSupportPillar(final WorldGenLevel level, final BoundingBox chunkBB, final int x, final int y0, final int z, final int y1) {
      if (!this.getBlock(level, x, y1 + 1, z, chunkBB).isAir()) {
         this.generateBox(level, chunkBB, x, y0, z, x, y1, z, this.type.getPlanksState(), CAVE_AIR, false);
      }
   }
}
