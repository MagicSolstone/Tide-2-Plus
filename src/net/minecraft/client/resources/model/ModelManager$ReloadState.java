package net.minecraft.client.resources.model;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
record ModelManager$ReloadState(
   ModelBakery$BakingResult bakedModels,
   Object2IntMap<BlockState> modelGroups,
   Map<BlockState, BlockStateModel> blockStateModels,
   Map<BlockState, BlockModel> blockModels,
   Map<Fluid, FluidModel> fluidModels,
   EntityModelSet entityModelSet
) {
}
