package net.minecraft.client.resources.palette;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public interface PalettedTextureManager$Handle extends UvMapping {
   Identifier textureLocation();
}
