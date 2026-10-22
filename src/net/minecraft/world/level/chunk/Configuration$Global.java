package net.minecraft.world.level.chunk;

import java.util.List;

public record Configuration$Global(int bitsInMemory, int bitsInStorage) implements Configuration {
   @Override
   public boolean alwaysRepack() {
      return true;
   }

   @Override
   public <T> Palette<T> createPalette(final Strategy<T> strategy, final List<T> paletteEntries) {
      return strategy.globalPalette();
   }
}
