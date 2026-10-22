package net.minecraft.world.level.storage;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtFormatException;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.visitors.FieldSelector;
import net.minecraft.nbt.visitors.SkipFields;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.WorldLoader$PackConfig;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.util.DirectoryLock;
import net.minecraft.util.FileUtil;
import net.minecraft.util.MemoryReserve;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldDimensions$Complete;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.validation.DirectoryValidator;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import net.minecraft.world.level.validation.PathAllowList;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class LevelStorageSource {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final String TAG_DATA = "Data";
   private static final PathMatcher NO_SYMLINKS_ALLOWED = path -> false;
   public static final String ALLOWED_SYMLINKS_CONFIG_NAME = "allowed_symlinks.txt";
   private static final int DISK_SPACE_WARNING_THRESHOLD = 67108864;
   private static final Component LOAD_FOLDER_ACCESS_MESSAGE = Component.translatable("selectWorld.load_folder_access");
   private final Path baseDir;
   private final Path backupDir;
   private final DataFixer fixerUpper;
   private final DirectoryValidator worldDirValidator;

   public LevelStorageSource(final Path baseDir, final Path backupDir, final DirectoryValidator worldDirValidator, final DataFixer fixerUpper) {
      this.fixerUpper = fixerUpper;

      try {
         FileUtil.createDirectoriesSafe(baseDir);
      } catch (IOException var6) {
         throw new UncheckedIOException(var6);
      }

      this.baseDir = baseDir;
      this.backupDir = backupDir;
      this.worldDirValidator = worldDirValidator;
   }

   public static DirectoryValidator parseValidator(final Path configPath) {
      if (Files.exists(configPath)) {
         try {
            DirectoryValidator var2;
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
               var2 = new DirectoryValidator(PathAllowList.readPlain(reader));
            }

            return var2;
         } catch (Exception var6) {
            LOGGER.error("Failed to parse {}, disallowing all symbolic links", "allowed_symlinks.txt", var6);
         }
      }

      return new DirectoryValidator(NO_SYMLINKS_ALLOWED);
   }

   public static LevelStorageSource createDefault(final Path path) {
      DirectoryValidator validator = parseValidator(path.resolve("allowed_symlinks.txt"));
      return new LevelStorageSource(path, path.resolve("../backups"), validator, DataFixers.getDataFixer());
   }

   public static WorldDataConfiguration readDataConfig(final Dynamic<?> levelData) {
      return WorldDataConfiguration.CODEC.parse(levelData).resultOrPartial(LOGGER::error).orElse(WorldDataConfiguration.DEFAULT);
   }

   public static WorldLoader$PackConfig getPackConfig(final Dynamic<?> levelDataTag, final PackRepository packRepository, final boolean safeMode) {
      return new WorldLoader$PackConfig(packRepository, readDataConfig(levelDataTag), safeMode, false);
   }

   public static LevelDataAndDimensions getLevelDataAndDimensions(
      final LevelStorageSource$LevelStorageAccess worldAccess,
      final Dynamic<?> levelDataTag,
      final WorldDataConfiguration dataConfiguration,
      final Registry<LevelStem> datapackDimensions,
      final HolderLookup$Provider registryAccess
   ) {
      if (DataFixers.getFileFixer().requiresFileFixing(NbtUtils.getDataVersion(levelDataTag))) {
         throw new IllegalStateException("Cannot get level data without file fixing first");
      } else {
         Dynamic<?> dataTag = RegistryOps.injectRegistryContext(levelDataTag, registryAccess);
         WorldGenSettings worldGenSettings = (WorldGenSettings)readExistingSavedData(worldAccess, registryAccess, WorldGenSettings.TYPE)
            .mapOrElse(
               Function.identity(),
               error -> {
                  LOGGER.error(
                     "Unable to read or access the world gen settings file! Falling back to the default settings with a random world seed. {}", error.message()
                  );
                  return new WorldGenSettings(WorldOptions.defaultWithRandomSeed(), new WorldDimensions(datapackDimensions));
               }
            );
         LevelSettings settings = LevelSettings.parse(dataTag, dataConfiguration);
         WorldDimensions$Complete dimensions = worldGenSettings.dimensions().bake(datapackDimensions);
         Lifecycle lifecycle = dimensions.lifecycle().add(registryAccess.allRegistriesLifecycle());
         PrimaryLevelData worldData = PrimaryLevelData.parse(dataTag, settings, dimensions.specialWorldProperty(), lifecycle);
         return LevelDataAndDimensions.create(worldData, worldGenSettings, dimensions);
      }
   }

   public static <T extends SavedData> DataResult<T> readExistingSavedData(
      final LevelStorageSource$LevelStorageAccess access, final HolderLookup$Provider registryAccess, final SavedDataType<T> savedDataType
   ) {
      Path dataLocation = savedDataType.id().withSuffix(".dat").resolveAgainst(access.getLevelPath(LevelResource.DATA));

      CompoundTag fileContents;
      try {
         fileContents = NbtIo.readCompressed(dataLocation, NbtAccounter.unlimitedHeap());
      } catch (IOException var6) {
         return DataResult.error(var6::getMessage);
      }

      return savedDataType.codec().parse(RegistryOps.create(NbtOps.INSTANCE, registryAccess), fileContents.getCompoundOrEmpty("data"));
   }

   public static void writeGameRules(final WorldData worldData, final Path worldFolder, final GameRules gameRules) throws IOException {
      Codec<GameRules> codec = GameRules.codec(worldData.enabledFeatures());
      writeSavedData(worldFolder, NbtOps.INSTANCE, GameRuleMap.TYPE, codec, gameRules);
   }

   public static void writeWorldGenSettings(final RegistryAccess registryAccess, final Path worldFolder, final WorldGenSettings worldGenSettings) throws IOException {
      RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
      writeSavedData(worldFolder, ops, WorldGenSettings.TYPE, WorldGenSettings.CODEC, worldGenSettings);
   }

   private static <T> void writeSavedData(final Path worldFolder, final DynamicOps<Tag> ops, final SavedDataType<?> type, final Codec<T> codec, final T data) throws IOException {
      Tag encoded = (Tag)codec.encodeStart(ops, data).getOrThrow();
      CompoundTag fullTag = new CompoundTag();
      fullTag.put("data", encoded);
      NbtUtils.addCurrentDataVersion(fullTag);
      Path path = type.id().withSuffix(".dat").resolveAgainst(worldFolder.resolve("data"));
      FileUtil.createDirectoriesSafe(path.getParent());
      NbtIo.writeCompressed(fullTag, path);
   }

   public String getName() {
      return "Anvil";
   }

   public LevelStorageSource$LevelCandidates findLevelCandidates() throws LevelStorageException {
      if (!Files.exists(this.baseDir)) {
         try {
            Files.createDirectory(this.baseDir);
         } catch (IOException var5) {
            throw new LevelStorageException(LOAD_FOLDER_ACCESS_MESSAGE, var5);
         }
      } else if (!Files.isDirectory(this.baseDir)) {
         throw new LevelStorageException(LOAD_FOLDER_ACCESS_MESSAGE);
      }

      try {
         LevelStorageSource$LevelCandidates var3;
         try (Stream<Path> paths = Files.list(this.baseDir)) {
            List<LevelStorageSource$LevelDirectory> candidates = paths.filter(x$0 -> Files.isDirectory(x$0))
               .map(LevelStorageSource$LevelDirectory::new)
               .filter(directory -> Files.isRegularFile(directory.dataFile()) || Files.isRegularFile(directory.oldDataFile()))
               .toList();
            var3 = new LevelStorageSource$LevelCandidates(candidates);
         }

         return var3;
      } catch (IOException var7) {
         throw new LevelStorageException(LOAD_FOLDER_ACCESS_MESSAGE);
      }
   }

   public CompletableFuture<List<LevelSummary>> loadLevelSummaries(final LevelStorageSource$LevelCandidates candidates) {
      List<CompletableFuture<LevelSummary>> futures = new ArrayList<>(candidates.levels.size());

      for (LevelStorageSource$LevelDirectory level : candidates.levels) {
         futures.add(CompletableFuture.supplyAsync(() -> {
            boolean locked;
            try {
               locked = DirectoryLock.isLocked(level.path());
            } catch (Exception var13) {
               LOGGER.warn("Failed to read {} lock", level.path(), var13);
               return null;
            }

            try {
               return this.readLevelSummary(level, locked);
            } catch (OutOfMemoryError var12) {
               MemoryReserve.release();
               String detailedMessage = "Ran out of memory trying to read summary of world folder \"" + level.directoryName() + "\"";
               LOGGER.error(LogUtils.FATAL_MARKER, "{}", detailedMessage);
               OutOfMemoryError detailedException = new OutOfMemoryError("Ran out of memory reading level data");
               detailedException.initCause(var12);
               CrashReport crashReport = CrashReport.forThrowable(detailedException, detailedMessage);
               CrashReportCategory worldDetails = crashReport.addCategory("World details");
               worldDetails.setDetail("Folder Name", level.directoryName());

               try {
                  long size = Files.size(level.dataFile());
                  worldDetails.setDetail("level.dat size", size);
               } catch (IOException var11) {
                  worldDetails.setDetailError("level.dat size", var11);
               }

               throw new ReportedException(crashReport);
            }
         }, Util.backgroundExecutor().forName("loadLevelSummaries")));
      }

      return Util.sequenceFailFastAndCancel(futures).thenApply(levels -> levels.stream().filter(Objects::nonNull).sorted().toList());
   }

   private int getStorageVersion() {
      return 19133;
   }

   private static CompoundTag readLevelDataTagRaw(final Path dataFile) throws IOException {
      return NbtIo.readCompressed(dataFile, NbtAccounter.uncompressedQuota());
   }

   private LevelSummary readLevelSummary(final LevelStorageSource$LevelDirectory level, final boolean locked) {
      Path dataFile = level.dataFile();
      if (Files.exists(dataFile)) {
         try {
            if (Files.isSymbolicLink(dataFile)) {
               List<ForbiddenSymlinkInfo> issues = this.worldDirValidator.validateSymlink(dataFile);
               if (!issues.isEmpty()) {
                  LOGGER.warn("{}", ContentValidationException.getMessage(dataFile, issues));
                  return new LevelSummary$SymlinkLevelSummary(level.directoryName(), level.iconFile());
               }
            }

            if (readLightweightData(dataFile) instanceof CompoundTag root) {
               CompoundTag tag = root.getCompoundOrEmpty("Data");
               int dataVersion = NbtUtils.getDataVersion(tag);
               Dynamic<?> updated = DataFixTypes.LEVEL_SUMMARY.updateToCurrentVersion(this.fixerUpper, new Dynamic(NbtOps.INSTANCE, tag), dataVersion);
               return this.makeLevelSummary(updated, level, locked, dataVersion);
            }

            LOGGER.warn("Invalid root tag in {}", dataFile);
         } catch (Exception var9) {
            LOGGER.error("Exception reading {}", dataFile, var9);
         }
      }

      return new LevelSummary$CorruptedLevelSummary(level.directoryName(), level.iconFile(), getFileModificationTime(level));
   }

   private static long getFileModificationTime(final LevelStorageSource$LevelDirectory level) {
      Instant timeStamp = getFileModificationTime(level.dataFile());
      if (timeStamp == null) {
         timeStamp = getFileModificationTime(level.oldDataFile());
      }

      return timeStamp == null ? -1L : timeStamp.toEpochMilli();
   }

   @Nullable
   private static Instant getFileModificationTime(final Path path) {
      try {
         return Files.getLastModifiedTime(path).toInstant();
      } catch (IOException var2) {
         return null;
      }
   }

   private LevelSummary makeLevelSummary(
      final Dynamic<?> dataTag, final LevelStorageSource$LevelDirectory levelDirectory, final boolean locked, final int dataVersion
   ) {
      LevelVersion levelVersion = LevelVersion.parse(dataTag);
      int levelDataVersion = levelVersion.levelDataVersion();
      if (levelDataVersion != 19132 && levelDataVersion != 19133) {
         throw new NbtFormatException("Unknown data version: " + Integer.toHexString(levelDataVersion));
      } else {
         boolean requiresManualConversion = levelDataVersion != this.getStorageVersion();
         boolean requiresFileFixing = DataFixers.getFileFixer().requiresFileFixing(dataVersion);
         Path icon = levelDirectory.iconFile();
         WorldDataConfiguration dataConfiguration = readDataConfig(dataTag);
         LevelSettings settings = LevelSettings.parse(dataTag, dataConfiguration);
         FeatureFlagSet enabledFeatureFlags = parseFeatureFlagsFromSummary(dataTag);
         boolean experimental = FeatureFlags.isExperimental(enabledFeatureFlags);
         return new LevelSummary(
            settings, levelVersion, levelDirectory.directoryName(), requiresManualConversion, requiresFileFixing, locked, experimental, icon
         );
      }
   }

   private static FeatureFlagSet parseFeatureFlagsFromSummary(final Dynamic<?> tag) {
      Set<Identifier> enabledFlags = tag.get("enabled_features")
         .asStream()
         .flatMap(entry -> entry.asString().result().map(Identifier::tryParse).stream())
         .collect(Collectors.toSet());
      return FeatureFlags.REGISTRY.fromNames(enabledFlags, unknownId -> {});
   }

   @Nullable
   private static Tag readLightweightData(final Path dataFile) throws IOException {
      SkipFields parser = new SkipFields(new FieldSelector("Data", CompoundTag.TYPE, "Player"), new FieldSelector("Data", CompoundTag.TYPE, "WorldGenSettings"));
      NbtIo.parseCompressed(dataFile, parser, NbtAccounter.uncompressedQuota());
      return parser.getResult();
   }

   public boolean isNewLevelIdAcceptable(final String levelId) {
      try {
         Path fullPath = this.getLevelPath(levelId);
         Files.createDirectory(fullPath);
         Files.deleteIfExists(fullPath);
         return true;
      } catch (IOException var3) {
         return false;
      }
   }

   public boolean levelExists(final String levelId) {
      try {
         return Files.isDirectory(this.getLevelPath(levelId));
      } catch (InvalidPathException var3) {
         return false;
      }
   }

   public Path getLevelPath(final String levelId) {
      return this.baseDir.resolve(levelId);
   }

   public Path getBaseDir() {
      return this.baseDir;
   }

   public Path getBackupPath() {
      return this.backupDir;
   }

   public LevelStorageSource$LevelStorageAccess validateAndCreateAccess(final String levelId) throws IOException, ContentValidationException {
      Path levelPath = this.getLevelPath(levelId);
      List<ForbiddenSymlinkInfo> validationResults = this.worldDirValidator.validateDirectory(levelPath, true);
      if (!validationResults.isEmpty()) {
         throw new ContentValidationException(levelPath, validationResults);
      } else {
         return new LevelStorageSource$LevelStorageAccess(this, levelId, levelPath);
      }
   }

   public LevelStorageSource$LevelStorageAccess createAccess(final String levelId) throws IOException {
      Path levelPath = this.getLevelPath(levelId);
      return new LevelStorageSource$LevelStorageAccess(this, levelId, levelPath);
   }

   public DirectoryValidator getWorldDirValidator() {
      return this.worldDirValidator;
   }
}
