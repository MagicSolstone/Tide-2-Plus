package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.CodepointMap;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastBufferedInputStream;

@Environment(EnvType.CLIENT)
public class UnihexProvider$Definition implements GlyphProviderDefinition {
   public static final MapCodec<UnihexProvider$Definition> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("hex_file").forGetter(o -> o.hexFile),
            UnihexProvider$OverrideRange.CODEC.listOf().optionalFieldOf("size_overrides", List.of()).forGetter(o -> o.sizeOverrides)
         )
         .apply(i, UnihexProvider$Definition::new)
   );
   private final Identifier hexFile;
   private final List<UnihexProvider$OverrideRange> sizeOverrides;

   private UnihexProvider$Definition(final Identifier hexFile, final List<UnihexProvider$OverrideRange> sizeOverrides) {
      this.hexFile = hexFile;
      this.sizeOverrides = sizeOverrides;
   }

   @Override
   public GlyphProviderType type() {
      return GlyphProviderType.UNIHEX;
   }

   @Override
   public Either<GlyphProviderDefinition$Loader, GlyphProviderDefinition$Reference> unpack() {
      return Either.left(this::load);
   }

   private GlyphProvider load(final ResourceManager resourceManager) throws IOException {
      UnihexProvider var3;
      try (InputStream raw = resourceManager.open(this.hexFile)) {
         var3 = this.loadData(raw);
      }

      return var3;
   }

   private UnihexProvider loadData(final InputStream zipFile) throws IOException {
      CodepointMap<UnihexProvider$LineData> bits = new CodepointMap<>(UnihexProvider$LineData[]::new, UnihexProvider$LineData[][]::new);
      UnihexProvider$ReaderOutput output = bits::put;

      UnihexProvider var17;
      try (ZipInputStream zis = new ZipInputStream(zipFile)) {
         ZipEntry entry;
         while ((entry = zis.getNextEntry()) != null) {
            String name = entry.getName();
            if (name.endsWith(".hex")) {
               UnihexProvider.LOGGER.info("Found {}, loading", name);
               UnihexProvider.readFromStream(new FastBufferedInputStream(zis), output);
            }
         }

         CodepointMap<UnihexProvider$Glyph> glyphs = new CodepointMap<>(UnihexProvider$Glyph[]::new, UnihexProvider$Glyph[][]::new);

         for (UnihexProvider$OverrideRange sizeOverride : this.sizeOverrides) {
            int from = sizeOverride.from;
            int to = sizeOverride.to;
            UnihexProvider$Dimensions size = sizeOverride.dimensions;

            for (int c = from; c <= to; c++) {
               UnihexProvider$LineData codepointBits = bits.remove(c);
               if (codepointBits != null) {
                  glyphs.put(c, new UnihexProvider$Glyph(codepointBits, size.left, size.right));
               }
            }
         }

         bits.forEach((codepoint, glyphBits) -> {
            int packedSize = glyphBits.calculateWidth();
            int left = UnihexProvider$Dimensions.left(packedSize);
            int right = UnihexProvider$Dimensions.right(packedSize);
            glyphs.put(codepoint, new UnihexProvider$Glyph(glyphBits, left, right));
         });
         var17 = new UnihexProvider(glyphs);
      }

      return var17;
   }
}
