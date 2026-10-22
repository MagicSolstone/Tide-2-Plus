package net.minecraft.client.resources.model;

import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.MissingItemModel;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record ModelBakery$MissingModels(BlockStateModelPart blockPart, BlockStateModel block, MissingItemModel item, FluidModel fluid) {
   public static ModelBakery$MissingModels bake(final ResolvedModel unbaked, final MaterialBaker materials, final ModelBaker$Interner interner) {
      ModelBaker missingModelBakery = new ModelBakery$MissingModels$1(materials, interner);
      TextureSlots textureSlots = unbaked.getTopTextureSlots();
      boolean hasAmbientOcclusion = unbaked.getTopAmbientOcclusion();
      boolean usesBlockLight = unbaked.getTopGuiLight().lightLikeBlock();
      ItemTransforms transforms = unbaked.getTopTransforms();
      QuadCollection geometry = unbaked.bakeTopGeometry(textureSlots, missingModelBakery, BlockModelRotation.IDENTITY);
      Multimap<Identifier, Identifier> forbiddenSprites = SimpleModelWrapper.findNonBlockSprites(geometry);
      if (forbiddenSprites != null) {
         throw new IllegalStateException("Missing block contains sprites from outside of block atlas: " + forbiddenSprites);
      } else {
         Material$Baked particleMaterial = unbaked.resolveParticleMaterial(textureSlots, missingModelBakery);
         SimpleModelWrapper missingModelPart = new SimpleModelWrapper(geometry, hasAmbientOcclusion, particleMaterial);
         BlockStateModel bakedBlockModel = new SingleVariant(missingModelPart);
         MissingItemModel bakedItemModel = new MissingItemModel(geometry.getAll(), new ModelRenderProperties(usesBlockLight, particleMaterial, transforms));
         FluidModel bakedFluidModel = new FluidModel(ChunkSectionLayer.SOLID, particleMaterial, particleMaterial, null, null);
         return new ModelBakery$MissingModels(missingModelPart, bakedBlockModel, bakedItemModel, bakedFluidModel);
      }
   }
}
