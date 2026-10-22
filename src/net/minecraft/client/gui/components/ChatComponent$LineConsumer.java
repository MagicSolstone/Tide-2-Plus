package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.GuiMessage$Line;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface ChatComponent$LineConsumer {
   void accept(GuiMessage$Line line, int lineIndex, float alpha);
}
