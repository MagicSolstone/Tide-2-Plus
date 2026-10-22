package net.minecraft.util.filefix.fixes;

import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.Tag;

public record LegacyStructureFileFix$LegacyStructureData(Map<String, Dynamic<?>> starts, Map<String, LongList> indexes) {
   public LegacyStructureFileFix$LegacyStructureData() {
      this(new HashMap<>(), new HashMap<>());
   }

   public void addStart(final String id, final Dynamic<Tag> data) {
      this.starts.put(id, data);
   }

   public void addIndex(final String id, final long sourcePos) {
      this.indexes.computeIfAbsent(id, l -> new LongArrayList()).add(sourcePos);
   }
}
