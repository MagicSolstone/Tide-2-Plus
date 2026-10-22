package net.minecraft.client.renderer.block.model.properties.conditional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public interface ConditionalBlockModelProperty {
   boolean get(BlockState state);
}
