package net.minecraft.world.level.levelgen.structure.structures;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

interface EndCityPieces$SectionGenerator {
   void init();

   boolean generate(
      StructureTemplateManager structureTemplateManager,
      int genDepth,
      EndCityPieces$EndCityPiece parent,
      BlockPos offset,
      List<StructurePiece> pieces,
      RandomSource random
   );
}
