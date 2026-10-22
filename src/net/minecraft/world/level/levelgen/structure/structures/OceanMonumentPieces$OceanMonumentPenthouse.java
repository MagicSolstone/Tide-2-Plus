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

public class OceanMonumentPieces$OceanMonumentPenthouse extends OceanMonumentPieces$OceanMonumentPiece {
   public OceanMonumentPieces$OceanMonumentPenthouse(final Direction orientation, final BoundingBox boundingBox) {
      super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, orientation, 1, boundingBox);
   }

   public OceanMonumentPieces$OceanMonumentPenthouse(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_PENTHOUSE, tag);
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
      this.generateBox(level, chunkBB, 2, -1, 2, 11, -1, 11, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 0, -1, 0, 1, -1, 11, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 12, -1, 0, 13, -1, 11, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 2, -1, 0, 11, -1, 1, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 2, -1, 12, 11, -1, 13, BASE_GRAY, BASE_GRAY, false);
      this.generateBox(level, chunkBB, 0, 0, 0, 0, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 13, 0, 0, 13, 0, 13, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 0, 0, 12, 0, 0, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 1, 0, 13, 12, 0, 13, BASE_LIGHT, BASE_LIGHT, false);

      for (int i = 2; i <= 11; i += 3) {
         this.placeBlock(level, LAMP_BLOCK, 0, 0, i, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 13, 0, i, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, i, 0, 0, chunkBB);
      }

      this.generateBox(level, chunkBB, 2, 0, 3, 4, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 9, 0, 3, 11, 0, 9, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 4, 0, 9, 9, 0, 11, BASE_LIGHT, BASE_LIGHT, false);
      this.placeBlock(level, BASE_LIGHT, 5, 0, 8, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 8, 0, 8, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 10, 0, 10, chunkBB);
      this.placeBlock(level, BASE_LIGHT, 3, 0, 10, chunkBB);
      this.generateBox(level, chunkBB, 3, 0, 3, 3, 0, 7, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 10, 0, 3, 10, 0, 7, BASE_BLACK, BASE_BLACK, false);
      this.generateBox(level, chunkBB, 6, 0, 10, 7, 0, 10, BASE_BLACK, BASE_BLACK, false);
      int x = 3;

      for (int i = 0; i < 2; i++) {
         for (int z = 2; z <= 8; z += 3) {
            this.generateBox(level, chunkBB, x, 0, z, x, 2, z, BASE_LIGHT, BASE_LIGHT, false);
         }

         x = 10;
      }

      this.generateBox(level, chunkBB, 5, 0, 10, 5, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 8, 0, 10, 8, 2, 10, BASE_LIGHT, BASE_LIGHT, false);
      this.generateBox(level, chunkBB, 6, -1, 7, 7, -1, 8, BASE_BLACK, BASE_BLACK, false);
      this.generateWaterBox(level, chunkBB, 6, -1, 3, 7, -1, 4);
      this.spawnElder(level, chunkBB, 6, 1, 6);
   }
}
