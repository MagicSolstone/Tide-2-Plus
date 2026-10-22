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

public class OceanMonumentPieces$OceanMonumentCoreRoom extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentCoreRoom(final Direction orientation, final OceanMonumentPieces$RoomDefinition definition) {
      super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, 1, orientation, definition, 2, 2, 2);
   }

   public OceanMonumentPieces$OceanMonumentCoreRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_CORE_ROOM, tag);
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
      this.generateBoxOnFillOnly(level, chunkBB, 1, 8, 0, 14, 8, 14, BASE_GRAY);
      int y = 7;
      BlockState block = BASE_LIGHT;
      this.generateBox(level, chunkBB, 0, 7, 0, 0, 7, 15, block, block, false);
      this.generateBox(level, chunkBB, 15, 7, 0, 15, 7, 15, block, block, false);
      this.generateBox(level, chunkBB, 1, 7, 0, 15, 7, 0, block, block, false);
      this.generateBox(level, chunkBB, 1, 7, 15, 14, 7, 15, block, block, false);

      for (int yx = 1; yx <= 6; yx++) {
         block = BASE_LIGHT;
         if (yx == 2 || yx == 6) {
            block = BASE_GRAY;
         }

         for (int x = 0; x <= 15; x += 15) {
            this.generateBox(level, chunkBB, x, yx, 0, x, yx, 1, block, block, false);
            this.generateBox(level, chunkBB, x, yx, 6, x, yx, 9, block, block, false);
            this.generateBox(level, chunkBB, x, yx, 14, x, yx, 15, block, block, false);
         }

         this.generateBox(level, chunkBB, 1, yx, 0, 1, yx, 0, block, block, false);
         this.generateBox(level, chunkBB, 6, yx, 0, 9, yx, 0, block, block, false);
         this.generateBox(level, chunkBB, 14, yx, 0, 14, yx, 0, block, block, false);
         this.generateBox(level, chunkBB, 1, yx, 15, 14, yx, 15, block, block, false);
      }

      this.generateBox(level, chunkBB, 6, 3, 6, 9, 6, 9, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);

      for (int yx = 3; yx <= 6; yx += 3) {
         for (int x = 6; x <= 9; x += 3) {
            this.placeBlock(level, LAMP_BLOCK, x, yx, 6, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, x, yx, 9, chunkBB);
         }
      }

      this.generateBox(level, chunkBB, 5, 1, 6, 5, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 1, 9, 5, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 1, 6, 10, 2, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 1, 9, 10, 2, 9, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 1, 5, 6, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 9, 1, 5, 9, 2, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, 1, 10, 6, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 9, 1, 10, 9, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 2, 5, 5, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 2, 10, 5, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 2, 5, 10, 6, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 2, 10, 10, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 7, 1, 5, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 7, 1, 10, 7, 6, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 5, 7, 9, 5, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 10, 7, 9, 10, 7, 14, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 7, 5, 6, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 7, 10, 6, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 9, 7, 5, 14, 7, 5, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 9, 7, 10, 14, 7, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 2, 1, 2, 2, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 3, 1, 2, 3, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 13, 1, 2, 13, 1, 3, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 12, 1, 2, 12, 1, 2, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 2, 1, 12, 2, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 3, 1, 13, 3, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 13, 1, 12, 13, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 12, 1, 13, 12, 1, 13, BASE_LIGHT, BASE_LIGHT, false);
   }
}
