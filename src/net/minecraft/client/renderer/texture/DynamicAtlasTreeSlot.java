package net.minecraft.client.renderer.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DynamicAtlasTreeSlot {
   int x();

   int y();

   int width();

   int height();
}
