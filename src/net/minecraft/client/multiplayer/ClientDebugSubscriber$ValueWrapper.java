package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record ClientDebugSubscriber$ValueWrapper<T>(T value, long expiresAfterTime) {
   private static final long NO_EXPIRY = -1L;

   public boolean hasExpired(final long gameTime) {
      return this.expiresAfterTime == -1L ? false : gameTime >= this.expiresAfterTime;
   }
}
