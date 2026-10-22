package net.minecraft.util.filefix.virtualfilesystem;

import java.nio.file.Path;
import java.util.List;

public record CopyOnWriteFileSystem$Moves(List<Path> directories, List<FileMove> copiedFiles, List<FileMove> preexistingFiles) {
}
