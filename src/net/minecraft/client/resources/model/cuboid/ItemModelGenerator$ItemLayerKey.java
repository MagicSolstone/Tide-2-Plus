package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBaker$SharedOperationKey;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialInfo;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.QuadCollection$Builder;
import net.minecraft.client.resources.model.sprite.Material$Baked;

@Environment(EnvType.CLIENT)
record ItemModelGenerator$ItemLayerKey(Material$Baked material, ModelState modelState, int layerIndex) implements ModelBaker$SharedOperationKey<QuadCollection> {
   public QuadCollection compute(final ModelBaker modelBakery) {
      QuadCollection$Builder builder = new QuadCollection$Builder();
      BakedQuad$MaterialInfo materialInfo = modelBakery.interner()
         .materialInfo(BakedQuad$MaterialInfo.of(this.material, this.material.sprite().transparency(), this.layerIndex, null, 0));
      ItemModelGenerator.bakeExtrudedSprite(builder, modelBakery.interner(), this.modelState, materialInfo);
      return builder.build();
   }
}
