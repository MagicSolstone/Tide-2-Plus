package net.minecraft.world.level.validation;

import java.nio.file.FileSystem;
import java.nio.file.PathMatcher;

@FunctionalInterface
public interface PathAllowList$EntryType {
   PathAllowList$EntryType FILESYSTEM = FileSystem::getPathMatcher;
   PathAllowList$EntryType PREFIX = (fileSystem, pattern) -> path -> path.toString().startsWith(pattern);

   PathMatcher compile(FileSystem fileSystem, String pattern);
}
