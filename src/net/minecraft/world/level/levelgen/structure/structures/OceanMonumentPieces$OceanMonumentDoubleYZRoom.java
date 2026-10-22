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

public class OceanMonumentPieces$OceanMonumentDoubleYZRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentDoubleYZRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, 1, orientation, definition, 1, 2, 2);
   }

   public OceanMonumentPieces$OceanMonumentDoubleYZRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_YZ_ROOM, tag);
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
      OceanMonumentPieces$RoomDefinition north = this.roomDefinition.connections[Direction.NORTH.get3DDataValue()];
      OceanMonumentPieces$RoomDefinition south = this.roomDefinition;
      OceanMonumentPieces$RoomDefinition northUp = north.connections[Direction.UP.get3DDataValue()];
      OceanMonumentPieces$RoomDefinition southUp = south.connections[Direction.UP.get3DDataValue()];
      if (this.roomDefinition.index / 25 > 0) {
         this.generateDefaultFloor(level, chunkBB, 0, 8, north.hasOpening[Direction.DOWN.get3DDataValue()]);
         this.generateDefaultFloor(level, chunkBB, 0, 0, south.hasOpening[Direction.DOWN.get3DDataValue()]);
      }

      if (southUp.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 8, 1, 6, 8, 7, BASE_GRAY);
      }

      if (northUp.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 8, 8, 6, 8, 14, BASE_GRAY);
      }

      for (int y = 1; y <= 7; y++) {
         BlockState block = BASE_LIGHT;
         if (y == 2 || y == 6) {
            block = BASE_GRAY;
         }

         this.generateBox(level, chunkBB, 0, y, 0, 0, y, 15, block, block, false);
         this.generateBox(level, chunkBB, 7, y, 0, 7, y, 15, block, block, false);
         this.generateBox(level, chunkBB, 1, y, 0, 6, y, 0, block, block, false);
         this.generateBox(level, chunkBB, 1, y, 15, 6, y, 15, block, block, false);
      }

      for (int y = 1; y <= 7; y++) {
         BlockState block = BASE_BLACK;
         if (y == 2 || y == 6) {
            block = LAMP_BLOCK;
         }

         this.generateBox(level, chunkBB, 3, y, 7, 4, y, 8, block, block, false);
      }

      if (south.hasOpening[Direction.SOUTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
      }

      if (south.hasOpening[Direction.EAST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 7, 1, 3, 7, 2, 4);
      }

      if (south.hasOpening[Direction.WEST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 0, 1, 3, 0, 2, 4);
      }

      if (north.hasOpening[Direction.NORTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 1, 15, 4, 2, 15);
      }

      if (north.hasOpening[Direction.WEST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 0, 1, 11, 0, 2, 12);
      }

      if (north.hasOpening[Direction.EAST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 7, 1, 11, 7, 2, 12);
      }

      if (southUp.hasOpening[Direction.SOUTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 5, 0, 4, 6, 0);
      }

      if (southUp.hasOpening[Direction.EAST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 7, 5, 3, 7, 6, 4);
         this.generateBox(level, chunkBB, 5, 4, 2, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 6, 1, 2, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 6, 1, 5, 6, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
      }

      if (southUp.hasOpening[Direction.WEST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 0, 5, 3, 0, 6, 4);
         this.generateBox(level, chunkBB, 1, 4, 2, 2, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 1, 1, 2, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 1, 1, 5, 1, 3, 5, BASE_LIGHT, BASE_LIGHT, false);
      }

      if (northUp.hasOpening[Direction.NORTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 5, 15, 4, 6, 15);
      }

      if (northUp.hasOpening[Direction.WEST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 0, 5, 11, 0, 6, 12);
         this.generateBox(level, chunkBB, 1, 4, 10, 2, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 1, 1, 10, 1, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 1, 1, 13, 1, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
      }

      if (northUp.hasOpening[Direction.EAST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 7, 5, 11, 7, 6, 12);
         this.generateBox(level, chunkBB, 5, 4, 10, 6, 4, 13, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 6, 1, 10, 6, 3, 10, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 6, 1, 13, 6, 3, 13, BASE_LIGHT, BASE_LIGHT, false);
      }
   }
}
