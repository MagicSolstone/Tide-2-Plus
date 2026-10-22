package net.minecraft.world.level.levelgen.structure.structures;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

class EndCityPieces$3 implements EndCityPieces$SectionGenerator {
   public boolean shipCreated;

   @Override
   public void init() {
      this.shipCreated = false;
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
      Rotation rotation = parent.placeSettings().getRotation();
      int bridgeLength = random.nextInt(4) + 1;
      EndCityPieces$EndCityPiece lastPiece = EndCityPieces.addHelper(
         pieces, EndCityPieces.addPiece(structureTemplateManager, parent, new BlockPos(0, 0, -4), "bridge_piece", rotation, true)
      );
      lastPiece.setGenDepth(-1);
      int nextY = 0;

      for (int i = 0; i < bridgeLength; i++) {
         if (random.nextBoolean()) {
            lastPiece = EndCityPieces.addHelper(
               pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -4), "bridge_piece", rotation, true)
            );
            nextY = 0;
         } else {
            if (random.nextBoolean()) {
               lastPiece = EndCityPieces.addHelper(
                  pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -4), "bridge_steep_stairs", rotation, true)
               );
            } else {
               lastPiece = EndCityPieces.addHelper(
                  pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -8), "bridge_gentle_stairs", rotation, true)
               );
            }

            nextY = 4;
         }
      }

      if (!this.shipCreated && random.nextInt(10 - genDepth) == 0) {
         EndCityPieces.addHelper(
            pieces,
            EndCityPieces.addPiece(
               structureTemplateManager, lastPiece, new BlockPos(-8 + random.nextInt(8), nextY, -70 + random.nextInt(10)), "ship", rotation, true
            )
         );
         this.shipCreated = true;
      } else if (!EndCityPieces.recursiveChildren(
         structureTemplateManager, EndCityPieces.HOUSE_TOWER_GENERATOR, genDepth + 1, lastPiece, new BlockPos(-3, nextY + 1, -11), pieces, random
      )) {
         return false;
      }

      lastPiece = EndCityPieces.addHelper(
         pieces,
         EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(4, nextY, 0), "bridge_end", rotation.getRotated(Rotation.CLOCKWISE_180), true)
      );
      lastPiece.setGenDepth(-1);
      return true;
   }
}
