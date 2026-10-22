package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface StringSplitter$LinePosConsumer {
   void accept(final Style style, int start, int end);
}
