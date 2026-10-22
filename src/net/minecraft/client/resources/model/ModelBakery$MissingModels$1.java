package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class ModelBakery$MissingModels$1 implements ModelBaker {
   ModelBakery$MissingModels$1(final MaterialBaker val$materials, final ModelBaker$Interner val$interner) {
      this.val$materials = val$materialsx;
      this.val$interner = val$internerx;
   }

   @Override
   public ResolvedModel getModel(final Identifier location) {
      throw new IllegalStateException("Missing model can't have dependencies, but asked for " + location);
   }

   @Override
   public BlockStateModelPart missingBlockModelPart() {
      throw new IllegalStateException();
   }

   @Override
   public <T> T compute(final ModelBaker$SharedOperationKey<T> key) {
      return key.compute(this);
   }

   @Override
   public MaterialBaker materials() {
      return this.val$materials;
   }

   @Override
   public ModelBaker$Interner interner() {
      return this.val$interner;
   }
}
