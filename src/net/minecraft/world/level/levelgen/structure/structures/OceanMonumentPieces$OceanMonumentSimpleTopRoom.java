package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class OceanMonumentPieces$OceanMonumentSimpleTopRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentSimpleTopRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, 1, orientation, definition, 1, 1, 1);
   }

   public OceanMonumentPieces$OceanMonumentSimpleTopRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_SIMPLE_TOP_ROOM, tag);
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

      if (this.roomDefinition.connections[Direction.UP.get3DDataValue()] == null) {
         this.generateBoxOnFillOnly(level, chunkBB, 1, 4, 1, 6, 4, 6, BASE_GRAY);
      }

      for (int x = 1; x <= 6; x++) {
         for (int z = 1; z <= 6; z++) {
            if (random.nextInt(3) != 0) {
               int y0 = 2 + (random.nextInt(4) == 0 ? 0 : 1);
               BlockState wetSponge = Blocks.WET_SPONGE.defaultBlockState();
               this.generateBox(level, chunkBB, x, y0, z, x, 3, z, wetSponge, wetSponge, false);
            }
         }
      }

      this.generateBox(level, chunkBB, 0, 1, 0, 0, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 7, 1, 0, 7, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 0, 6, 1, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 1, 7, 6, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, 2, 0, 0, 2, 7, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 7, 2, 0, 7, 2, 7, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 1, 2, 0, 6, 2, 0, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 1, 2, 7, 6, 2, 7, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 0, 3, 0, 0, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 7, 3, 0, 7, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 0, 6, 3, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 3, 7, 6, 3, 7, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, 1, 3, 0, 2, 4, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 7, 1, 3, 7, 2, 4, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 3, 1, 0, 4, 2, 0, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 3, 1, 7, 4, 2, 7, BASE_BLACK, BASE_BLACK, false);
      if (this.roomDefinition.hasOpening[Direction.SOUTH.get3DDataValue()]) {
         this.generateWaterBox(level, chunkBB, 3, 1, 0, 4, 2, 0);
      }
   }
}
