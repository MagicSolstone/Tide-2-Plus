package net.minecraft.world.level.levelgen.structure.structures;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class WoodlandMansionPieces {
   public static void generateMansion(
      final StructureTemplateManager structureTemplateManager,
      final BlockPos origin,
      final Rotation rotation,
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final RandomSource random
   ) {
      WoodlandMansionPieces$MansionGrid grid = new WoodlandMansionPieces$MansionGrid(random);
      WoodlandMansionPieces$MansionPiecePlacer placer = new WoodlandMansionPieces$MansionPiecePlacer(structureTemplateManager, random);
      placer.createMansion(origin, rotation, pieces, grid);
   }
}
