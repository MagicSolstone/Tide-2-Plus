package com.mojang.blaze3d.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum Lighting$Entry {
   LEVEL,
   ITEMS_FLAT,
   ITEMS_3D,
   ENTITY_IN_UI,
   PLAYER_SKIN;
}
