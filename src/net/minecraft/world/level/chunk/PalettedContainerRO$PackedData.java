package net.minecraft.world.level.chunk;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

public record PalettedContainerRO$PackedData<T>(List<T> paletteEntries, Optional<LongStream> storage, int bitsPerEntry) {
   public static final int UNKNOWN_BITS_PER_ENTRY = -1;

   public PalettedContainerRO$PackedData(final List<T> paletteEntries, final Optional<LongStream> storage) {
      this(paletteEntries, storage, -1);
   }
}
