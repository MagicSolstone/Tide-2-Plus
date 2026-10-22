package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface OptionInstance$CycleableValueSet$ValueSetter<T> {
   void set(final OptionInstance<T> instance, final T value);
}
