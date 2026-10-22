package net.minecraft.util.filefix.fixes;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.filefix.access.ChunkNbt;
import net.minecraft.util.filefix.access.FileAccess;
import net.minecraft.util.filefix.access.SavedDataNbt;
import net.minecraft.world.level.Level;

record LegacyStructureFileFix$DimensionFixEntry(
   ResourceKey<Level> dimensionKey,
   List<FileAccess<SavedDataNbt>> structureFileAccess,
   FileAccess<ChunkNbt> chunkFileAccess,
   Long2ObjectOpenHashMap<LegacyStructureFileFix$LegacyStructureData> structures
) {
}
