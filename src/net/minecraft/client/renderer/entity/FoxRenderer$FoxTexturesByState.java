package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record FoxRenderer$FoxTexturesByState(FoxRenderer$FoxTexturesByAge idle, FoxRenderer$FoxTexturesByAge sleeping) {
}
