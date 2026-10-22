package net.minecraft.client.renderer.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.special.SpecialModelRenderer$BakingContext;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.RegistryContextSwapper;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ItemModel$BakingContext(
   ModelBaker blockModelBaker,
   EntityModelSet entityModelSet,
   SpriteGetter sprites,
   PlayerSkinRenderCache playerSkinRenderCache,
   MissingItemModel missingItemModel,
   @Nullable RegistryContextSwapper contextSwapper
) implements SpecialModelRenderer$BakingContext {
   public MissingItemModel missingItemModel(final Matrix4fc transformation) {
      return this.missingItemModel.withTransform(transformation);
   }
}
