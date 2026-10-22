package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface CycleButton$OnValueChange<T> {
   void onValueChange(CycleButton<T> button, T value);
}
