package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

class EndCityPieces$2 implements EndCityPieces$SectionGenerator {
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
      Rotation rotation = parent.placeSettings().getRotation();
      EndCityPieces$EndCityPiece lastPiece = EndCityPieces.addHelper(
         pieces,
         EndCityPieces.addPiece(structureTemplateManager, parent, new BlockPos(3 + random.nextInt(2), -3, 3 + random.nextInt(2)), "tower_base", rotation, true)
      );
      lastPiece = EndCityPieces.addHelper(
         pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 7, 0), "tower_piece", rotation, true)
      );
      EndCityPieces$EndCityPiece bridgePiece = random.nextInt(3) == 0 ? lastPiece : null;
      int towerHeight = 1 + random.nextInt(3);

      for (int i = 0; i < towerHeight; i++) {
         lastPiece = EndCityPieces.addHelper(
            pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 4, 0), "tower_piece", rotation, true)
         );
         if (i < towerHeight - 1 && random.nextBoolean()) {
            bridgePiece = lastPiece;
         }
      }

      if (bridgePiece != null) {
         for (Pair<Rotation, BlockPos> bridge : EndCityPieces.TOWER_BRIDGES) {
            if (random.nextBoolean()) {
               EndCityPieces$EndCityPiece bridgeStart = EndCityPieces.addHelper(
                  pieces,
                  EndCityPieces.addPiece(
                     structureTemplateManager, bridgePiece, (BlockPos)bridge.getSecond(), "bridge_end", rotation.getRotated((Rotation)bridge.getFirst()), true
                  )
               );
               EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, genDepth + 1, bridgeStart, null, pieces, random);
            }
         }

         lastPiece = EndCityPieces.addHelper(
            pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "tower_top", rotation, true)
         );
      } else {
         if (genDepth != 7) {
            return EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.FAT_TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
         }

         lastPiece = EndCityPieces.addHelper(
            pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "tower_top", rotation, true)
         );
      }

      return true;
   }
}
