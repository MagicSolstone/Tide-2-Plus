package net.minecraft.world.level.levelgen.blending;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public interface BlendingData$BiomeConsumer {
   void consume(final int cellX, final int cellZ, final Holder<Biome> biome);
}
