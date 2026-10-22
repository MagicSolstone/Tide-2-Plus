package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;

@Environment(EnvType.CLIENT)
public class SnowGolemRenderState extends LivingEntityRenderState {
   public final BlockModelRenderState headBlock = new BlockModelRenderState();
}
