package com.mojang.blaze3d.audio;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
interface Library$ChannelPool {
   @Nullable
   Channel acquire();

   boolean release(Channel channel);

   void cleanup();

   int getMaxCount();

   int getUsedCount();
}
