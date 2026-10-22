package net.minecraft.world.level.storage;

import com.google.common.collect.Maps;
import com.mojang.serialization.Dynamic;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.zip.ZipOutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.ReportedNbtException;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.DirectoryLock;
import net.minecraft.util.FileUtil;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.apache.commons.io.function.IORunnable;
import org.jspecify.annotations.Nullable;

public class LevelStorageSource$LevelStorageAccess implements AutoCloseable {
   private DirectoryLock lock;
   private final LevelStorageSource$LevelDirectory levelDirectory;
   private final String levelId;
   private final Map<LevelResource, Path> resources;

   private LevelStorageSource$LevelStorageAccess(final LevelStorageSource this$0, final String levelId, final Path path) throws IOException {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.resources = Maps.newHashMap();
      this.levelId = levelId;
      this.levelDirectory = new LevelStorageSource$LevelDirectory(path);
      this.createLock();
   }

   private void createLock() throws IOException {
      this.lock = DirectoryLock.create(this.levelDirectory.path);
   }

   public void releaseTemporarilyAndRun(final IORunnable runnable) throws IOException {
      this.close();

      try {
         runnable.run();
      } finally {
         this.createLock();
      }
   }

   public long estimateDiskSpace() {
      try {
         return Files.getFileStore(this.levelDirectory.path).getUsableSpace();
      } catch (Exception var2) {
         return Long.MAX_VALUE;
      }
   }

   public boolean checkForLowDiskSpace() {
      return this.estimateDiskSpace() < 67108864L;
   }

   public void safeClose() {
      try {
         this.close();
      } catch (IOException var2) {
         LevelStorageSource.LOGGER.warn("Failed to unlock access to level {}", this.getLevelId(), var2);
      }
   }

   public LevelStorageSource parent() {
      return this.this$0;
   }

   public LevelStorageSource$LevelDirectory getLevelDirectory() {
      return this.levelDirectory;
   }

   public String getLevelId() {
      return this.levelId;
   }

   public Path getLevelPath(final LevelResource resource) {
      return this.resources.computeIfAbsent(resource, this.levelDirectory::resourcePath);
   }

   public Path getDimensionPath(final ResourceKey<Level> name) {
      return DimensionType.getStorageFolder(name, this.levelDirectory.path());
   }

   private void checkLock() {
      if (!this.lock.isValid()) {
         throw new IllegalStateException("Lock is no longer valid");
      }
   }

   public PlayerDataStorage createPlayerStorage() {
      this.checkLock();
      return new PlayerDataStorage(this, this.this$0.fixerUpper);
   }

   public void collectIssues(final boolean useFallback) throws IOException {
      this.checkLock();
      Dynamic<?> unfixedDataTag = this.getUnfixedDataTag(useFallback);
      int dataVersion = NbtUtils.getDataVersion(unfixedDataTag);
      Dynamic<?> fixedDataTag = DataFixTypes.LEVEL.updateToCurrentVersion(this.this$0.fixerUpper, unfixedDataTag, dataVersion);
      this.this$0.makeLevelSummary(fixedDataTag, this.levelDirectory, false, dataVersion);
   }

   public LevelSummary fixAndGetSummary() throws IOException {
      this.checkLock();
      return this.fixAndGetSummaryFromTag(this.getUnfixedDataTag(false));
   }

   public LevelSummary fixAndGetSummaryFromTag(final Dynamic<?> dataTag) {
      this.checkLock();
      int dataVersion = NbtUtils.getDataVersion(dataTag);
      Dynamic<?> dataTagFixed = DataFixTypes.LEVEL_SUMMARY.updateToCurrentVersion(this.this$0.fixerUpper, dataTag, dataVersion);
      return this.this$0.makeLevelSummary(dataTagFixed, this.levelDirectory, false, dataVersion);
   }

   public Dynamic<?> getUnfixedDataTagWithFallback() throws IOException {
      Dynamic<?> unfixedDataTag;
      try {
         unfixedDataTag = this.getUnfixedDataTag(false);
      } catch (NbtException | ReportedNbtException | IOException var3) {
         LevelStorageSource.LOGGER.warn("Failed to load world data from {}", this.levelDirectory.dataFile(), var3);
         LevelStorageSource.LOGGER.info("Attempting to use fallback {}", this.levelDirectory.oldDataFile());
         unfixedDataTag = this.getUnfixedDataTag(true);
         this.restoreLevelDataFromOld();
      }

      return unfixedDataTag;
   }

   public Dynamic<?> getUnfixedDataTag(final boolean useFallback) throws IOException {
      this.checkLock();
      Path dataFile = this.getDataFile(useFallback);
      CompoundTag root = LevelStorageSource.readLevelDataTagRaw(dataFile);
      return new Dynamic(NbtOps.INSTANCE, root.getCompoundOrEmpty("Data"));
   }

   private Path getDataFile(final boolean useFallback) {
      return useFallback ? this.levelDirectory.oldDataFile() : this.levelDirectory.dataFile();
   }

   public void saveDataTag(final WorldData levelData) {
      this.saveDataTag(levelData, null);
   }

   public void saveDataTag(final WorldData levelData, @Nullable final UUID singleplayerUUID) {
      CompoundTag dataTag = levelData.createTag(singleplayerUUID);
      CompoundTag root = new CompoundTag();
      root.put("Data", dataTag);
      this.saveLevelData(root);
   }

   public void saveLevelData(final Dynamic<?> tag) {
      Tag genericTag = (Tag)tag.convert(NbtOps.INSTANCE).getValue();
      CompoundTag root = new CompoundTag();
      root.put("Data", genericTag);
      this.saveLevelData(root);
   }

   private void saveLevelData(final CompoundTag root) {
      Path worldDir = this.levelDirectory.path();

      try {
         Path dataFile = Files.createTempFile(worldDir, "level", ".dat");
         NbtIo.writeCompressed(root, dataFile);
         Path oldDataFile = this.levelDirectory.oldDataFile();
         Path currentFile = this.levelDirectory.dataFile();
         Util.safeReplaceFile(currentFile, dataFile, oldDataFile);
      } catch (Exception var6) {
         LevelStorageSource.LOGGER.error("Failed to save level {}", worldDir, var6);
      }
   }

   public Optional<Path> getIconFile() {
      return !this.lock.isValid() ? Optional.empty() : Optional.of(this.levelDirectory.iconFile());
   }

   public void deleteLevel() throws IOException {
      this.checkLock();
      Path lockPath = this.levelDirectory.lockFile();
      LevelStorageSource.LOGGER.info("Deleting level {}", this.levelId);

      for (int attempt = 1; attempt <= 5; attempt++) {
         LevelStorageSource.LOGGER.info("Attempt {}...", attempt);

         try {
            Files.walkFileTree(this.levelDirectory.path(), new LevelStorageSource$LevelStorageAccess$1(this, lockPath));
            break;
         } catch (IOException var6) {
            if (attempt >= 5) {
               throw var6;
            }

            LevelStorageSource.LOGGER.warn("Failed to delete {}", this.levelDirectory.path(), var6);

            try {
               Thread.sleep(500L);
            } catch (InterruptedException var5) {
            }
         }
      }
   }

   public void renameLevel(final String newName) throws IOException {
      this.modifyLevelDataWithoutDatafix(tag -> tag.putString("LevelName", newName.trim()));
   }

   public void renameAndDropPlayer(final String newName) throws IOException {
      this.modifyLevelDataWithoutDatafix(tag -> {
         tag.putString("LevelName", newName.trim());
         tag.remove("singleplayer_uuid");
      });
   }

   private void modifyLevelDataWithoutDatafix(final Consumer<CompoundTag> updater) throws IOException {
      this.checkLock();
      CompoundTag root = LevelStorageSource.readLevelDataTagRaw(this.levelDirectory.dataFile());
      updater.accept(root.getCompoundOrEmpty("Data"));
      this.saveLevelData(root);
   }

   public long makeWorldBackup() throws IOException {
      this.checkLock();
      String zipFilePrefix = FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now()) + "_" + this.levelId;
      Path root = this.this$0.getBackupPath();

      try {
         FileUtil.createDirectoriesSafe(root);
      } catch (IOException var9) {
         throw new RuntimeException(var9);
      }

      Path zipFilePath = root.resolve(FileUtil.findAvailableName(root, zipFilePrefix, ".zip"));

      try (ZipOutputStream stream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zipFilePath)))) {
         Path rootPath = Paths.get(this.levelId);
         Files.walkFileTree(this.levelDirectory.path(), new LevelStorageSource$LevelStorageAccess$2(this, rootPath, stream));
      }

      return Files.size(zipFilePath);
   }

   public boolean hasWorldData() {
      return Files.exists(this.levelDirectory.dataFile()) || Files.exists(this.levelDirectory.oldDataFile());
   }

   @Override
   public void close() throws IOException {
      this.lock.close();
   }

   public boolean restoreLevelDataFromOld() {
      return Util.safeReplaceOrMoveFile(
         this.levelDirectory.dataFile(), this.levelDirectory.oldDataFile(), this.levelDirectory.corruptedDataFile(ZonedDateTime.now()), true
      );
   }

   @Nullable
   public Instant getFileModificationTime(final boolean fallback) {
      return LevelStorageSource.getFileModificationTime(this.getDataFile(fallback));
   }
}
