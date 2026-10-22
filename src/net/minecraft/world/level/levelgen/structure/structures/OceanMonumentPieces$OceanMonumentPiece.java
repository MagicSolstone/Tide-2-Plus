package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public abstract class OceanMonumentPieces$OceanMonumentPiece extends StructurePiece {
   protected static final BlockState BASE_GRAY = Blocks.PRISMARINE.defaultBlockState();
   protected static final BlockState BASE_LIGHT = Blocks.PRISMARINE_BRICKS.defaultBlockState();
   protected static final BlockState BASE_BLACK = Blocks.DARK_PRISMARINE.defaultBlockState();
   protected static final BlockState DOT_DECO_DATA = BASE_LIGHT;
   protected static final BlockState LAMP_BLOCK = Blocks.SEA_LANTERN.defaultBlockState();
   protected static final boolean DO_FILL = true;
   protected static final BlockState FILL_BLOCK = Blocks.WATER.defaultBlockState();
   protected static final Set<Block> FILL_KEEP = ImmutableSet.builder()
      .add(Blocks.ICE)
      .add(Blocks.PACKED_ICE)
      .add(Blocks.BLUE_ICE)
      .add(FILL_BLOCK.getBlock())
      .build();
   protected static final int GRIDROOM_WIDTH = 8;
   protected static final int GRIDROOM_DEPTH = 8;
   protected static final int GRIDROOM_HEIGHT = 4;
   protected static final int GRID_WIDTH = 5;
   protected static final int GRID_DEPTH = 5;
   protected static final int GRID_HEIGHT = 3;
   protected static final int GRID_FLOOR_COUNT = 25;
   protected static final int GRID_SIZE = 75;
   protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
   protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
   protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
   protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
   protected static final int LEFTWING_INDEX = 1001;
   protected static final int RIGHTWING_INDEX = 1002;
   protected static final int PENTHOUSE_INDEX = 1003;
   protected OceanMonumentPieces$RoomDefinition roomDefinition;

   protected static int getRoomIndex(final int roomX, final int roomY, final int roomZ) {
      return roomY * 25 + roomZ * 5 + roomX;
   }

   public OceanMonumentPieces$OceanMonumentPiece(final StructurePieceType type, final Direction orientation, final int genDepth, final BoundingBox boundingBox) {
      super(type, genDepth, boundingBox);
      this.setOrientation(orientation);
   }

   protected OceanMonumentPieces$OceanMonumentPiece(
      final StructurePieceType type,
      final int genDepth,
      final Direction orientation,
      final OceanMonumentPieces$RoomDefinition roomDefinition,
      final int roomWidth,
      final int roomHeight,
      final int roomDepth
   ) {
      super(type, genDepth, makeBoundingBox(orientation, roomDefinition, roomWidth, roomHeight, roomDepth));
      this.setOrientation(orientation);
      this.roomDefinition = roomDefinition;
   }

   private static BoundingBox makeBoundingBox(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition roomDefinition, final int roomWidth, final int roomHeight, final int roomDepth
   ) {
      int roomIndex = roomDefinition.index;
      int roomX = roomIndex % 5;
      int roomZ = roomIndex / 5 % 5;
      int roomY = roomIndex / 25;
      BoundingBox boundingBox = makeBoundingBox(0, 0, 0, orientation, roomWidth * 8, roomHeight * 4, roomDepth * 8);
      switch (orientation) {
         case NORTH:
            boundingBox.move(roomX * 8, roomY * 4, -(roomZ + roomDepth) * 8 + 1);
            break;
         case SOUTH:
            boundingBox.move(roomX * 8, roomY * 4, roomZ * 8);
            break;
         case WEST:
            boundingBox.move(-(roomZ + roomDepth) * 8 + 1, roomY * 4, roomX * 8);
            break;
         case EAST:
         default:
            boundingBox.move(roomZ * 8, roomY * 4, roomX * 8);
      }

      return boundingBox;
   }

   public OceanMonumentPieces$OceanMonumentPiece(final StructurePieceType type, final CompoundTag tag) {
      super(type, tag);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
   }

   protected void generateWaterBox(
      final WorldGenLevel level, final BoundingBox chunkBB, final int x0, final int y0, final int z0, final int x1, final int y1, final int z1
   ) {
      for (int y = y0; y <= y1; y++) {
         for (int x = x0; x <= x1; x++) {
            for (int z = z0; z <= z1; z++) {
               BlockState block = this.getBlock(level, x, y, z, chunkBB);
               if (!FILL_KEEP.contains(block.getBlock())) {
                  if (this.getWorldY(y) >= level.getSeaLevel() && block != FILL_BLOCK) {
                     this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, y, z, chunkBB);
                  } else {
                     this.placeBlock(level, FILL_BLOCK, x, y, z, chunkBB);
                  }
               }
            }
         }
      }
   }

   protected void generateDefaultFloor(final WorldGenLevel level, final BoundingBox chunkBB, final int xOff, final int zOff, final boolean downOpening) {
      if (downOpening) {
         this.generateBox(level, chunkBB, xOff + 0, 0, zOff + 0, xOff + 2, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xOff + 5, 0, zOff + 0, xOff + 8 - 1, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xOff + 3, 0, zOff + 0, xOff + 4, 0, zOff + 2, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xOff + 3, 0, zOff + 5, xOff + 4, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xOff + 3, 0, zOff + 2, xOff + 4, 0, zOff + 2, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, xOff + 3, 0, zOff + 5, xOff + 4, 0, zOff + 5, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, xOff + 2, 0, zOff + 3, xOff + 2, 0, zOff + 4, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, xOff + 5, 0, zOff + 3, xOff + 5, 0, zOff + 4, BASE_LIGHT, BASE_LIGHT, false);
      } else {
         this.generateBox(level, chunkBB, xOff + 0, 0, zOff + 0, xOff + 8 - 1, 0, zOff + 8 - 1, BASE_GRAY, BASE_GRAY, false);
      }
   }

   protected void generateBoxOnFillOnly(
      final WorldGenLevel level,
      final BoundingBox chunkBB,
      final int x0,
      final int y0,
      final int z0,
      final int x1,
      final int y1,
      final int z1,
      final BlockState targetBlock
   ) {
      for (int y = y0; y <= y1; y++) {
         for (int x = x0; x <= x1; x++) {
            for (int z = z0; z <= z1; z++) {
               if (this.getBlock(level, x, y, z, chunkBB) == FILL_BLOCK) {
                  this.placeBlock(level, targetBlock, x, y, z, chunkBB);
               }
            }
         }
      }
   }

   protected boolean chunkIntersects(final BoundingBox chunkBB, final int x0, final int z0, final int x1, final int z1) {
      int wx0 = this.getWorldX(x0, z0);
      int wz0 = this.getWorldZ(x0, z0);
      int wx1 = this.getWorldX(x1, z1);
      int wz1 = this.getWorldZ(x1, z1);
      return chunkBB.intersects(Math.min(wx0, wx1), Math.min(wz0, wz1), Math.max(wx0, wx1), Math.max(wz0, wz1));
   }

   protected void spawnElder(final WorldGenLevel level, final BoundingBox chunkBB, final int x, final int y, final int z) {
      BlockPos pos = this.getWorldPos(x, y, z);
      if (chunkBB.isInside(pos)) {
         ElderGuardian elder = EntityTypes.ELDER_GUARDIAN.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
         if (elder != null) {
            elder.heal(elder.getMaxHealth());
            elder.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
            elder.finalizeSpawn(level, level.getCurrentDifficultyAt(elder.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            level.addFreshEntityWithPassengers(elder);
         }
      }
   }
}
