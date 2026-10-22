package net.minecraft.util.filefix.virtualfilesystem;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

@FunctionalInterface
interface CopyOnWriteFSProvider$ChannelFactory<C> {
   C newChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException;
}
