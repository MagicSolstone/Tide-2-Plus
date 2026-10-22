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

public class OceanMonumentPieces$OceanMonumentDoubleZRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentDoubleZRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, 1, orientation, definition, 1, 1, 2);
   }

   public OceanMonumentPieces$OceanMonumentDoubleZRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Z_ROOM, tag);
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
      if (this.roomDefinition.index / 25 > 0) {
         this.generateDefaultFloor(level, chunkBB, 0, 8, north.hasOpening[Direction.DOWN.get3DDataValue()]);
         this.generateDefaultFloor(level, chunkBB, 0, 0, south.hasOpening[Direction.DOWN.get3DDataValue()]);
      }

      if (south.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 6, 4, 7, BASE_GRAY);
      }

      if (north.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 4, 8, 6, 4, 14, BASE_GRAY);
      }

      this.generateBox(level, chunkBB, 0, 3, 0, 0, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 7, 3, 0, 7, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 0, 7, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 15, 6, 3, 15, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, 2, 0, 0, 2, 15, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 7, 2, 0, 7, 2, 15, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 1, 2, 0, 7, 2, 0, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 1, 2, 15, 6, 2, 15, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 0, 1, 0, 0, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 7, 1, 0, 7, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 0, 7, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 15, 6, 1, 15, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 1, 1, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 1, 1, 6, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 1, 1, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 3, 1, 6, 3, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 13, 1, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 1, 13, 6, 1, 14, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 13, 1, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 3, 13, 6, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 2, 1, 6, 2, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 1, 6, 5, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 2, 1, 9, 2, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 1, 9, 5, 3, 9, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 3, 2, 6, 4, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 3, 2, 9, 4, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 2, 2, 7, 2, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 2, 7, 5, 2, 8, BASE_LIGHT, BASE_LIGHT, false);
      this.placeBlock(level, LAMP_BLOCK, 2, 2, 5, chunkBB);
      this.placeBlock(level, LAMP_BLOCK, 5, 2, 5, chunkBB);
      this.placeBlock(level, LAMP_BLOCK, 2, 2, 10, chunkBB);
      this.placeBlock(level, LAMP_BLOCK, 5, 2, 10, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 2, 3, 5, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 5, 3, 5, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 2, 3, 10, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 5, 3, 10, chunkBB);
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
   }
}
