package net.minecraft.world.level.chunk.storage;

import java.io.IOException;

@FunctionalInterface
interface RegionFileVersion$StreamWrapper<O> {
   O wrap(O stream) throws IOException;
}
