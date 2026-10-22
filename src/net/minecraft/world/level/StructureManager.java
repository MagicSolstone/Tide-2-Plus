package net.minecraft.world.level;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.chunk.StructureAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.jspecify.annotations.Nullable;

public class StructureManager {
   private final LevelAccessor level;
   private final WorldOptions worldOptions;
   private final StructureCheck structureCheck;

   public StructureManager(final LevelAccessor level, final WorldOptions worldOptions, final StructureCheck structureCheck) {
      this.level = level;
      this.worldOptions = worldOptions;
      this.structureCheck = structureCheck;
   }

   public StructureManager forWorldGenRegion(final WorldGenRegion region) {
      if (region.getLevel() != this.level) {
         throw new IllegalStateException("Using invalid structure manager (source level: " + region.getLevel() + ", region: " + region);
      } else {
         return new StructureManager(region, this.worldOptions, this.structureCheck);
      }
   }

   public List<StructureStart> startsForStructure(final int sectionX, final int sectionZ, final Predicate<Structure> matcher) {
      Map<Structure, LongSet> allReferences = this.level.getChunk(sectionX, sectionZ, ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
      Builder<StructureStart> result = ImmutableList.builder();

      for (Entry<Structure, LongSet> entry : allReferences.entrySet()) {
         Structure structure = entry.getKey();
         if (matcher.test(structure)) {
            this.fillStartsForStructure(structure, entry.getValue(), result::add);
         }
      }

      return result.build();
   }

   public List<StructureStart> startsForStructure(final int sectionX, final int sectionZ, final Structure structure) {
      LongSet referencesForStructure = this.level.getChunk(sectionX, sectionZ, ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure);
      Builder<StructureStart> result = ImmutableList.builder();
      this.fillStartsForStructure(structure, referencesForStructure, result::add);
      return result.build();
   }

   public void fillStartsForStructure(final Structure structure, final LongSet referencesForStructure, final Consumer<StructureStart> consumer) {
      LongIterator var4 = referencesForStructure.iterator();

      while (var4.hasNext()) {
         long key = (Long)var4.next();
         ChunkPos chunkPos = ChunkPos.unpack(key);
         StructureStart start = this.getStartForStructure(structure, this.level.getChunk(chunkPos.x(), chunkPos.z(), ChunkStatus.STRUCTURE_STARTS));
         if (start != null && start.isValid()) {
            consumer.accept(start);
         }
      }
   }

   @Nullable
   public StructureStart getStartForStructure(final Structure structure, final StructureAccess chunk) {
      return chunk.getStartForStructure(structure);
   }

   public void setStartForStructure(final Structure structure, final StructureStart start, final StructureAccess chunk) {
      chunk.setStartForStructure(structure, start);
   }

   public void addReferenceForStructure(final Structure structure, final long reference, final StructureAccess chunk) {
      chunk.addReferenceForStructure(structure, reference);
   }

   public boolean shouldGenerateStructures() {
      return this.worldOptions.generateStructures();
   }

   public StructureStart getStructureAt(final BlockPos blockPos, final Structure structure) {
      int sectionX = SectionPos.blockToSectionCoord(blockPos.getX());
      int sectionZ = SectionPos.blockToSectionCoord(blockPos.getZ());

      for (StructureStart structureStart : this.startsForStructure(sectionX, sectionZ, structure)) {
         if (structureStart.getBoundingBox().isInside(blockPos)) {
            return structureStart;
         }
      }

      return StructureStart.INVALID_START;
   }

   public StructureStart getStructureAt(final BlockPos blockPos, final HolderSet<Structure> structures) {
      int sectionX = SectionPos.blockToSectionCoord(blockPos.getX());
      int sectionZ = SectionPos.blockToSectionCoord(blockPos.getZ());

      for (StructureStart structureStart : this.startsForStructure(sectionX, sectionZ, this.matching(structures::contains))) {
         if (structureStart.getBoundingBox().isInside(blockPos)) {
            return structureStart;
         }
      }

      return StructureStart.INVALID_START;
   }

   public StructureStart getStructureWithPieceAt(final BlockPos blockPos, final TagKey<Structure> structureTag) {
      return this.getStructureWithPieceAt(blockPos, structure -> structure.is(structureTag));
   }

   public StructureStart getStructureWithPieceAt(final BlockPos blockPos, final HolderSet<Structure> structures) {
      return this.getStructureWithPieceAt(blockPos, structures::contains);
   }

   public StructureStart getStructureWithPieceAt(final BlockPos blockPos, final Predicate<Holder<Structure>> predicate) {
      int sectionX = SectionPos.blockToSectionCoord(blockPos.getX());
      int sectionZ = SectionPos.blockToSectionCoord(blockPos.getZ());

      for (StructureStart structureStart : this.startsForStructure(sectionX, sectionZ, this.matching(predicate))) {
         if (this.structureHasPieceAt(blockPos, structureStart)) {
            return structureStart;
         }
      }

      return StructureStart.INVALID_START;
   }

   private Predicate<Structure> matching(final Predicate<Holder<Structure>> predicate) {
      Registry<Structure> structures = this.registryAccess().lookupOrThrow(Registries.STRUCTURE);
      return structure -> structures.get(structures.getId(structure)).map(predicate::test).orElse(false);
   }

   public StructureStart getStructureWithPieceAt(final int x, final int y, final int z, final Structure structure) {
      int sectionX = SectionPos.blockToSectionCoord(x);
      int sectionZ = SectionPos.blockToSectionCoord(z);

      for (StructureStart structureStart : this.startsForStructure(sectionX, sectionZ, structure)) {
         if (this.structureHasPieceAt(x, y, z, structureStart)) {
            return structureStart;
         }
      }

      return StructureStart.INVALID_START;
   }

   public boolean structureHasPieceAt(final BlockPos blockPos, final StructureStart structureStart) {
      return this.structureHasPieceAt(blockPos.getX(), blockPos.getY(), blockPos.getZ(), structureStart);
   }

   public boolean structureHasPieceAt(final int x, final int y, final int z, final StructureStart structureStart) {
      for (StructurePiece piece : structureStart.getPieces()) {
         if (piece.getBoundingBox().isInside(x, y, z)) {
            return true;
         }
      }

      return false;
   }

   public Map<Structure, LongSet> getAllStructuresAt(final BlockPos pos) {
      return this.getAllStructuresAt(pos.getX(), pos.getY(), pos.getZ());
   }

   public Map<Structure, LongSet> getAllStructuresAt(final int x, final int y, final int z) {
      int sectionX = SectionPos.blockToSectionCoord(x);
      int sectionZ = SectionPos.blockToSectionCoord(z);
      return this.level.getChunk(sectionX, sectionZ, ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
   }

   public StructureCheckResult checkStructurePresence(
      final ChunkPos pos, final Structure structure, final StructurePlacement placement, final boolean createReference
   ) {
      return this.structureCheck.checkStart(pos, structure, placement, createReference);
   }

   public void addReference(final StructureStart start) {
      start.addReference();
      this.structureCheck.incrementReference(start.getChunkPos(), start.getStructure());
   }

   public RegistryAccess registryAccess() {
      return this.level.registryAccess();
   }
}
