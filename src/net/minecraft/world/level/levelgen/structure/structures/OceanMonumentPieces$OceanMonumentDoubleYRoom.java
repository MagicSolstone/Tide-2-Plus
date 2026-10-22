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

public class OceanMonumentPieces$OceanMonumentDoubleYRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentDoubleYRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, 1, orientation, definition, 1, 2, 1);
   }

   public OceanMonumentPieces$OceanMonumentDoubleYRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_DOUBLE_Y_ROOM, tag);
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
      if (this.roomDefinition.index / 25 > 0) {
         this.generateDefaultFloor(level, chunkBB, 0, 0, this.roomDefinition.hasOpening[Direction.DOWN.get3DDataValue()]);
      }

      OceanMonumentPieces$RoomDefinition above = this.roomDefinition.connections[Direction.UP.get3DDataValue()];
      if (above.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 8, 1, 6, 8, 6, BASE_GRAY);
      }

      this.generateBox(level, chunkBB, 0, 4, 0, 0, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 7, 4, 0, 7, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 4, 0, 6, 4, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 4, 7, 6, 4, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 2, 4, 1, 2, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 4, 2, 1, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 4, 1, 5, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 4, 2, 6, 4, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 2, 4, 5, 2, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 4, 5, 1, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 4, 5, 5, 4, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 4, 5, 6, 4, 5, BASE_LIGHT, BASE_LIGHT, false);
      OceanMonumentPieces$RoomDefinition definition = this.roomDefinition;

      for (int y = 1; y <= 5; y += 4) {
         int z = 0;
         if (definition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
            this.generateBox(level, chunkBB, 2, y, z, 2, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 5, y, z, 5, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 3, y + 2, z, 4, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
         } else {
            this.generateBox(level, chunkBB, 0, y, z, 7, y + 2, z, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 0, y + 1, z, 7, y + 1, z, BASE_GRAY, BASE_GRAY, false);
         }

         int var13 = 7;
         if (definition.hasOpening[Direction.NORTH.get3DDataValue()]) {
            this.generateBox(level, chunkBB, 2, y, var13, 2, y + 2, var13, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 5, y, var13, 5, y + 2, var13, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 3, y + 2, var13, 4, y + 2, var13, BASE_LIGHT, BASE_LIGHT, false);
         } else {
            this.generateBox(level, chunkBB, 0, y, var13, 7, y + 2, var13, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 0, y + 1, var13, 7, y + 1, var13, BASE_GRAY, BASE_GRAY, false);
         }

         int x = 0;
         if (definition.hasOpening[Direction.WEST.get3DDataValue()]) {
            this.generateBox(level, chunkBB, x, y, 2, x, y + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, x, y, 5, x, y + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, x, y + 2, 3, x, y + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
         } else {
            this.generateBox(level, chunkBB, x, y, 0, x, y + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, x, y + 1, 0, x, y + 1, 7, BASE_GRAY, BASE_GRAY, false);
         }

         int var14 = 7;
         if (definition.hasOpening[Direction.EAST.get3DDataValue()]) {
            this.generateBox(level, chunkBB, var14, y, 2, var14, y + 2, 2, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, var14, y, 5, var14, y + 2, 5, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, var14, y + 2, 3, var14, y + 2, 4, BASE_LIGHT, BASE_LIGHT, false);
         } else {
            this.generateBox(level, chunkBB, var14, y, 0, var14, y + 2, 7, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, var14, y + 1, 0, var14, y + 1, 7, BASE_GRAY, BASE_GRAY, false);
         }

         definition = above;
      }
   }
}
