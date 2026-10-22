package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum CloudRenderer$RelativeCameraPos {
   ABOVE_CLOUDS,
   INSIDE_CLOUDS,
   BELOW_CLOUDS;
}
