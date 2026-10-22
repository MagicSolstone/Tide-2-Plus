package net.minecraft.client.renderer.entity.layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record HorseMarkingLayer$HorseMarkingTextures(Identifier adult, Identifier baby) {
}
