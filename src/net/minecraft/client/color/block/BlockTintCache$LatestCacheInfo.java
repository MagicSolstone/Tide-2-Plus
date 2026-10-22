package net.minecraft.client.color.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class BlockTintCache$LatestCacheInfo {
   private int x = Integer.MIN_VALUE;
   private int z = Integer.MIN_VALUE;
   @Nullable
   private BlockTintCache$CacheData cache;

   private BlockTintCache$LatestCacheInfo() {
   }
}
