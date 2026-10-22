package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public interface ModelBaker {
   ResolvedModel getModel(Identifier location);

   BlockStateModelPart missingBlockModelPart();

   MaterialBaker materials();

   ModelBaker$Interner interner();

   <T> T compute(ModelBaker$SharedOperationKey<T> key);
}
