package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface ClientDebugSubscriber$ValueMapType<K, V> {
   ClientDebugSubscriber$ValueMap<K, V> get(ClientDebugSubscriber$ValueMaps<V> maps);
}
