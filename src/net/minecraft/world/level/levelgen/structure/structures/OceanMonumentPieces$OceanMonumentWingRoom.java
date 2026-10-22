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

public class OceanMonumentPieces$OceanMonumentWingRoom extends OceanMonumentPieces$OceanMonumentPiece {
   private int mainDesign;

   public OceanMonumentPieces$OceanMonumentWingRoom(final Direction orientation, final BoundingBox boundingBox, final int randomValue) {
      super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, orientation, 1, boundingBox);
      this.mainDesign = randomValue & 1;
   }

   public OceanMonumentPieces$OceanMonumentWingRoom(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_WING_ROOM, tag);
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
      if (this.mainDesign == 0) {
         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, 10 - i, 3 - i, 20 - i, 12 + i, 3 - i, 20, BASE_LIGHT, BASE_LIGHT, false);
         }

         this.generateBox(level, chunkBB, 7, 0, 6, 15, 0, 16, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 6, 0, 6, 6, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 16, 0, 6, 16, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 7, 1, 7, 7, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 15, 1, 7, 15, 1, 20, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 7, 1, 6, 9, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 13, 1, 6, 15, 3, 6, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 8, 1, 7, 9, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 13, 1, 7, 14, 1, 7, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 9, 0, 5, 13, 0, 5, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 10, 0, 7, 12, 0, 7, BASE_BLACK, BASE_BLACK, false);
         this.generateBox(level, chunkBB, 8, 0, 10, 8, 0, 12, BASE_BLACK, BASE_BLACK, false);
         this.generateBox(level, chunkBB, 14, 0, 10, 14, 0, 12, BASE_BLACK, BASE_BLACK, false);

         for (int z = 18; z >= 7; z -= 3) {
            this.placeBlock(level, LAMP_BLOCK, 6, 3, z, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, 16, 3, z, chunkBB);
         }

         this.placeBlock(level, LAMP_BLOCK, 10, 0, 10, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 12, 0, 10, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 10, 0, 12, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 12, 0, 12, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 8, 3, 6, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 14, 3, 6, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 4, 2, 4, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 4, 1, 4, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 4, 0, 4, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 18, 2, 4, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 18, 1, 4, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 18, 0, 4, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 4, 2, 18, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 4, 1, 18, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 4, 0, 18, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 18, 2, 18, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 18, 1, 18, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 18, 0, 18, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 9, 7, 20, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 13, 7, 20, chunkBB);
         this.generateBox(level, chunkBB, 6, 0, 21, 7, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 15, 0, 21, 16, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
         this.spawnElder(level, chunkBB, 11, 2, 16);
      } else if (this.mainDesign == 1) {
         this.generateBox(level, chunkBB, 9, 3, 18, 13, 3, 20, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 9, 0, 18, 9, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 13, 0, 18, 13, 2, 18, BASE_LIGHT, BASE_LIGHT, false);
         int x = 9;
         int z = 20;
         int y = 5;

         for (int i = 0; i < 2; i++) {
            this.placeBlock(level, BASE_LIGHT, x, 6, 20, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, x, 5, 20, chunkBB);
            this.placeBlock(level, BASE_LIGHT, x, 4, 20, chunkBB);
            x = 13;
         }

         this.generateBox(level, chunkBB, 7, 3, 7, 15, 3, 14, BASE_LIGHT, BASE_LIGHT, false);
         int var14 = 10;

         for (int i = 0; i < 2; i++) {
            this.generateBox(level, chunkBB, var14, 0, 10, var14, 6, 10, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, var14, 0, 12, var14, 6, 12, BASE_LIGHT, BASE_LIGHT, false);
            this.placeBlock(level, LAMP_BLOCK, var14, 0, 10, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, var14, 0, 12, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, var14, 4, 10, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, var14, 4, 12, chunkBB);
            var14 = 12;
         }

         var14 = 8;

         for (int i = 0; i < 2; i++) {
            this.generateBox(level, chunkBB, var14, 0, 7, var14, 2, 7, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, var14, 0, 14, var14, 2, 14, BASE_LIGHT, BASE_LIGHT, false);
            var14 = 14;
         }

         this.generateBox(level, chunkBB, 8, 3, 8, 8, 3, 13, BASE_BLACK, BASE_BLACK, false);
         this.generateBox(level, chunkBB, 14, 3, 8, 14, 3, 13, BASE_BLACK, BASE_BLACK, false);
         this.spawnElder(level, chunkBB, 11, 5, 13);
      }
   }
}
