package net.minecraft.world.level.levelgen.structure.structures;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

class EndCityPieces$1 implements EndCityPieces$SectionGenerator {
   @Override
   public void init() {
   }

   @Override
   public boolean generate(
      final StructureTemplateManager structureTemplateManager,
      final int genDepth,
      final EndCityPieces$EndCityPiece parent,
      final BlockPos offset,
      final List<StructurePiece> pieces,
      final RandomSource random
   ) {
      if (genDepth > 8) {
         return false;
      } else {
         Rotation rotation = parent.placeSettings().getRotation();
         EndCityPieces$EndCityPiece lastPiece = EndCityPieces.addHelper(
            pieces, EndCityPieces.addPiece(structureTemplateManager, parent, offset, "base_floor", rotation, true)
         );
         int numFloors = random.nextInt(3);
         if (numFloors == 0) {
            lastPiece = EndCityPieces.addHelper(
               pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "base_roof", rotation, true)
            );
         } else if (numFloors == 1) {
            lastPiece = EndCityPieces.addHelper(
               pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false)
            );
            lastPiece = EndCityPieces.addHelper(
               pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "second_roof", rotation, false)
            );
            EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
         } else if (numFloors == 2) {
            lastPiece = EndCityPieces.addHelper(
               pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false)
            );
            lastPiece = EndCityPieces.addHelper(
               pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "third_floor_2", rotation, false)
            );
            lastPiece = EndCityPieces.addHelper(
               pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "third_roof", rotation, true)
            );
            EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
         }

         return true;
      }
   }
}
