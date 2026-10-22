package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public sealed interface Uniform extends UncheckedAutoCloseable permits Uniform$Sampler, Uniform$Ubo, Uniform$Utb {
   @Override
   default void close() {
   }
}
