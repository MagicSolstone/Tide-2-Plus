package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.MenuTabBar;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.commands.Commands$CommandSelection;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldLoader$DataLoadContext;
import net.minecraft.server.WorldLoader$DataLoadOutput;
import net.minecraft.server.WorldLoader$InitConfig;
import net.minecraft.server.WorldLoader$PackConfig;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.util.FileUtil;
import net.minecraft.util.Util;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.LevelSettings$DifficultySettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.gamerules.GameRuleMap$Builder;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldDimensions$Complete;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelDataAndDimensions$WorldDataAndGenSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource$LevelStorageAccess;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.PrimaryLevelData$SpecialWorldProperty;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class CreateWorldScreen extends Screen {
   private static final int GROUP_BOTTOM = 1;
   private static final int TAB_COLUMN_WIDTH = 210;
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final String TEMP_WORLD_PREFIX = "mcworld-";
   private static final Component GAME_MODEL_LABEL = Component.translatable("selectWorld.gameMode");
   private static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");
   private static final Component EXPERIMENTS_LABEL = Component.translatable("selectWorld.experiments");
   private static final Component ALLOW_COMMANDS_INFO = Component.translatable("selectWorld.allowCommands.info");
   private static final Component PREPARING_WORLD_DATA = Component.translatable("createWorld.preparing");
   private static final int HORIZONTAL_BUTTON_SPACING = 10;
   private static final int VERTICAL_BUTTON_SPACING = 8;
   public static final Identifier TAB_HEADER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/tab_header_background.png");
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private final WorldCreationUiState uiState;
   private final TabManager tabManager = new TabManager(x$0 -> this.addRenderableWidget(x$0), x$0 -> this.removeWidget(x$0));
   private boolean recreated;
   private final DirectoryValidator packValidator;
   private final CreateWorldCallback createWorldCallback;
   private final Runnable onClose;
   @Nullable
   private Path tempDataPackDir;
   @Nullable
   private PackRepository tempDataPackRepository;
   @Nullable
   private MenuTabBar tabNavigationBar;

   public static void openFresh(final Minecraft minecraft, final Runnable onClose) {
      openFresh(
         minecraft,
         onClose,
         (createWorldScreen, finalLayers, worldDataAndGenSettings, gameRules, tempDataPackDir) -> createWorldScreen.createNewWorld(
            finalLayers, worldDataAndGenSettings, gameRules
         )
      );
   }

   public static void openFresh(final Minecraft minecraft, final Runnable onClose, final CreateWorldCallback createWorld) {
      WorldCreationContextMapper worldCreationContext = (managers, registries, cookie) -> new WorldCreationContext(
         cookie.worldGenSettings(), registries, managers, cookie.dataConfiguration()
      );
      Function<WorldLoader$DataLoadContext, WorldGenSettings> worldGenSettings = context -> new WorldGenSettings(
         WorldOptions.defaultWithRandomSeed(), WorldPresets.createNormalWorldDimensions(context.datapackWorldRegistries())
      );
      openCreateWorldScreen(minecraft, onClose, worldGenSettings, worldCreationContext, WorldPresets.NORMAL, createWorld);
   }

   public static void testWorld(final Minecraft minecraft, final Runnable onClose) {
      WorldCreationContextMapper worldCreationContext = (managers, registries, cookie) -> new WorldCreationContext(
         cookie.worldGenSettings().options(),
         cookie.worldGenSettings().dimensions(),
         registries,
         managers,
         cookie.dataConfiguration(),
         new InitialWorldCreationOptions(
            WorldCreationUiState$SelectedGameMode.CREATIVE,
            new GameRuleMap$Builder().set(GameRules.ADVANCE_TIME, false).set(GameRules.ADVANCE_WEATHER, false).set(GameRules.SPAWN_MOBS, false).build(),
            null
         )
      );
      Function<WorldLoader$DataLoadContext, WorldGenSettings> worldGenSettings = context -> new WorldGenSettings(
         WorldOptions.testWorldWithRandomSeed(), WorldPresets.createTestWorldDimensions(context.datapackWorldRegistries())
      );
      openCreateWorldScreen(
         minecraft,
         onClose,
         worldGenSettings,
         worldCreationContext,
         WorldPresets.FLAT_ALL_DIMENSIONS,
         (createWorldScreen, finalLayers, worldDataAndGenSettings, gameRules, tempDataPackDir) -> createWorldScreen.createNewWorld(
            finalLayers, worldDataAndGenSettings, gameRules
         )
      );
   }

   private static void openCreateWorldScreen(
      final Minecraft minecraft,
      final Runnable onClose,
      final Function<WorldLoader$DataLoadContext, WorldGenSettings> worldGenSettings,
      final WorldCreationContextMapper worldCreationContext,
      final ResourceKey<WorldPreset> worldPreset,
      final CreateWorldCallback createWorld
   ) {
      queueLoadScreen(minecraft, PREPARING_WORLD_DATA);
      long start = Util.getMillis();
      PackRepository vanillaOnlyPackRepository = new PackRepository(new ServerPacksSource(minecraft.directoryValidator()));
      WorldDataConfiguration dataConfig = SharedConstants.IS_RUNNING_IN_IDE
         ? new WorldDataConfiguration(new DataPackConfig(List.of("vanilla", "tests"), List.of()), FeatureFlags.DEFAULT_FLAGS)
         : WorldDataConfiguration.DEFAULT;
      WorldLoader$InitConfig loadConfig = createDefaultLoadConfig(vanillaOnlyPackRepository, dataConfig);
      CompletableFuture<WorldCreationContext> loadResult = WorldLoader.load(
         loadConfig,
         context -> new WorldLoader$DataLoadOutput<>(
            new DataPackReloadCookie(worldGenSettings.apply(context), context.dataConfiguration()), context.datapackDimensions()
         ),
         (resources, managers, registries, cookie) -> {
            resources.close();
            return worldCreationContext.apply(managers, registries, cookie);
         },
         Util.backgroundExecutor(),
         minecraft
      );
      minecraft.managedBlock(loadResult::isDone);
      long end = Util.getMillis();
      LOGGER.debug("Resource load for world creation blocked for {} ms", end - start);
      minecraft.gui.setScreen(new CreateWorldScreen(minecraft, onClose, loadResult.join(), Optional.of(worldPreset), OptionalLong.empty(), createWorld));
   }

   public static CreateWorldScreen createFromExisting(
      final Minecraft minecraft,
      final Runnable onClose,
      final LevelSettings levelSettings,
      final WorldCreationContext worldCreationContext,
      @Nullable final Path newDataPackDir
   ) {
      CreateWorldScreen result = new CreateWorldScreen(
         minecraft,
         onClose,
         worldCreationContext,
         WorldPresets.fromSettings(worldCreationContext.selectedDimensions()),
         OptionalLong.of(worldCreationContext.options().seed()),
         (createWorldScreen, finalLayers, worldDataAndGenSettings, gameRules, tempDataPackDir) -> createWorldScreen.createNewWorld(
            finalLayers, worldDataAndGenSettings, gameRules
         )
      );
      result.recreated = true;
      result.uiState.setName(levelSettings.levelName());
      result.uiState.setAllowCommands(levelSettings.allowCommands());
      result.uiState.setDifficulty(levelSettings.difficultySettings().difficulty());
      result.uiState.getGameRules().setAll(worldCreationContext.initialWorldCreationOptions().gameRuleOverwrites(), null);
      if (levelSettings.difficultySettings().hardcore()) {
         result.uiState.setGameMode(WorldCreationUiState$SelectedGameMode.HARDCORE);
      } else if (levelSettings.gameType().isSurvival()) {
         result.uiState.setGameMode(WorldCreationUiState$SelectedGameMode.SURVIVAL);
      } else if (levelSettings.gameType().isCreative()) {
         result.uiState.setGameMode(WorldCreationUiState$SelectedGameMode.CREATIVE);
      }

      result.tempDataPackDir = newDataPackDir;
      return result;
   }

   private CreateWorldScreen(
      final Minecraft minecraft,
      final Runnable onClose,
      final WorldCreationContext settings,
      final Optional<ResourceKey<WorldPreset>> preset,
      final OptionalLong seed,
      final CreateWorldCallback createWorldCallback
   ) {
      super(Component.translatable("selectWorld.create"));
      this.onClose = onClose;
      this.packValidator = minecraft.directoryValidator();
      this.createWorldCallback = createWorldCallback;
      this.uiState = new WorldCreationUiState(minecraft.getLevelSource().getBaseDir(), settings, preset, seed);
   }

   public WorldCreationUiState getUiState() {
      return this.uiState;
   }

   @Override
   protected void init() {
      this.tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width)
         .addTabs(new CreateWorldScreen$GameTab(this), new CreateWorldScreen$WorldTab(this), new CreateWorldScreen$MoreTab(this))
         .build();
      this.addRenderableWidget(this.tabNavigationBar);
      LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
      footer.addChild(Button.builder(Component.translatable("selectWorld.create"), button -> this.onCreate()).build());
      footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.popScreen()).build());
      this.layout.visitWidgets(button -> {
         button.setTabOrderGroup(1);
         this.addRenderableWidget(button);
      });
      this.tabNavigationBar.selectTab(0, false);
      this.uiState.onChanged();
      this.repositionElements();
   }

   @Override
   protected void setInitialFocus() {
   }

   @Override
   public void repositionElements() {
      if (this.tabNavigationBar != null) {
         this.tabNavigationBar.arrangeElements(this.width);
         int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
         ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
         this.tabManager.setTabArea(tabArea);
         this.layout.setHeaderHeight(tabAreaTop);
         this.layout.arrangeElements();
      }
   }

   private static void queueLoadScreen(final Minecraft minecraft, final Component message) {
      minecraft.setScreenAndShow(new GenericMessageScreen(message));
   }

   private void onCreate() {
      WorldCreationContext context = this.uiState.getSettings();
      WorldDimensions worldDimensions = context.selectedDimensions();
      WorldDimensions$Complete finalDimensions = worldDimensions.bake(context.datapackDimensions());
      LayeredRegistryAccess<RegistryLayer> finalLayers = context.worldgenRegistries()
         .replaceFrom(RegistryLayer.DIMENSIONS, finalDimensions.dimensionsRegistryAccess());
      FeatureFlagSet enabledFeatures = context.dataConfiguration().enabledFeatures();
      Lifecycle lifecycleFromFeatures = FeatureFlags.isExperimental(enabledFeatures) ? Lifecycle.experimental() : Lifecycle.stable();
      Lifecycle lifecycleFromRegistries = finalLayers.compositeAccess().allRegistriesLifecycle();
      Lifecycle lifecycle = lifecycleFromRegistries.add(lifecycleFromFeatures);
      boolean skipWarning = !this.recreated && lifecycleFromRegistries == Lifecycle.stable();
      boolean isDebug = finalDimensions.specialWorldProperty() == PrimaryLevelData$SpecialWorldProperty.DEBUG;
      LevelSettings levelSettings = this.createLevelSettings(isDebug);
      GameRules gameRules;
      if (isDebug) {
         gameRules = MinecraftServer.DEFAULT_GAME_RULES.get();
         gameRules.set(GameRules.ADVANCE_TIME, false, null);
      } else {
         gameRules = this.uiState.getGameRules().copy(enabledFeatures);
      }

      PrimaryLevelData worldData = new PrimaryLevelData(levelSettings, finalDimensions.specialWorldProperty(), lifecycle);
      WorldOptions options = this.uiState.getSettings().options();
      WorldGenSettings worldGenSettings = new WorldGenSettings(options, worldDimensions);
      LevelDataAndDimensions$WorldDataAndGenSettings worldDataAndGenSettings = new LevelDataAndDimensions$WorldDataAndGenSettings(worldData, worldGenSettings);
      WorldOpenFlows.confirmWorldCreation(
         this.minecraft, this, lifecycle, () -> this.createWorldAndCleanup(finalLayers, worldDataAndGenSettings, Optional.of(gameRules)), skipWarning
      );
   }

   private void createWorldAndCleanup(
      final LayeredRegistryAccess<RegistryLayer> finalLayers,
      final LevelDataAndDimensions$WorldDataAndGenSettings worldDataAndGenSettings,
      final Optional<GameRules> gameRules
   ) {
      boolean worldCreationSuccessful = this.createWorldCallback.create(this, finalLayers, worldDataAndGenSettings, gameRules, this.tempDataPackDir);
      this.removeTempDataPackDir();
      if (!worldCreationSuccessful) {
         this.popScreen();
      }
   }

   private boolean createNewWorld(
      final LayeredRegistryAccess<RegistryLayer> finalLayers,
      final LevelDataAndDimensions$WorldDataAndGenSettings worldDataAndGenSettings,
      final Optional<GameRules> gameRules
   ) {
      String worldFolder = this.uiState.getTargetFolder();
      WorldCreationContext context = this.uiState.getSettings();
      queueLoadScreen(this.minecraft, PREPARING_WORLD_DATA);
      Optional<LevelStorageSource$LevelStorageAccess> newWorldAccess = createNewWorldDirectory(this.minecraft, worldFolder, this.tempDataPackDir);
      if (newWorldAccess.isEmpty()) {
         SystemToast.onPackCopyFailure(this.minecraft, worldFolder);
         return false;
      } else {
         this.minecraft
            .createWorldOpenFlows()
            .createLevelFromExistingSettings(newWorldAccess.get(), context.dataPackResources(), finalLayers, worldDataAndGenSettings, gameRules);
         return true;
      }
   }

   private LevelSettings createLevelSettings(final boolean isDebug) {
      String name = this.uiState.getName().trim();
      return isDebug
         ? new LevelSettings(
            name, GameType.SPECTATOR, new LevelSettings$DifficultySettings(Difficulty.PEACEFUL, false, false), true, WorldDataConfiguration.DEFAULT
         )
         : new LevelSettings(
            name,
            this.uiState.getGameMode().gameType,
            new LevelSettings$DifficultySettings(this.uiState.getDifficulty(), this.uiState.isHardcore(), false),
            this.uiState.isAllowCommands(),
            this.uiState.getSettings().dataConfiguration()
         );
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (this.tabNavigationBar.keyPressed(event)) {
         return true;
      } else if (super.keyPressed(event)) {
         return true;
      } else if (event.isConfirmation()) {
         this.onCreate();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void onClose() {
      this.popScreen();
   }

   public void popScreen() {
      this.onClose.run();
      this.removeTempDataPackDir();
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
   }

   @Override
   protected void extractMenuBackground(final GuiGraphicsExtractor graphics) {
      graphics.blit(RenderPipelines.GUI_TEXTURED, TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
      this.extractMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
   }

   @Nullable
   private Path getOrCreateTempDataPackDir() {
      if (this.tempDataPackDir == null) {
         try {
            this.tempDataPackDir = Files.createTempDirectory("mcworld-");
         } catch (IOException var2) {
            LOGGER.warn("Failed to create temporary dir", var2);
            SystemToast.onPackCopyFailure(this.minecraft, this.uiState.getTargetFolder());
            this.popScreen();
         }
      }

      return this.tempDataPackDir;
   }

   private void openExperimentsScreen(final WorldDataConfiguration dataConfiguration) {
      Pair<Path, PackRepository> settings = this.getDataPackSelectionSettings(dataConfiguration);
      if (settings != null) {
         this.minecraft
            .gui
            .setScreen(
               new ExperimentsScreen(
                  this, (PackRepository)settings.getSecond(), packRepository -> this.tryApplyNewDataPacks(packRepository, false, this::openExperimentsScreen)
               )
            );
      }
   }

   private void openDataPackSelectionScreen(final WorldDataConfiguration dataConfiguration) {
      Pair<Path, PackRepository> settings = this.getDataPackSelectionSettings(dataConfiguration);
      if (settings != null) {
         this.minecraft
            .gui
            .setScreen(
               new PackSelectionScreen(
                  (PackRepository)settings.getSecond(),
                  packRepository -> this.tryApplyNewDataPacks(packRepository, true, this::openDataPackSelectionScreen),
                  (Path)settings.getFirst(),
                  Component.translatable("dataPack.title")
               )
            );
      }
   }

   private void tryApplyNewDataPacks(final PackRepository packRepository, final boolean isDataPackScreen, final Consumer<WorldDataConfiguration> onAbort) {
      List<String> newEnabled = ImmutableList.copyOf(packRepository.getSelectedIds());
      List<String> newDisabled = packRepository.getAvailableIds().stream().filter(id -> !newEnabled.contains(id)).collect(ImmutableList.toImmutableList());
      WorldDataConfiguration newConfig = new WorldDataConfiguration(
         new DataPackConfig(newEnabled, newDisabled), this.uiState.getSettings().dataConfiguration().enabledFeatures()
      );
      if (this.uiState.tryUpdateDataConfiguration(newConfig)) {
         this.minecraft.gui.setScreen(this);
      } else {
         FeatureFlagSet requestedFeatureFlags = packRepository.getRequestedFeatureFlags();
         if (FeatureFlags.isExperimental(requestedFeatureFlags) && isDataPackScreen) {
            this.minecraft.gui.setScreen(new ConfirmExperimentalFeaturesScreen(packRepository.getSelectedPacks(), accepted -> {
               if (accepted) {
                  this.applyNewPackConfig(packRepository, newConfig, onAbort);
               } else {
                  onAbort.accept(this.uiState.getSettings().dataConfiguration());
               }
            }));
         } else {
            this.applyNewPackConfig(packRepository, newConfig, onAbort);
         }
      }
   }

   private void applyNewPackConfig(final PackRepository packRepository, final WorldDataConfiguration newConfig, final Consumer<WorldDataConfiguration> onAbort) {
      this.minecraft.setScreenAndShow(new GenericMessageScreen(Component.translatable("dataPack.validation.working")));
      WorldLoader$InitConfig config = createDefaultLoadConfig(packRepository, newConfig);
      WorldLoader.<DataPackReloadCookie, WorldCreationContext>load(
            config,
            context -> {
               if (context.datapackWorldRegistries().lookupOrThrow(Registries.WORLD_PRESET).listElements().findAny().isEmpty()) {
                  throw new IllegalStateException("Needs at least one world preset to continue");
               } else if (context.datapackWorldRegistries().lookupOrThrow(Registries.BIOME).listElements().findAny().isEmpty()) {
                  throw new IllegalStateException("Needs at least one biome continue");
               } else {
                  WorldCreationContext existingContext = this.uiState.getSettings();
                  DynamicOps<JsonElement> writeOps = existingContext.worldgenLoadContext().createSerializationContext(JsonOps.INSTANCE);
                  DataResult<JsonElement> encoded = WorldGenSettings.CODEC
                     .encodeStart(writeOps, new WorldGenSettings(existingContext.options(), existingContext.selectedDimensions()))
                     .setLifecycle(Lifecycle.stable());
                  DynamicOps<JsonElement> readOps = context.datapackWorldRegistries().createSerializationContext(JsonOps.INSTANCE);
                  WorldGenSettings settings = (WorldGenSettings)encoded.flatMap(r -> WorldGenSettings.CODEC.parse(readOps, r))
                     .getOrThrow(error -> new IllegalStateException("Error parsing worldgen settings after loading data packs: " + error));
                  return new WorldLoader$DataLoadOutput<>(new DataPackReloadCookie(settings, context.dataConfiguration()), context.datapackDimensions());
               }
            },
            (resources, managers, registries, cookie) -> {
               resources.close();
               return new WorldCreationContext(cookie.worldGenSettings(), registries, managers, cookie.dataConfiguration());
            },
            Util.backgroundExecutor(),
            this.minecraft
         )
         .thenApply(settings -> {
            settings.validate();
            return (WorldCreationContext)settings;
         })
         .thenAcceptAsync(this.uiState::setSettings, this.minecraft)
         .handleAsync(
            (nothing, throwable) -> {
               if (throwable != null) {
                  LOGGER.warn("Failed to validate datapack", throwable);
                  this.minecraft
                     .gui
                     .setScreen(
                        new ConfirmScreen(
                           retry -> {
                              if (retry) {
                                 onAbort.accept(this.uiState.getSettings().dataConfiguration());
                              } else {
                                 onAbort.accept(WorldDataConfiguration.DEFAULT);
                              }
                           },
                           Component.translatable("dataPack.validation.failed"),
                           CommonComponents.EMPTY,
                           Component.translatable("dataPack.validation.back"),
                           Component.translatable("dataPack.validation.reset")
                        )
                     );
               } else {
                  this.minecraft.gui.setScreen(this);
               }

               return null;
            },
            this.minecraft
         );
   }

   private static WorldLoader$InitConfig createDefaultLoadConfig(final PackRepository packRepository, final WorldDataConfiguration config) {
      WorldLoader$PackConfig packConfig = new WorldLoader$PackConfig(packRepository, config, false, true);
      return new WorldLoader$InitConfig(packConfig, Commands$CommandSelection.INTEGRATED, LevelBasedPermissionSet.GAMEMASTER);
   }

   private void removeTempDataPackDir() {
      if (this.tempDataPackDir != null && Files.exists(this.tempDataPackDir)) {
         try (Stream<Path> files = Files.walk(this.tempDataPackDir)) {
            files.sorted(Comparator.reverseOrder()).forEach(path -> {
               try {
                  Files.delete(path);
               } catch (IOException var2) {
                  LOGGER.warn("Failed to remove temporary file {}", path, var2);
               }
            });
         } catch (IOException var6) {
            LOGGER.warn("Failed to list temporary dir {}", this.tempDataPackDir);
         }
      }

      this.tempDataPackDir = null;
   }

   private static void copyBetweenDirs(final Path sourceDir, final Path targetDir, final Path sourcePath) {
      try {
         Util.copyBetweenDirs(sourceDir, targetDir, sourcePath);
      } catch (IOException var4) {
         LOGGER.warn("Failed to copy datapack file from {} to {}", sourcePath, targetDir);
         throw new UncheckedIOException(var4);
      }
   }

   private static Optional<LevelStorageSource$LevelStorageAccess> createNewWorldDirectory(
      final Minecraft minecraft, final String worldFolder, @Nullable final Path tempDataPackDir
   ) {
      try {
         LevelStorageSource$LevelStorageAccess access = minecraft.getLevelSource().createAccess(worldFolder);
         if (tempDataPackDir == null) {
            return Optional.of(access);
         }

         try {
            Optional var6;
            try (Stream<Path> files = Files.walk(tempDataPackDir)) {
               Path targetDir = access.getLevelPath(LevelResource.DATAPACK_DIR);
               FileUtil.createDirectoriesSafe(targetDir);
               files.filter(f -> !f.equals(tempDataPackDir)).forEach(source -> copyBetweenDirs(tempDataPackDir, targetDir, source));
               var6 = Optional.of(access);
            }

            return var6;
         } catch (UncheckedIOException | IOException var9) {
            LOGGER.warn("Failed to copy datapacks to world {}", worldFolder, var9);
            access.close();
         }
      } catch (UncheckedIOException | IOException var10) {
         LOGGER.warn("Failed to create access for {}", worldFolder, var10);
      }

      return Optional.empty();
   }

   public static Path createTempDataPackDirFromExistingWorld(final Path sourcePackDir, final Minecraft minecraft) {
      MutableObject<Path> tempDataPackDir = new MutableObject();

      try (Stream<Path> dataPackContents = Files.walk(sourcePackDir)) {
         dataPackContents.filter(p -> !p.equals(sourcePackDir)).forEach(source -> {
            Path targetDir = (Path)tempDataPackDir.get();
            if (targetDir == null) {
               try {
                  targetDir = Files.createTempDirectory("mcworld-");
               } catch (IOException var5) {
                  LOGGER.warn("Failed to create temporary dir");
                  throw new UncheckedIOException(var5);
               }

               tempDataPackDir.setValue(targetDir);
            }

            copyBetweenDirs(sourcePackDir, targetDir, source);
         });
      } catch (UncheckedIOException | IOException var8) {
         LOGGER.warn("Failed to copy datapacks from world {}", sourcePackDir, var8);
         SystemToast.onPackCopyFailure(minecraft, sourcePackDir.toString());
         return null;
      }

      return (Path)tempDataPackDir.get();
   }

   @Nullable
   private Pair<Path, PackRepository> getDataPackSelectionSettings(final WorldDataConfiguration dataConfiguration) {
      Path dataPackDir = this.getOrCreateTempDataPackDir();
      if (dataPackDir != null) {
         if (this.tempDataPackRepository == null) {
            this.tempDataPackRepository = ServerPacksSource.createPackRepository(dataPackDir, this.packValidator);
            this.tempDataPackRepository.reload();
         }

         this.tempDataPackRepository.setSelected(dataConfiguration.dataPacks().getEnabled());
         return Pair.of(dataPackDir, this.tempDataPackRepository);
      } else {
         return null;
      }
   }
}
