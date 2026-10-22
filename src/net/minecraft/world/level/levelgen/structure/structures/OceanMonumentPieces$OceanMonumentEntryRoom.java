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

public class OceanMonumentPieces$OceanMonumentEntryRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentEntryRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, 1, orientation, definition, 1, 1, 1);
   }

   public OceanMonumentPieces$OceanMonumentEntryRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_ENTRY_ROOM, tag);
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
      this.generateBox(level, chunkBB, 0, 3, 0, 2, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, 2, 0, 1, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 2, 0, 7, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, 1, 7, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 0, 2, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 1, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
      if (this.roomDefinition.hasOpening[Direction.NORTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 1, 7, 4, 2, 7);
      }

      if (this.roomDefinition.hasOpening[Direction.WEST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 0, 1, 3, 1, 2, 4);
      }

      if (this.roomDefinition.hasOpening[Direction.EAST.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 6, 1, 3, 7, 2, 4);
      }
   }
}
