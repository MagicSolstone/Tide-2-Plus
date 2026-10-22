package net.minecraft.server.packs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.List;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackFormat$IntermediaryFormat;
import net.minecraft.util.InclusiveRange;

public record OverlayMetadataSection$OverlayEntry(InclusiveRange<PackFormat> format, String overlay) {
   private static Codec<List<OverlayMetadataSection$OverlayEntry>> listCodecForPackType(final PackType packType) {
      int lastPreMinorVersion = PackFormat.lastPreMinorVersion(packType);
      return OverlayMetadataSection$OverlayEntry$IntermediateEntry.CODEC
         .listOf()
         .flatXmap(
            list -> PackFormat.validateHolderList(
               list, lastPreMinorVersion, (entry, formats) -> new OverlayMetadataSection$OverlayEntry(formats, entry.overlay())
            ),
            list -> DataResult.success(
               list.stream()
                  .map(
                     entry -> new OverlayMetadataSection$OverlayEntry$IntermediateEntry(
                        PackFormat$IntermediaryFormat.fromRange(entry.format(), lastPreMinorVersion), entry.overlay()
                     )
                  )
                  .toList()
            )
         );
   }

   public boolean isApplicable(final PackFormat formatToTest) {
      return this.format.isValueInRange(formatToTest);
   }
}
