package net.minecraft.world.level.storage;

import java.nio.file.Path;
import java.time.ZonedDateTime;

public record LevelStorageSource$LevelDirectory(Path path) {
   public String directoryName() {
      return this.path.getFileName().toString();
   }

   public Path dataFile() {
      return this.resourcePath(LevelResource.LEVEL_DATA_FILE);
   }

   public Path oldDataFile() {
      return this.resourcePath(LevelResource.OLD_LEVEL_DATA_FILE);
   }

   public Path corruptedDataFile(final ZonedDateTime time) {
      return this.path.resolve(LevelResource.LEVEL_DATA_FILE.id() + "_corrupted_" + time.format(FileNameDateFormatter.FORMATTER));
   }

   public Path rawDataFile(final ZonedDateTime time) {
      return this.path.resolve(LevelResource.LEVEL_DATA_FILE.id() + "_raw_" + time.format(FileNameDateFormatter.FORMATTER));
   }

   public Path iconFile() {
      return this.resourcePath(LevelResource.ICON_FILE);
   }

   public Path lockFile() {
      return this.resourcePath(LevelResource.LOCK_FILE);
   }

   public Path resourcePath(final LevelResource resource) {
      return this.path.resolve(resource.id());
   }
}
