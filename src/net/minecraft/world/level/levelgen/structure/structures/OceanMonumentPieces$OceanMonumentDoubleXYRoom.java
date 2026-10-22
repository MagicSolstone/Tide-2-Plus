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
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class OceanMonumentPieces$OceanMonumentDoubleXYRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentDoubleXYRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, 1, orientation, definition, 2, 2, 1);
   }

   public OceanMonumentPieces$OceanMonumentDoubleXYRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_XY_ROOM, tag);
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
      OceanMonumentPieces$RoomDefinition east = this.roomDefinition.connections[Direction.EAST.get3DDataValue()];
      OceanMonumentPieces$RoomDefinition west = this.roomDefinition;
      OceanMonumentPieces$RoomDefinition westUp = west.connections[Direction.UP.get3DDataValue()];
      OceanMonumentPieces$RoomDefinition eastUp = east.connections[Direction.UP.get3DDataValue()];
      if (this.roomDefinition.index / 25 > 0) {
         this.generateDefaultFloor(level, chunkBB, 8, 0, east.hasOpening[Direction.DOWN.get3DDataValue()]);
         this.generateDefaultFloor(level, chunkBB, 0, 0, west.hasOpening[Direction.DOWN.get3DDataValue()]);
      }

      if (westUp.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 8, 1, 7, 8, 6, BASE_GRAY);
      }

      if (eastUp.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 8, 8, 1, 14, 8, 6, BASE_GRAY);
      }

      for (int y = 1; y <= 7; y++) {
         BlockState block = BASE_LIGHT;
         if (y == 2 || y == 6) {
            block = BASE_GRAY;
         }

         this.generateBox(level, chunkBB, 0, y, 0, 0, y, 7, block, block, false);
         this.generateBox(level, chunkBB, 15, y, 0, 15, y, 7, block, block, false);
         this.generateBox(level, chunkBB, 1, y, 0, 15, y, 0, block, block, false);
         this.generateBox(level, chunkBB, 1, y, 7, 14, y, 7, block, block, false);
      }

      this.generateBox(level, chunkBB, 2, 1, 3, 2, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 3, 1, 2, 4, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 3, 1, 5, 4, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 13, 1, 3, 13, 7, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 11, 1, 2, 12, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 11, 1, 5, 12, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 1, 3, 5, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 1, 3, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 7, 2, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 5, 2, 5, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 5, 2, 10, 7, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 5, 5, 5, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 5, 5, 10, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.placeBlock(level, BASE_LIGHT, 6, 6, 2, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 9, 6, 2, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 6, 6, 5, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 9, 6, 5, chunkBB);
      this.generateBox(level, chunkBB, 5, 4, 3, 6, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 9, 4, 3, 10, 4, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.placeBlock(level, LAMP_BLOCK, 5, 4, 2, chunkBB);
      this.placeBlock(level, LAMP_BLOCK, 5, 4, 5, chunkBB);
      this.placeBlock(level, LAMP_BLOCK, 10, 4, 2, chunkBB);
      this.placeBlock(level, LAMP_BLOCK, 10, 4, 5, chunkBB);
      if (west.hasOpening[Direction.SOUTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
      }

      if (west.hasOpening[Direction.NORTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 1, 7, 4, 2, 7);
      }

      if (west.hasOpening[Direction.WEST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
      }

      if (east.hasOpening[Direction.SOUTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 11, 1, 0, 12, 2, 0);
      }

      if (east.hasOpening[Direction.NORTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 11, 1, 7, 12, 2, 7);
      }

      if (east.hasOpening[Direction.EAST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 15, 1, 3, 15, 2, 4);
      }

      if (westUp.hasOpening[Direction.SOUTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 5, 0, 4, 6, 0);
      }

      if (westUp.hasOpening[Direction.NORTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 5, 7, 4, 6, 7);
      }

      if (westUp.hasOpening[Direction.WEST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 0, 5, 3, 0, 6, 4);
      }

      if (eastUp.hasOpening[Direction.SOUTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 11, 5, 0, 12, 6, 0);
      }

      if (eastUp.hasOpening[Direction.NORTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 11, 5, 7, 12, 6, 7);
      }

      if (eastUp.hasOpening[Direction.EAST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 15, 5, 3, 15, 6, 4);
      }
   }
}
