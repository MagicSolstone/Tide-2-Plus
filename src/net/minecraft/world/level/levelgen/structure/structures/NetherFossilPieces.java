package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class NetherFossilPieces {
   private static final Identifier[] FOSSILS = new Identifier[]{
      Identifier.withDefaultNamespace("nether_fossils/fossil_1"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_2"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_3"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_4"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_5"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_6"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_7"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_8"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_9"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_10"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_11"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_12"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_13"),
      Identifier.withDefaultNamespace("nether_fossils/fossil_14")
   };

   public static void addPieces(
      final StructureTemplateManager structureTemplateManager, final StructurePiecesBuilder builder, final RandomSource random, final BlockPos position
   ) {
      Rotation nextRotation = Rotation.getRandom(random);
      builder.addPiece(new NetherFossilPieces$NetherFossilPiece(structureTemplateManager, Util.getRandom(FOSSILS, random), position, nextRotation));
   }
}
