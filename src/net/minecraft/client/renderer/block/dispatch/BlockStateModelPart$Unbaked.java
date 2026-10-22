package net.minecraft.client.renderer.block.dispatch;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;

@Environment(EnvType.CLIENT)
public interface BlockStateModelPart$Unbaked extends ResolvableModel {
   BlockStateModelPart bake(ModelBaker modelBakery);
}
