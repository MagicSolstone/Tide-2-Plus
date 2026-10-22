package com.mojang.blaze3d.audio;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class Library$1 implements Library$ChannelPool {
   @Nullable
   @Override
   public Channel acquire() {
      return null;
   }

   @Override
   public boolean release(final Channel channel) {
      return false;
   }

   @Override
   public void cleanup() {
   }

   @Override
   public int getMaxCount() {
      return 0;
   }

   @Override
   public int getUsedCount() {
      return 0;
   }
}
