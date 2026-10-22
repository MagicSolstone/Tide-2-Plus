package net.minecraft.world.level;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class EmptyStructureManager extends StructureManager {
   public static final EmptyStructureManager INSTANCE = new EmptyStructureManager();

   private EmptyStructureManager() {
      super(null, null, null);
   }

   @Override
   public List<StructureStart> startsForStructure(final int sectionX, final int sectionZ, final Structure structure) {
      return List.of();
   }

   @Override
   public List<StructureStart> startsForStructure(final int sectionX, final int sectionZ, final Predicate<Structure> matcher) {
      return List.of();
   }

   @Override
   public boolean shouldGenerateStructures() {
      return false;
   }

   @Override
   public StructureCheckResult checkStructurePresence(
      final ChunkPos pos, final Structure structure, final StructurePlacement placement, final boolean createReference
   ) {
      return StructureCheckResult.START_NOT_PRESENT;
   }
}
