package net.minecraft.util.filefix.access;

import java.nio.file.Path;

@FunctionalInterface
public interface FileResourceType$AccessFactory<T> {
   T create(Path path, int dataVersion);
}
