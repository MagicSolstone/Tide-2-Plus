package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.GlyphProvider$Conditional;
import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record FontManager$BuilderResult(FontManager$BuilderId id, FontOption$Filter filter, Either<CompletableFuture<Optional<GlyphProvider>>, Identifier> result) {
   public Optional<List<GlyphProvider$Conditional>> resolve(final Function<Identifier, List<GlyphProvider$Conditional>> resolver) {
      return (Optional<List<GlyphProvider$Conditional>>)this.result
         .map(
            provider -> ((Optional)provider.join()).map(p -> List.of(new GlyphProvider$Conditional(p, this.filter))),
            reference -> {
               List<GlyphProvider$Conditional> resolvedReferences = resolver.apply(reference);
               if (resolvedReferences == null) {
                  FontManager.LOGGER
                     .warn(
                        "Can't find font {} referenced by builder {}, either because it's missing, failed to load or is part of loading cycle",
                        reference,
                        this.id
                     );
                  return Optional.empty();
               } else {
                  return Optional.of(resolvedReferences.stream().map(this::mergeFilters).toList());
               }
            }
         );
   }

   private GlyphProvider$Conditional mergeFilters(final GlyphProvider$Conditional original) {
      return new GlyphProvider$Conditional(original.provider(), this.filter.merge(original.filter()));
   }
}
