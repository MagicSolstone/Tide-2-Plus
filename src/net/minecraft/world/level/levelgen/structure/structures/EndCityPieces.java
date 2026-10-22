package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class EndCityPieces {
   private static final int MAX_GEN_DEPTH = 8;
   private static final EndCityPieces$SectionGenerator HOUSE_TOWER_GENERATOR = new EndCityPieces$1();
   private static final List<Pair<Rotation, BlockPos>> TOWER_BRIDGES = List.of(
      Pair.of(Rotation.NONE, new BlockPos(1, -1, 0)),
      Pair.of(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)),
      Pair.of(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)),
      Pair.of(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6))
   );
   private static final EndCityPieces$SectionGenerator TOWER_GENERATOR = new EndCityPieces$2();
   private static final EndCityPieces$SectionGenerator TOWER_BRIDGE_GENERATOR = new EndCityPieces$3();
   private static final List<Pair<Rotation, BlockPos>> FAT_TOWER_BRIDGES = List.of(
      Pair.of(Rotation.NONE, new BlockPos(4, -1, 0)),
      Pair.of(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)),
      Pair.of(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)),
      Pair.of(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12))
   );
   private static final EndCityPieces$SectionGenerator FAT_TOWER_GENERATOR = new EndCityPieces$4();

   private static EndCityPieces$EndCityPiece addPiece(
      final StructureTemplateManager structureTemplateManager,
      final EndCityPieces$EndCityPiece parent,
      final BlockPos offset,
      final String templateName,
      final Rotation rotation,
      final boolean overwrite
   ) {
      EndCityPieces$EndCityPiece child = new EndCityPieces$EndCityPiece(structureTemplateManager, templateName, parent.templatePosition(), rotation, overwrite);
      BlockPos origin = parent.template().calculateConnectedPosition(parent.placeSettings(), offset, child.placeSettings(), BlockPos.ZERO);
      child.move(origin.getX(), origin.getY(), origin.getZ());
      return child;
   }

   public static void startHouseTower(
      final StructureTemplateManager structureTemplateManager,
      final BlockPos origin,
      final Rotation rotation,
      final List<StructurePiece> pieces,
      final RandomSource random
   ) {
      FAT_TOWER_GENERATOR.init();
      HOUSE_TOWER_GENERATOR.init();
      TOWER_BRIDGE_GENERATOR.init();
      TOWER_GENERATOR.init();
      EndCityPieces$EndCityPiece lastPiece = addHelper(pieces, new EndCityPieces$EndCityPiece(structureTemplateManager, "base_floor", origin, rotation, true));
      lastPiece = addHelper(pieces, addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_1", rotation, false));
      lastPiece = addHelper(pieces, addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "third_floor_1", rotation, false));
      lastPiece = addHelper(pieces, addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
      recursiveChildren(structureTemplateManager, TOWER_GENERATOR, 1, lastPiece, null, pieces, random);
   }

   private static EndCityPieces$EndCityPiece addHelper(final List<StructurePiece> pieces, final EndCityPieces$EndCityPiece piece) {
      pieces.add(piece);
      return piece;
   }

   private static boolean recursiveChildren(
      final StructureTemplateManager structureTemplateManager,
      final EndCityPieces$SectionGenerator generator,
      final int genDepth,
      final EndCityPieces$EndCityPiece parent,
      final BlockPos offset,
      final List<StructurePiece> pieces,
      final RandomSource random
   ) {
      if (genDepth > 8) {
         return false;
      } else {
         List<StructurePiece> childPieces = new ArrayList<>();
         if (generator.generate(structureTemplateManager, genDepth, parent, offset, childPieces, random)) {
            boolean collision = false;
            int childTag = random.nextInt();

            for (StructurePiece child : childPieces) {
               child.setGenDepth(childTag);
               StructurePiece collisionPiece = StructurePiece.findCollisionPiece(pieces, child.getBoundingBox());
               if (collisionPiece != null && collisionPiece.getGenDepth() != parent.getGenDepth()) {
                  collision = true;
                  break;
               }
            }

            if (!collision) {
               pieces.addAll(childPieces);
               return true;
            }
         }

         return false;
      }
   }
}
