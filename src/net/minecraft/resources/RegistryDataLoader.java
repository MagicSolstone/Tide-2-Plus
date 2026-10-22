package net.minecraft.resources;

import com.mojang.logging.LogUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess$Frozen;
import net.minecraft.core.RegistryAccess$ImmutableRegistryAccess;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowSoundVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.feline.CatSoundVariant;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.pig.PigSoundVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.item.slot.SlotSources;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.timeline.Timeline;
import org.slf4j.Logger;

public class RegistryDataLoader {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Comparator<ResourceKey<?>> ERROR_KEY_COMPARATOR = Comparator.comparing(ResourceKey::registry).thenComparing(ResourceKey::identifier);
   public static final List<RegistryDataLoader$RegistryData<?>> WORLD_REGISTRIES = List.of(
      new RegistryDataLoader$RegistryData<>(Registries.DIMENSION_TYPE, DimensionType.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.BIOME, Biome.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.CARVER, WorldCarver.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.FEATURE, Feature.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.STRUCTURE, Structure.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TEMPLATE_POOL, StructureTemplatePool.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.NOISE_SETTINGS, NoiseGeneratorSettings.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.NOISE, NormalNoise.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.DENSITY_FUNCTION, DensityFunctions.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.MATERIAL_RULE, MaterialRule.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.MATERIAL_CONDITION, MaterialCondition.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.WORLD_PRESET, WorldPreset.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPreset.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfig.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.WOLF_VARIANT, WolfVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.PIG_VARIANT, PigVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.PIG_SOUND_VARIANT, PigSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.FROG_VARIANT, FrogVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.CAT_VARIANT, CatVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.CAT_SOUND_VARIANT, CatSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.COW_VARIANT, CowVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.COW_SOUND_VARIANT, CowSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.SULFUR_CUBE_ARCHETYPE, SulfurCubeArchetype.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.CHICKEN_VARIANT, ChickenVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterList.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.ENCHANTMENT_PROVIDER, EnchantmentProvider.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.INSTRUMENT, Instrument.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.DIALOG, Dialog.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.WORLD_CLOCK, WorldClock.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TIMELINE, Timeline.DIRECT_CODEC, Timeline::validateRegistry),
      new RegistryDataLoader$RegistryData<>(Registries.VILLAGER_TRADE, VillagerTrade.CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TRADE_SET, TradeSet.CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.DECORATED_POT_PATTERN, DecoratedPotPattern.CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.BLOCK_TRANSFORMER, BlockTransformer.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.BLOCK_STATE_PROVIDER, BlockStateProvider.DIRECT_CODEC)
   );
   public static final List<RegistryDataLoader$RegistryData<?>> DIMENSION_REGISTRIES = List.of(
      new RegistryDataLoader$RegistryData<>(Registries.LEVEL_STEM, LevelStem.CODEC)
   );
   public static final List<RegistryDataLoader$RegistryData<?>> RELOADABLE_REGISTRIES = List.of(
      new RegistryDataLoader$RegistryData<>(Registries.LOOT_TABLE, LootTable.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.PREDICATE, LootItemCondition.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.CONTEXT_FLOAT_PROVIDER, ContextFloatProviders.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.CONTEXT_INT_PROVIDER, ContextIntProviders.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.ITEM_MODIFIER, LootItemFunctions.TYPED_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.SLOT_SOURCE, SlotSources.TYPED_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.ADVANCEMENT, Advancement.CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.RECIPE, Recipe.DIRECT_CODEC)
   );
   public static final List<RegistryDataLoader$RegistryData<?>> SYNCHRONIZED_REGISTRIES = List.of(
      new RegistryDataLoader$RegistryData<>(Registries.BIOME, Biome.NETWORK_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.WOLF_VARIANT, WolfVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.PIG_VARIANT, PigVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.PIG_SOUND_VARIANT, PigSoundVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.FROG_VARIANT, FrogVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.CAT_VARIANT, CatVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.CAT_SOUND_VARIANT, CatSoundVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.COW_SOUND_VARIANT, CowSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.COW_VARIANT, CowVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.CHICKEN_VARIANT, ChickenVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.NETWORK_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, RegistryValidator.nonEmpty()),
      new RegistryDataLoader$RegistryData<>(Registries.SULFUR_CUBE_ARCHETYPE, SulfurCubeArchetype.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.DIMENSION_TYPE, DimensionType.NETWORK_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.INSTRUMENT, Instrument.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.DIALOG, Dialog.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.WORLD_CLOCK, WorldClock.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.TIMELINE, Timeline.NETWORK_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.DECORATED_POT_PATTERN, DecoratedPotPattern.CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.BLOCK_TRANSFORMER, BlockTransformer.DIRECT_CODEC),
      new RegistryDataLoader$RegistryData<>(Registries.BLOCK_STATE_PROVIDER, BlockStateProvider.DIRECT_CODEC)
   );

   public static CompletableFuture<RegistryAccess$Frozen> load(
      final ResourceManager resourceManager,
      final List<HolderLookup$RegistryLookup<?>> contextRegistries,
      final List<RegistryDataLoader$RegistryData<?>> registriesToLoad,
      final Executor executor
   ) {
      RegistryDataLoader$LoaderFactory loaderFactory = new RegistryDataLoader$1(resourceManager);
      return load(loaderFactory, contextRegistries, registriesToLoad, executor);
   }

   public static CompletableFuture<RegistryAccess$Frozen> load(
      final Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader$NetworkedRegistryData> entries,
      final ResourceProvider knownDataSource,
      final List<HolderLookup$RegistryLookup<?>> contextRegistries,
      final List<RegistryDataLoader$RegistryData<?>> registriesToLoad,
      final Executor executor
   ) {
      RegistryDataLoader$LoaderFactory loaderFactory = new RegistryDataLoader$2(entries, knownDataSource);
      return load(loaderFactory, contextRegistries, registriesToLoad, executor);
   }

   private static CompletableFuture<RegistryAccess$Frozen> load(
      final RegistryDataLoader$LoaderFactory loaderFactory,
      final List<HolderLookup$RegistryLookup<?>> contextRegistries,
      final List<RegistryDataLoader$RegistryData<?>> registriesToLoad,
      final Executor executor
   ) {
      return CompletableFuture.<CompletableFuture<RegistryAccess$Frozen>>supplyAsync(
            () -> {
               Map<ResourceKey<?>, Exception> loadingErrors = new ConcurrentHashMap<>();
               List<RegistryLoadTask<?>> loadTasks = registriesToLoad.stream()
                  .map(r -> loaderFactory.create((RegistryDataLoader$RegistryData<?>)r, loadingErrors))
                  .collect(Collectors.toUnmodifiableList());
               RegistryOps$RegistryInfoLookup contextAndNewRegistries = createContext(contextRegistries, loadTasks);
               int taskCount = loadTasks.size();
               CompletableFuture<?>[] loadCompletions = new CompletableFuture[taskCount];

               for (int i = 0; i < taskCount; i++) {
                  loadCompletions[i] = loadTasks.get(i).load(contextAndNewRegistries, executor);
               }

               return CompletableFuture.allOf(loadCompletions).thenApplyAsync(ignored -> {
                  List<RegistryLoadTask<?>> frozenRegistries = loadTasks.stream().filter(task -> task.freezeRegistry(loadingErrors)).toList();
                  if (!loadingErrors.isEmpty()) {
                     throw logErrors(loadingErrors);
                  } else {
                     List<? extends Registry<?>> registries = frozenRegistries.stream().flatMap(task -> task.validateRegistry(loadingErrors).stream()).toList();
                     if (!loadingErrors.isEmpty()) {
                        throw logErrors(loadingErrors);
                     } else {
                        return new RegistryAccess$ImmutableRegistryAccess(registries).freeze();
                     }
                  }
               }, executor);
            },
            executor
         )
         .thenCompose(c -> (CompletionStage<RegistryAccess$Frozen>)c);
   }

   private static RegistryOps$RegistryInfoLookup createContext(
      final List<HolderLookup$RegistryLookup<?>> contextRegistries, final List<RegistryLoadTask<?>> newRegistriesAndLoaders
   ) {
      Map<ResourceKey<? extends Registry<?>>, HolderGetter<?>> result = new HashMap<>();
      contextRegistries.forEach(e -> result.put(e.key(), e));
      newRegistriesAndLoaders.forEach(e -> result.put(e.registryKey(), e.concurrentRegistrationGetter));
      return new RegistryDataLoader$3(result);
   }

   private static ReportedException logErrors(final Map<ResourceKey<?>, Exception> loadingErrors) {
      printFullDetailsToLog(loadingErrors);
      return createReportWithBriefInfo(loadingErrors);
   }

   private static void printFullDetailsToLog(final Map<ResourceKey<?>, Exception> loadingErrors) {
      StringWriter collectedErrors = new StringWriter();
      PrintWriter errorPrinter = new PrintWriter(collectedErrors);
      Map<Identifier, Map<Identifier, Exception>> errorsByRegistry = loadingErrors.entrySet()
         .stream()
         .collect(Collectors.groupingBy(e -> e.getKey().registry(), Collectors.toMap(e -> e.getKey().identifier(), Entry::getValue)));
      errorsByRegistry.entrySet().stream().sorted(Entry.comparingByKey()).forEach(registryEntry -> {
         errorPrinter.printf(Locale.ROOT, "> Errors in registry %s:%n", registryEntry.getKey());
         registryEntry.getValue().entrySet().stream().sorted(Entry.comparingByKey()).forEach(elementError -> {
            errorPrinter.printf(Locale.ROOT, ">> Errors in element %s:%n", elementError.getKey());
            elementError.getValue().printStackTrace(errorPrinter);
         });
      });
      errorPrinter.flush();
      LOGGER.error("Registry loading errors:\n{}", collectedErrors);
   }

   private static ReportedException createReportWithBriefInfo(final Map<ResourceKey<?>, Exception> loadingErrors) {
      CrashReport report = CrashReport.forThrowable(new IllegalStateException("Failed to load registries due to errors"), "Registry Loading");
      CrashReportCategory errors = report.addCategory("Loading info");
      errors.setDetail(
         "Errors",
         () -> {
            StringBuilder briefDetails = new StringBuilder();
            loadingErrors.entrySet()
               .stream()
               .sorted(Entry.comparingByKey(ERROR_KEY_COMPARATOR))
               .forEach(
                  e -> briefDetails.append("\n\t\t")
                     .append(e.getKey().registry())
                     .append("/")
                     .append(e.getKey().identifier())
                     .append(": ")
                     .append(e.getValue().getMessage())
               );
            return briefDetails.toString();
         }
      );
      return new ReportedException(report);
   }
}
