package com.mojang.realmsclient.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record RealmsTextureManager$RealmsTexture(String image, Identifier textureId) {
}
