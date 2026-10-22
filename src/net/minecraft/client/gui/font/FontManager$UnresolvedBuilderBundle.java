package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.GlyphProvider$Conditional;
import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition$Reference;
import net.minecraft.resources.Identifier;
import net.minecraft.util.DependencySorter$Entry;

@Environment(EnvType.CLIENT)
record FontManager$UnresolvedBuilderBundle(Identifier fontId, List<FontManager$BuilderResult> builders, Set<Identifier> dependencies)
   implements DependencySorter$Entry<Identifier> {
   public FontManager$UnresolvedBuilderBundle(final Identifier fontId) {
      this(fontId, new ArrayList<>(), new HashSet<>());
   }

   public void add(final FontManager$BuilderId builderId, final FontOption$Filter filter, final GlyphProviderDefinition$Reference reference) {
      this.builders.add(new FontManager$BuilderResult(builderId, filter, Either.right(reference.id())));
      this.dependencies.add(reference.id());
   }

   public void add(final FontManager$BuilderId builderId, final FontOption$Filter filter, final CompletableFuture<Optional<GlyphProvider>> provider) {
      this.builders.add(new FontManager$BuilderResult(builderId, filter, Either.left(provider)));
   }

   private Stream<CompletableFuture<Optional<GlyphProvider>>> listBuilders() {
      return this.builders.stream().flatMap(e -> e.result.left().stream());
   }

   public Optional<List<GlyphProvider$Conditional>> resolve(final Function<Identifier, List<GlyphProvider$Conditional>> resolver) {
      List<GlyphProvider$Conditional> resolved = new ArrayList<>();

      for (FontManager$BuilderResult builder : this.builders) {
         Optional<List<GlyphProvider$Conditional>> resolvedBuilder = builder.resolve(resolver);
         if (!resolvedBuilder.isPresent()) {
            return Optional.empty();
         }

         resolved.addAll(resolvedBuilder.get());
      }

      return Optional.of(resolved);
   }

   @Override
   public void visitRequiredDependencies(final Consumer<Identifier> output) {
      this.dependencies.forEach(output);
   }

   @Override
   public void visitOptionalDependencies(final Consumer<Identifier> output) {
   }
}
