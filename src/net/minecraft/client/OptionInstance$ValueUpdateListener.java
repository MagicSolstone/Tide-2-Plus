package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface OptionInstance$ValueUpdateListener<T> {
   void valueChanged(T newValue);
}
