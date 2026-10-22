package net.minecraft.client.renderer.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface SelectItemModel$ModelSelector<T> {
   ItemModel get(@Nullable T value, @Nullable ClientLevel context);
}
