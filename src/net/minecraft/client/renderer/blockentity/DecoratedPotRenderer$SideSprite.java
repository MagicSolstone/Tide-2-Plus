package net.minecraft.client.renderer.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;

@Environment(EnvType.CLIENT)
record DecoratedPotRenderer$SideSprite(RenderType renderType, TextureAtlasSprite sprite) {
   public static DecoratedPotRenderer$SideSprite create(final SpriteGetter sprites, final SpriteId spriteId) {
      return new DecoratedPotRenderer$SideSprite(spriteId.renderType(RenderTypes::entitySolid), sprites.get(spriteId));
   }
}
