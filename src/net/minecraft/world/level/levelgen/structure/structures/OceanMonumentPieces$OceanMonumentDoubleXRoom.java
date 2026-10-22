package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class OceanMonumentPieces$OceanMonumentDoubleXRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentDoubleXRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, 1, orientation, definition, 2, 1, 1);
   }

   public OceanMonumentPieces$OceanMonumentDoubleXRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_X_ROOM, tag);
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
      if (this.roomDefinition.index / 25 > 0) {
         this.generateDefaultFloor(level, chunkBB, 8, 0, east.hasOpening[Direction.DOWN.get3DDataValue()]);
         this.generateDefaultFloor(level, chunkBB, 0, 0, west.hasOpening[Direction.DOWN.get3DDataValue()]);
      }

      if (west.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 7, 4, 6, BASE_GRAY);
      }

      if (east.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 8, 4, 1, 14, 4, 6, BASE_GRAY);
      }

      this.generateBox(level, chunkBB, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 15, 3, 0, 15, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 0, 15, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 7, 14, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, 2, 0, 0, 2, 7, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 15, 2, 0, 15, 2, 7, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 1, 2, 0, 15, 2, 0, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 1, 2, 7, 14, 2, 7, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 15, 1, 0, 15, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 0, 15, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 1, 0, 10, 1, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 2, 0, 9, 2, 3, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 5, 3, 0, 10, 3, 4, BASE_LIGHT, BASE_LIGHT, false);
      this.placeBlock(level, LAMP_BLOCK, 6, 2, 3, chunkBB);
      this.placeBlock(level, LAMP_BLOCK, 9, 2, 3, chunkBB);
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
   }
}
