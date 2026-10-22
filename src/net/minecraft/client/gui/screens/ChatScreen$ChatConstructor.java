package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface ChatScreen$ChatConstructor<T extends ChatScreen> {
   T create(String initial, boolean isDraft);
}
