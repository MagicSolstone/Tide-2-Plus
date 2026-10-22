package net.minecraft.client.renderer.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.resources.model.sprite.SpriteGetter;

@Environment(EnvType.CLIENT)
public record BlockEntityRendererProvider$Context(
   BlockEntityRenderDispatcher blockEntityRenderDispatcher,
   BlockModelResolver blockModelResolver,
   ItemModelResolver itemModelResolver,
   EntityRenderDispatcher entityRenderer,
   EntityModelSet entityModelSet,
   Font font,
   SpriteGetter sprites,
   PlayerSkinRenderCache playerSkinRenderCache
) {
   public ModelPart bakeLayer(final ModelLayerLocation id) {
      return this.entityModelSet.bakeLayer(id);
   }
}
