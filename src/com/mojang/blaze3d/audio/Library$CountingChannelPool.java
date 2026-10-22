package com.mojang.blaze3d.audio;

import com.google.common.collect.Sets;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class Library$CountingChannelPool implements Library$ChannelPool {
   private final int limit;
   private final Set<Channel> activeChannels = Sets.newIdentityHashSet();

   public Library$CountingChannelPool(final int limit) {
      this.limit = limit;
   }

   @Nullable
   @Override
   public Channel acquire() {
      if (this.activeChannels.size() >= this.limit) {
         if (SharedConstants.IS_RUNNING_IN_IDE) {
            Library.LOGGER.warn("Maximum sound pool size {} reached", this.limit);
         }

         return null;
      } else {
         Channel channel = Channel.create();
         if (channel != null) {
            this.activeChannels.add(channel);
         }

         return channel;
      }
   }

   @Override
   public boolean release(final Channel channel) {
      if (!this.activeChannels.remove(channel)) {
         return false;
      } else {
         channel.destroy();
         return true;
      }
   }

   @Override
   public void cleanup() {
      this.activeChannels.forEach(Channel::destroy);
      this.activeChannels.clear();
   }

   @Override
   public int getMaxCount() {
      return this.limit;
   }

   @Override
   public int getUsedCount() {
      return this.activeChannels.size();
   }
}
