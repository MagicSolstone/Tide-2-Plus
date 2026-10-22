package net.minecraft.client.renderer.special;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.resources.model.sprite.SpriteGetter;

@Environment(EnvType.CLIENT)
public interface SpecialModelRenderer$BakingContext {
   EntityModelSet entityModelSet();

   SpriteGetter sprites();

   PlayerSkinRenderCache playerSkinRenderCache();
}
