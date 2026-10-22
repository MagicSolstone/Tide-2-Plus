package net.minecraft.server.packs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

record DownloadQueue$FileInfoEntry(String name, long size) {
   public static final Codec<DownloadQueue$FileInfoEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("name").forGetter(DownloadQueue$FileInfoEntry::name), Codec.LONG.fieldOf("size").forGetter(DownloadQueue$FileInfoEntry::size)
         )
         .apply(i, DownloadQueue$FileInfoEntry::new)
   );
}
