package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface CacheSlot$Cleaner<C extends CacheSlot$Cleaner<C>> {
   void registerForCleaning(CacheSlot<C, ?> slot);
}
