package com.mojang.realmsclient.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RealmsClient$CompatibleVersionResponse {
   COMPATIBLE,
   OUTDATED,
   OTHER;
}
