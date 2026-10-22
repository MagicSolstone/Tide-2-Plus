package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record MultilineTextField$StringView(int beginIndex, int endIndex) {
   private static final MultilineTextField$StringView EMPTY = new MultilineTextField$StringView(0, 0);
}
