package net.minecraft.client.renderer.block.dispatch;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public interface BlockStateModel$UnbakedRoot extends ResolvableModel {
   BlockStateModel bake(BlockState blockState, ModelBaker modelBakery);

   Object visualEqualityGroup(BlockState blockState);
}
