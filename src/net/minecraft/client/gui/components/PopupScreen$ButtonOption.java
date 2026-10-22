package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
record PopupScreen$ButtonOption(Component message, Consumer<PopupScreen> action) {
}
