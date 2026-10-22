package com.mojang.renderpearl.api.textures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum FilterMode {
   NEAREST,
   LINEAR;
}
