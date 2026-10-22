package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import java.util.Map;
import net.minecraft.util.Util;

public record MapDecorations(Map<String, MapDecorations$Entry> decorations) {
   public static final MapDecorations EMPTY = new MapDecorations(Map.of());
   public static final Codec<MapDecorations> CODEC = Codec.unboundedMap(Codec.STRING, MapDecorations$Entry.CODEC)
      .xmap(MapDecorations::new, MapDecorations::decorations);

   public MapDecorations withDecoration(final String id, final MapDecorations$Entry entry) {
      return new MapDecorations(Util.copyAndPut(this.decorations, id, entry));
   }
}
