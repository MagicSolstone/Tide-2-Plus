package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface OptionInstance$CaptionBasedToString<T> {
   Component toString(Component caption, T value);
}
