package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

class EndCityPieces$4 implements EndCityPieces$SectionGenerator {
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
         pieces, EndCityPieces.addPiece(structureTemplateManager, parent, new BlockPos(-3, 4, -3), "fat_tower_base", rotation, true)
      );
      lastPiece = EndCityPieces.addHelper(
         pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 4, 0), "fat_tower_middle", rotation, true)
      );

      for (int i = 0; i < 2 && random.nextInt(3) != 0; i++) {
         lastPiece = EndCityPieces.addHelper(
            pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 8, 0), "fat_tower_middle", rotation, true)
         );

         for (Pair<Rotation, BlockPos> bridge : EndCityPieces.FAT_TOWER_BRIDGES) {
            if (random.nextBoolean()) {
               EndCityPieces$EndCityPiece bridgeStart = EndCityPieces.addHelper(
                  pieces,
                  EndCityPieces.addPiece(
                     structureTemplateManager, lastPiece, (BlockPos)bridge.getSecond(), "bridge_end", rotation.getRotated((Rotation)bridge.getFirst()), true
                  )
               );
               EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, genDepth + 1, bridgeStart, null, pieces, random);
            }
         }
      }

      lastPiece = EndCityPieces.addHelper(
         pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-2, 8, -2), "fat_tower_top", rotation, true)
      );
      return true;
   }
}
