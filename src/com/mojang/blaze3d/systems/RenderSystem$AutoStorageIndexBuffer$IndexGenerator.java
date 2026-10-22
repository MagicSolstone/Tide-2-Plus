package com.mojang.blaze3d.systems;

import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
interface RenderSystem$AutoStorageIndexBuffer$IndexGenerator {
   void accept(final IntConsumer consumer, final int start);
}
