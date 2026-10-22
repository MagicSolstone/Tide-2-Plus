package net.minecraft.server.packs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.packs.metadata.pack.PackFormat$IntermediaryFormat;
import net.minecraft.server.packs.metadata.pack.PackFormat$IntermediaryFormatHolder;

record OverlayMetadataSection$OverlayEntry$IntermediateEntry(PackFormat$IntermediaryFormat format, String overlay)
   implements PackFormat$IntermediaryFormatHolder {
   private static final Codec<OverlayMetadataSection$OverlayEntry$IntermediateEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            PackFormat$IntermediaryFormat.OVERLAY_CODEC.forGetter(OverlayMetadataSection$OverlayEntry$IntermediateEntry::format),
            Codec.STRING
               .validate(OverlayMetadataSection::validateOverlayDir)
               .fieldOf("directory")
               .forGetter(OverlayMetadataSection$OverlayEntry$IntermediateEntry::overlay)
         )
         .apply(i, OverlayMetadataSection$OverlayEntry$IntermediateEntry::new)
   );

   @Override
   public String toString() {
      return this.overlay;
   }
}
