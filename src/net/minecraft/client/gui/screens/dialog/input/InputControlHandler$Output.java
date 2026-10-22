package net.minecraft.client.gui.screens.dialog.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.server.dialog.action.Action$ValueGetter;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface InputControlHandler$Output {
   void accept(LayoutElement element, Action$ValueGetter valueGetter);
}
