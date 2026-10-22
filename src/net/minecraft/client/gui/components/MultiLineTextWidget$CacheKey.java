package net.minecraft.client.gui.components;

import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
record MultiLineTextWidget$CacheKey(Component message, int maxWidth, OptionalInt maxRows) {
}
