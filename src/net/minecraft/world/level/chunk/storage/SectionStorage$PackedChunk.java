package net.minecraft.world.level.chunk.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Optional;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.LevelHeightAccessor;

record SectionStorage$PackedChunk<T>(Int2ObjectMap<T> sectionsByY, boolean versionChanged) {
   public static <T> SectionStorage$PackedChunk<T> parse(
      final Codec<T> codec,
      final DynamicOps<Tag> ops,
      final Tag tag,
      final SimpleRegionStorage simpleRegionStorage,
      final LevelHeightAccessor levelHeightAccessor
   ) {
      Dynamic<Tag> originalTag = new Dynamic(ops, tag);
      Dynamic<Tag> fixedTag = simpleRegionStorage.upgradeChunkTag(originalTag, 1945);
      boolean versionChanged = originalTag != fixedTag;
      OptionalDynamic<Tag> sections = fixedTag.get("Sections");
      Int2ObjectMap<T> sectionsByY = new Int2ObjectOpenHashMap();

      for (int sectionY = levelHeightAccessor.getMinSectionY(); sectionY <= levelHeightAccessor.getMaxSectionY(); sectionY++) {
         Optional<T> section = sections.get(Integer.toString(sectionY))
            .result()
            .flatMap(sectionData -> codec.parse(sectionData).resultOrPartial(SectionStorage.LOGGER::error));
         if (section.isPresent()) {
            sectionsByY.put(sectionY, section.get());
         }
      }

      return new SectionStorage$PackedChunk<>(sectionsByY, versionChanged);
   }
}
