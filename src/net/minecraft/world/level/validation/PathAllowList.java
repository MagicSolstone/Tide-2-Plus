package net.minecraft.world.level.validation;

import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;

public class PathAllowList implements PathMatcher {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final String COMMENT_PREFIX = "#";
   private final List<PathAllowList$ConfigEntry> entries;
   private final Map<String, PathMatcher> compiledPaths = new ConcurrentHashMap<>();

   public PathAllowList(final List<PathAllowList$ConfigEntry> entries) {
      this.entries = entries;
   }

   public PathMatcher getForFileSystem(final FileSystem fileSystem) {
      return this.compiledPaths.computeIfAbsent(fileSystem.provider().getScheme(), scheme -> {
         List<PathMatcher> compiledMatchers;
         try {
            compiledMatchers = this.entries.stream().map(e -> e.compile(fileSystem)).toList();
         } catch (Exception var5) {
            LOGGER.error("Failed to compile file pattern list", var5);
            return path -> false;
         }
         return switch (compiledMatchers.size()) {
            case 0 -> path -> false;
            case 1 -> (PathMatcher)compiledMatchers.get(0);
            default -> path -> {
               for (PathMatcher matcher : compiledMatchers) {
                  if (matcher.matches(path)) {
                     return true;
                  }
               }

               return false;
            };
         };
      });
   }

   @Override
   public boolean matches(final Path path) {
      return this.getForFileSystem(path.getFileSystem()).matches(path);
   }

   public static PathAllowList readPlain(final BufferedReader reader) {
      return new PathAllowList(reader.lines().flatMap(line -> PathAllowList$ConfigEntry.parse(line).stream()).toList());
   }
}
