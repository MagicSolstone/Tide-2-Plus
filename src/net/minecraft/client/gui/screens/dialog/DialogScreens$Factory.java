package net.minecraft.client.gui.screens.dialog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.Dialog;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface DialogScreens$Factory<T extends Dialog> {
   DialogScreen<T> create(@Nullable Screen previousScreen, T data, DialogConnectionAccess connectionAccess);
}
