package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface CycleButton$SpriteSupplier<T> {
   @Nullable
   Identifier apply(CycleButton<T> button, T value);
}
