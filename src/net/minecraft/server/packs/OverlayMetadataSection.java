package net.minecraft.server.packs;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackFormat;

public record OverlayMetadataSection(List<OverlayMetadataSection$OverlayEntry> overlays) {
   private static final Pattern DIR_VALIDATOR = Pattern.compile("[-_a-zA-Z0-9.]+");
   public static final MetadataSectionType<OverlayMetadataSection> CLIENT_TYPE = new MetadataSectionType<>(
      "overlays", codecForPackType(PackType.CLIENT_RESOURCES)
   );
   public static final MetadataSectionType<OverlayMetadataSection> SERVER_TYPE = new MetadataSectionType<>("overlays", codecForPackType(PackType.SERVER_DATA));

   private static DataResult<String> validateOverlayDir(final String path) {
      return !DIR_VALIDATOR.matcher(path).matches() ? DataResult.error(() -> path + " is not accepted directory name") : DataResult.success(path);
   }

   @VisibleForTesting
   public static Codec<OverlayMetadataSection> codecForPackType(final PackType packType) {
      return RecordCodecBuilder.create(
         i -> i.group(OverlayMetadataSection$OverlayEntry.listCodecForPackType(packType).fieldOf("entries").forGetter(OverlayMetadataSection::overlays))
            .apply(i, OverlayMetadataSection::new)
      );
   }

   public static MetadataSectionType<OverlayMetadataSection> forPackType(final PackType packType) {
      return switch (packType) {
         case CLIENT_RESOURCES -> CLIENT_TYPE;
         case SERVER_DATA -> SERVER_TYPE;
      };
   }

   public List<String> overlaysForVersion(final PackFormat version) {
      return this.overlays.stream().filter(entry -> entry.isApplicable(version)).map(OverlayMetadataSection$OverlayEntry::overlay).toList();
   }
}
