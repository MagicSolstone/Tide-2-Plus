package net.minecraft.client.resources.model.sprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(EnvType.CLIENT)
public record Material$Baked(TextureAtlasSprite sprite, boolean forceTranslucent) {
}
