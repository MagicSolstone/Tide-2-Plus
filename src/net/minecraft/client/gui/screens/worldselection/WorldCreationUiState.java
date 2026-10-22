package net.minecraft.client.gui.screens.worldselection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.util.FileUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.storage.LevelStorageException;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class WorldCreationUiState {
   private static final Component DEFAULT_WORLD_NAME = Component.translatable("selectWorld.newWorld");
   private final List<Consumer<WorldCreationUiState>> listeners = new ArrayList<>();
   private String name = DEFAULT_WORLD_NAME.getString();
   private WorldCreationUiState$SelectedGameMode gameMode = WorldCreationUiState$SelectedGameMode.SURVIVAL;
   private Difficulty difficulty = Difficulty.NORMAL;
   @Nullable
   private Boolean allowCommands;
   private String seed;
   private boolean generateStructures;
   private boolean bonusChest;
   private final Path savesFolder;
   private String targetFolder;
   private WorldCreationContext settings;
   private WorldCreationUiState$WorldTypeEntry worldType;
   private final List<WorldCreationUiState$WorldTypeEntry> normalPresetList = new ArrayList<>();
   private final List<WorldCreationUiState$WorldTypeEntry> altPresetList = new ArrayList<>();
   private GameRules gameRules;

   public WorldCreationUiState(
      final Path savesFolder, final WorldCreationContext settings, final Optional<ResourceKey<WorldPreset>> preset, final OptionalLong seed
   ) {
      this.savesFolder = savesFolder;
      this.settings = settings;
      this.worldType = new WorldCreationUiState$WorldTypeEntry(findPreset(settings, preset).orElse(null));
      this.updatePresetLists();
      this.seed = seed.isPresent() ? Long.toString(seed.getAsLong()) : "";
      this.generateStructures = settings.options().generateStructures();
      this.bonusChest = settings.options().generateBonusChest();
      this.targetFolder = this.findResultFolder(this.name);
      this.gameMode = settings.initialWorldCreationOptions().selectedGameMode();
      this.gameRules = new GameRules(settings.dataConfiguration().enabledFeatures());
      this.gameRules.setAll(settings.initialWorldCreationOptions().gameRuleOverwrites(), null);
      Optional.ofNullable(settings.initialWorldCreationOptions().flatLevelPreset())
         .flatMap(key -> settings.worldgenLoadContext().lookup(Registries.FLAT_LEVEL_GENERATOR_PRESET).flatMap(registry -> registry.get(key)))
         .map(reference -> reference.value().settings())
         .ifPresent(generatorSettings -> this.updateDimensions(PresetEditor.flatWorldConfigurator(generatorSettings)));
   }

   public void addListener(final Consumer<WorldCreationUiState> action) {
      this.listeners.add(action);
   }

   public void onChanged() {
      boolean bonusChest = this.isBonusChest();
      if (bonusChest != this.settings.options().generateBonusChest()) {
         this.settings = this.settings.withOptions(options -> options.withBonusChest(bonusChest));
      }

      boolean generateStructures = this.isGenerateStructures();
      if (generateStructures != this.settings.options().generateStructures()) {
         this.settings = this.settings.withOptions(options -> options.withStructures(generateStructures));
      }

      for (Consumer<WorldCreationUiState> listener : this.listeners) {
         listener.accept(this);
      }
   }

   public void setName(final String name) {
      this.name = name;
      this.targetFolder = this.findResultFolder(name);
      this.onChanged();
   }

   private String findResultFolder(final String name) {
      if (!Files.exists(this.savesFolder)) {
         try {
            Files.createDirectory(this.savesFolder);
         } catch (IOException var6) {
            throw new LevelStorageException(Component.literal("Could not create save folder"), var6);
         }
      }

      String trimmedName = name.trim();

      try {
         return FileUtil.findAvailableName(this.savesFolder, !trimmedName.isEmpty() ? trimmedName : DEFAULT_WORLD_NAME.getString(), "");
      } catch (Exception var5) {
         try {
            return FileUtil.findAvailableName(this.savesFolder, "World", "");
         } catch (IOException var4) {
            throw new LevelStorageException(Component.literal("Could not create world folder"), var4);
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public String getTargetFolder() {
      return this.targetFolder;
   }

   public void setGameMode(final WorldCreationUiState$SelectedGameMode gameMode) {
      this.gameMode = gameMode;
      this.onChanged();
   }

   public WorldCreationUiState$SelectedGameMode getGameMode() {
      return this.isDebug() ? WorldCreationUiState$SelectedGameMode.DEBUG : this.gameMode;
   }

   public void setDifficulty(final Difficulty difficulty) {
      this.difficulty = difficulty;
      this.onChanged();
   }

   public Difficulty getDifficulty() {
      return this.isHardcore() ? Difficulty.HARD : this.difficulty;
   }

   public boolean isHardcore() {
      return this.getGameMode() == WorldCreationUiState$SelectedGameMode.HARDCORE;
   }

   public void setAllowCommands(final boolean allowCommands) {
      this.allowCommands = allowCommands;
      this.onChanged();
   }

   public boolean isAllowCommands() {
      if (this.isDebug()) {
         return true;
      } else if (this.isHardcore()) {
         return false;
      } else {
         return this.allowCommands == null ? this.getGameMode() == WorldCreationUiState$SelectedGameMode.CREATIVE : this.allowCommands;
      }
   }

   public void setSeed(final String seed) {
      this.seed = seed;
      this.settings = this.settings.withOptions(options -> options.withSeed(WorldOptions.parseSeed(this.getSeed())));
      this.onChanged();
   }

   public String getSeed() {
      return this.seed;
   }

   public void setGenerateStructures(final boolean generateStructures) {
      this.generateStructures = generateStructures;
      this.onChanged();
   }

   public boolean isGenerateStructures() {
      return this.isDebug() ? false : this.generateStructures;
   }

   public void setBonusChest(final boolean bonusChest) {
      this.bonusChest = bonusChest;
      this.onChanged();
   }

   public boolean isBonusChest() {
      return !this.isDebug() && !this.isHardcore() ? this.bonusChest : false;
   }

   public void setSettings(final WorldCreationContext settings) {
      this.settings = settings;
      this.updatePresetLists();
      this.onChanged();
   }

   public WorldCreationContext getSettings() {
      return this.settings;
   }

   public void updateDimensions(final WorldCreationContext$DimensionsUpdater modifier) {
      this.settings = this.settings.withDimensions(modifier);
      this.onChanged();
   }

   protected boolean tryUpdateDataConfiguration(final WorldDataConfiguration newConfig) {
      WorldDataConfiguration oldConfig = this.settings.dataConfiguration();
      if (oldConfig.dataPacks().getEnabled().equals(newConfig.dataPacks().getEnabled()) && oldConfig.enabledFeatures().equals(newConfig.enabledFeatures())) {
         this.settings = new WorldCreationContext(
            this.settings.options(),
            this.settings.datapackDimensions(),
            this.settings.selectedDimensions(),
            this.settings.worldgenRegistries(),
            this.settings.dataPackResources(),
            newConfig,
            this.settings.initialWorldCreationOptions()
         );
         return true;
      } else {
         return false;
      }
   }

   public boolean isDebug() {
      return this.settings.selectedDimensions().isDebug();
   }

   public void setWorldType(final WorldCreationUiState$WorldTypeEntry worldType) {
      this.worldType = worldType;
      Holder<WorldPreset> preset = worldType.preset();
      if (preset != null) {
         this.updateDimensions((registryAccess, dimensions) -> preset.value().createWorldDimensions());
      }
   }

   public WorldCreationUiState$WorldTypeEntry getWorldType() {
      return this.worldType;
   }

   @Nullable
   public PresetEditor getPresetEditor() {
      Holder<WorldPreset> preset = this.getWorldType().preset();
      return preset != null ? PresetEditor.EDITORS.get(preset.unwrapKey()) : null;
   }

   public List<WorldCreationUiState$WorldTypeEntry> getNormalPresetList() {
      return this.normalPresetList;
   }

   public List<WorldCreationUiState$WorldTypeEntry> getAltPresetList() {
      return this.altPresetList;
   }

   private void updatePresetLists() {
      Registry<WorldPreset> presetRegistry = this.getSettings().worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET);
      this.normalPresetList.clear();
      this.normalPresetList
         .addAll(
            getNonEmptyList(presetRegistry, WorldPresetTags.NORMAL)
               .orElseGet(() -> presetRegistry.listElements().map(WorldCreationUiState$WorldTypeEntry::new).toList())
         );
      this.altPresetList.clear();
      this.altPresetList.addAll(getNonEmptyList(presetRegistry, WorldPresetTags.EXTENDED).orElse(this.normalPresetList));
      Holder<WorldPreset> preset = this.worldType.preset();
      if (preset != null) {
         WorldCreationUiState$WorldTypeEntry newPreset = findPreset(this.getSettings(), preset.unwrapKey())
            .map(WorldCreationUiState$WorldTypeEntry::new)
            .orElse(this.normalPresetList.getFirst());
         boolean isCustomizablePreset = PresetEditor.EDITORS.get(preset.unwrapKey()) != null;
         if (isCustomizablePreset) {
            this.worldType = newPreset;
         } else {
            this.setWorldType(newPreset);
         }
      }
   }

   private static Optional<Holder<WorldPreset>> findPreset(final WorldCreationContext settings, final Optional<ResourceKey<WorldPreset>> preset) {
      return preset.flatMap(k -> settings.worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET).get((ResourceKey<WorldPreset>)k));
   }

   private static Optional<List<WorldCreationUiState$WorldTypeEntry>> getNonEmptyList(final Registry<WorldPreset> presetRegistry, final TagKey<WorldPreset> id) {
      return presetRegistry.get(id).map(tag -> tag.stream().map(WorldCreationUiState$WorldTypeEntry::new).toList()).filter(l -> !l.isEmpty());
   }

   public void setGameRules(final GameRules gameRules) {
      this.gameRules = gameRules;
      this.onChanged();
   }

   public GameRules getGameRules() {
      return this.gameRules;
   }
}
