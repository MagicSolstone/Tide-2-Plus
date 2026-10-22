package net.minecraft.client.gui.screens.dialog.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.input.InputControl;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface InputControlHandler<T extends InputControl> {
   void addControl(T input, Screen screen, InputControlHandler$Output output);
}
