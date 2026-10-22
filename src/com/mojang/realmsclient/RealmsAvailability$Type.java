package com.mojang.realmsclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RealmsAvailability$Type {
   SUCCESS,
   INCOMPATIBLE_CLIENT,
   NEEDS_PARENTAL_CONSENT,
   AUTHENTICATION_ERROR,
   UNEXPECTED_ERROR;
}
