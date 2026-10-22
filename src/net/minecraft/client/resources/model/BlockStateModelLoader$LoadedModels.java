package net.minecraft.client.resources.model;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel$UnbakedRoot;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public record BlockStateModelLoader$LoadedModels(Map<BlockState, BlockStateModel$UnbakedRoot> models) {
}
