package net.minecraft.server.packs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.Util;

public class FixedPathPackResources$Builder {
   private final Set<Path> rootPaths = new LinkedHashSet<>();
   private final Map<PackType, Set<Path>> pathsForType = new EnumMap<>(PackType.class);
   private ResourceMetadata metadata = ResourceMetadata.EMPTY;
   private final Set<String> namespaces = new HashSet<>();

   private static boolean validateDirPath(final Path path) {
      if (!Files.exists(path)) {
         return false;
      } else if (!Files.isDirectory(path)) {
         throw new IllegalArgumentException("Path " + path.toAbsolutePath() + " is not directory");
      } else {
         return true;
      }
   }

   public void pushRootPath(final Path path) {
      if (validateDirPath(path)) {
         this.rootPaths.add(path);
      }
   }

   public void pushPathForType(final PackType packType, final Path path) {
      if (validateDirPath(path)) {
         this.pathsForType.computeIfAbsent(packType, var0 -> new LinkedHashSet<>()).add(path);
      }
   }

   public void setMetadata(final ResourceMetadata metadata) {
      this.metadata = metadata;
   }

   public void exposeNamespace(final Collection<String> namespaces) {
      this.namespaces.addAll(namespaces);
   }

   public FixedPathPackResources build(final PackLocationInfo location) {
      return new FixedPathPackResources(
         location,
         this.metadata,
         Set.copyOf(this.namespaces),
         copyAndReverse(this.rootPaths),
         Util.makeEnumMap(PackType.class, packType -> copyAndReverse(this.pathsForType.getOrDefault(packType, Set.of())))
      );
   }

   private static List<Path> copyAndReverse(final Collection<Path> input) {
      List<Path> paths = new ArrayList<>(input);
      Collections.reverse(paths);
      return List.copyOf(paths);
   }
}
