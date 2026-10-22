package com.mojang.realmsclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum RealmsMainScreen$LayoutState {
   LOADING,
   NO_REALMS,
   LIST;
}
