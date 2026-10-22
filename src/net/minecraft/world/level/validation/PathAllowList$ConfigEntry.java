package net.minecraft.world.level.validation;

import com.google.common.annotations.VisibleForTesting;
import java.nio.file.FileSystem;
import java.nio.file.PathMatcher;
import java.util.Optional;

public record PathAllowList$ConfigEntry(PathAllowList$EntryType type, String pattern) {
   public PathMatcher compile(final FileSystem fileSystem) {
      return this.type().compile(fileSystem, this.pattern);
   }

   public static Optional<PathAllowList$ConfigEntry> parse(final String definition) {
      if (definition.isBlank() || definition.startsWith("#")) {
         return Optional.empty();
      } else if (!definition.startsWith("[")) {
         return Optional.of(new PathAllowList$ConfigEntry(PathAllowList$EntryType.PREFIX, definition));
      } else {
         int split = definition.indexOf(93, 1);
         if (split == -1) {
            throw new IllegalArgumentException("Unterminated type in line '" + definition + "'");
         } else {
            String type = definition.substring(1, split);
            String contents = definition.substring(split + 1);

            return switch (type) {
               case "glob", "regex" -> Optional.of(new PathAllowList$ConfigEntry(PathAllowList$EntryType.FILESYSTEM, type + ":" + contents));
               case "prefix" -> Optional.of(new PathAllowList$ConfigEntry(PathAllowList$EntryType.PREFIX, contents));
               default -> throw new IllegalArgumentException("Unsupported definition type in line '" + definition + "'");
            };
         }
      }
   }

   @VisibleForTesting
   static PathAllowList$ConfigEntry glob(final String pattern) {
      return new PathAllowList$ConfigEntry(PathAllowList$EntryType.FILESYSTEM, "glob:" + pattern);
   }

   @VisibleForTesting
   static PathAllowList$ConfigEntry regex(final String pattern) {
      return new PathAllowList$ConfigEntry(PathAllowList$EntryType.FILESYSTEM, "regex:" + pattern);
   }

   @VisibleForTesting
   static PathAllowList$ConfigEntry prefix(final String pattern) {
      return new PathAllowList$ConfigEntry(PathAllowList$EntryType.PREFIX, pattern);
   }
}
