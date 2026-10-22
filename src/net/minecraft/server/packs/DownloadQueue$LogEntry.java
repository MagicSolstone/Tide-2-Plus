package net.minecraft.server.packs;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.ExtraCodecs;

record DownloadQueue$LogEntry(UUID id, String url, Instant time, Optional<String> hash, Either<String, DownloadQueue$FileInfoEntry> errorOrFileInfo) {
   public static final Codec<DownloadQueue$LogEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(DownloadQueue$LogEntry::id),
            Codec.STRING.fieldOf("url").forGetter(DownloadQueue$LogEntry::url),
            ExtraCodecs.INSTANT_ISO8601.fieldOf("time").forGetter(DownloadQueue$LogEntry::time),
            Codec.STRING.optionalFieldOf("hash").forGetter(DownloadQueue$LogEntry::hash),
            Codec.mapEither(Codec.STRING.fieldOf("error"), DownloadQueue$FileInfoEntry.CODEC.fieldOf("file"))
               .forGetter(DownloadQueue$LogEntry::errorOrFileInfo)
         )
         .apply(i, DownloadQueue$LogEntry::new)
   );
}
