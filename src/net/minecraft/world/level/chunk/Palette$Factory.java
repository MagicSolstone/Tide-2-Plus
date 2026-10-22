package net.minecraft.world.level.chunk;

import java.util.List;

public interface Palette$Factory {
   <A> Palette<A> create(int bits, List<A> paletteEntries);
}
