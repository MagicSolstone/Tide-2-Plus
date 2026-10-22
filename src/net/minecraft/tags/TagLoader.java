package net.minecraft.tags;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SequencedSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.Registry$PendingTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryAccess$Frozen;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.DependencySorter;
import net.minecraft.util.StrictJsonParser;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class TagLoader<T> {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final TagLoader$ElementLookup<T> elementLookup;
   private final String directory;

   public TagLoader(final TagLoader$ElementLookup<T> elementLookup, final String directory) {
      this.elementLookup = elementLookup;
      this.directory = directory;
   }

   public Map<Identifier, List<TagLoader$EntryWithSource>> load(final ResourceManager resourceManager) {
      Map<Identifier, List<TagLoader$EntryWithSource>> builders = new HashMap<>();
      FileToIdConverter lister = FileToIdConverter.json(this.directory);

      for (Entry<Identifier, List<Resource>> entry : lister.listMatchingResourceStacks(resourceManager).entrySet()) {
         Identifier location = entry.getKey();
         Identifier id = lister.fileToId(location);

         for (Resource resource : entry.getValue()) {
            try (Reader reader = resource.openAsReader()) {
               JsonElement element = StrictJsonParser.parse(reader);
               List<TagLoader$EntryWithSource> tagContents = builders.computeIfAbsent(id, key -> new ArrayList<>());
               TagFile parsedContents = (TagFile)TagFile.CODEC.parse(new Dynamic(JsonOps.INSTANCE, element)).getOrThrow();
               if (parsedContents.replace()) {
                  tagContents.clear();
               }

               String sourceId = resource.sourcePackId();
               parsedContents.entries().forEach(ex -> tagContents.add(new TagLoader$EntryWithSource(ex, sourceId)));
            } catch (Exception var17) {
               LOGGER.error("Couldn't read tag list {} from {} in data pack {}", new Object[]{id, location, resource.sourcePackId(), var17});
            }
         }
      }

      return builders;
   }

   private Either<List<TagLoader$EntryWithSource>, List<T>> tryBuildTag(final TagEntry$Lookup<T> lookup, final List<TagLoader$EntryWithSource> entries) {
      SequencedSet<T> values = new LinkedHashSet<>();
      List<TagLoader$EntryWithSource> missingElements = new ArrayList<>();

      for (TagLoader$EntryWithSource entry : entries) {
         if (!entry.entry().build(lookup, values::add)) {
            missingElements.add(entry);
         }
      }

      return missingElements.isEmpty() ? Either.right(List.copyOf(values)) : Either.left(missingElements);
   }

   public Map<Identifier, List<T>> build(final Map<Identifier, List<TagLoader$EntryWithSource>> builders) {
      Map<Identifier, List<T>> newTags = new HashMap<>();
      TagEntry$Lookup<T> lookup = new TagLoader$1(this, newTags);
      DependencySorter<Identifier, TagLoader$SortingEntry> sorter = new DependencySorter<>();
      builders.forEach((id, entry) -> sorter.addEntry(id, new TagLoader$SortingEntry((List<TagLoader$EntryWithSource>)entry)));
      sorter.orderByDependencies(
         (id, contents) -> this.tryBuildTag(lookup, contents.entries)
            .ifLeft(
               missing -> LOGGER.error(
                  "Couldn't load tag {} as it is missing following references: {}",
                  id,
                  missing.stream().map(Objects::toString).collect(Collectors.joining(", "))
               )
            )
            .ifRight(tag -> newTags.put(id, tag))
      );
      return newTags;
   }

   public static <T> Map<TagKey<T>, List<Holder<T>>> loadTagsFromNetwork(final TagNetworkSerialization$NetworkPayload tags, final Registry<T> registry) {
      return tags.resolve(registry).tags;
   }

   public static List<Registry$PendingTags<?>> loadTagsForExistingRegistries(final ResourceManager manager, final RegistryAccess layer) {
      return layer.registries().map(entry -> loadPendingTags(manager, entry.value())).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
   }

   public static <T> void loadTagsForRegistry(final ResourceManager manager, final WritableRegistry<T> registry) {
      loadTagsForRegistry(manager, registry.key(), TagLoader$ElementLookup.fromWritableRegistry(registry));
   }

   public static <T> Map<TagKey<T>, List<Holder<T>>> loadTagsForRegistry(
      final ResourceManager manager, final ResourceKey<? extends Registry<T>> registryKey, final TagLoader$ElementLookup<Holder<T>> lookup
   ) {
      TagLoader<Holder<T>> loader = new TagLoader<>(lookup, Registries.tagsDirPath(registryKey));
      return wrapTags(registryKey, loader.build(loader.load(manager)));
   }

   private static <T> Map<TagKey<T>, List<Holder<T>>> wrapTags(
      final ResourceKey<? extends Registry<T>> registryKey, final Map<Identifier, List<Holder<T>>> tags
   ) {
      return tags.entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> TagKey.create(registryKey, e.getKey()), Entry::getValue));
   }

   private static <T> Optional<Registry$PendingTags<T>> loadPendingTags(final ResourceManager manager, final Registry<T> registry) {
      ResourceKey<? extends Registry<T>> key = registry.key();
      TagLoader<Holder<T>> loader = new TagLoader<>(
         (TagLoader$ElementLookup<Holder<T>>)TagLoader$ElementLookup.fromFrozenRegistry(registry), Registries.tagsDirPath(key)
      );
      TagLoader$LoadResult<T> tags = new TagLoader$LoadResult<>(key, wrapTags(registry.key(), loader.build(loader.load(manager))));
      return tags.tags().isEmpty() ? Optional.empty() : Optional.of(registry.prepareTagReload(tags));
   }

   public static List<HolderLookup$RegistryLookup<?>> buildUpdatedLookups(final RegistryAccess$Frozen registries, final List<Registry$PendingTags<?>> tags) {
      List<HolderLookup$RegistryLookup<?>> result = new ArrayList<>();
      registries.registries().forEach(lookup -> {
         Registry$PendingTags<?> foundTags = findTagsForRegistry(tags, lookup.key());
         result.add((HolderLookup$RegistryLookup<?>)(foundTags != null ? foundTags.lookup() : lookup.value()));
      });
      return result;
   }

   @Nullable
   private static Registry$PendingTags<?> findTagsForRegistry(final List<Registry$PendingTags<?>> tags, final ResourceKey<? extends Registry<?>> registryKey) {
      for (Registry$PendingTags<?> tag : tags) {
         if (tag.key() == registryKey) {
            return tag;
         }
      }

      return null;
   }
}
