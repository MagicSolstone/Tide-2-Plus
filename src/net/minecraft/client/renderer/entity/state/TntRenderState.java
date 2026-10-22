package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;

@Environment(EnvType.CLIENT)
public class TntRenderState extends EntityRenderState {
   public float fuseRemainingInTicks;
   public final BlockModelRenderState blockState = new BlockModelRenderState();
}
