package net.minecraft.client.resources.palette;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record PalettedTextureManager$SlotKey(Identifier baseTexture, Identifier paletteId) {
}
