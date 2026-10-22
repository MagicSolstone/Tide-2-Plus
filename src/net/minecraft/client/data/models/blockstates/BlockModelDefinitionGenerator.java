package net.minecraft.client.data.models.blockstates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public interface BlockModelDefinitionGenerator {
   Block block();

   BlockStateModelDispatcher create();
}
