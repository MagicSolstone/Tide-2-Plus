package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class IglooPieces {
   public static final int GENERATION_HEIGHT = 90;
   private static final Identifier STRUCTURE_LOCATION_IGLOO = Identifier.withDefaultNamespace("igloo/top");
   private static final Identifier STRUCTURE_LOCATION_LADDER = Identifier.withDefaultNamespace("igloo/middle");
   private static final Identifier STRUCTURE_LOCATION_LABORATORY = Identifier.withDefaultNamespace("igloo/bottom");
   private static final Map<Identifier, BlockPos> PIVOTS = ImmutableMap.of(
      STRUCTURE_LOCATION_IGLOO, new BlockPos(3, 5, 5), STRUCTURE_LOCATION_LADDER, new BlockPos(1, 3, 1), STRUCTURE_LOCATION_LABORATORY, new BlockPos(3, 6, 7)
   );
   private static final Map<Identifier, BlockPos> OFFSETS = ImmutableMap.of(
      STRUCTURE_LOCATION_IGLOO, BlockPos.ZERO, STRUCTURE_LOCATION_LADDER, new BlockPos(2, -3, 4), STRUCTURE_LOCATION_LABORATORY, new BlockPos(0, -3, -2)
   );

   public static void addPieces(
      final StructureTemplateManager structureTemplateManager,
      final BlockPos position,
      final Rotation rotation,
      final StructurePiecesBuilder builder,
      final RandomSource random
   ) {
      if (random.nextDouble() < 0.5) {
         int depth = random.nextInt(8) + 4;
         builder.addPiece(new IglooPieces$IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_LABORATORY, position, rotation, depth * 3));

         for (int i = 0; i < depth - 1; i++) {
            builder.addPiece(new IglooPieces$IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_LADDER, position, rotation, i * 3));
         }
      }

      builder.addPiece(new IglooPieces$IglooPiece(structureTemplateManager, STRUCTURE_LOCATION_IGLOO, position, rotation, 0));
   }
}
