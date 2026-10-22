package com.mojang.renderpearl.api.pipeline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum PolygonMode {
   FILL,
   WIREFRAME;
}
