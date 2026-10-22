package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.MovingBlockRenderState;

@Environment(EnvType.CLIENT)
public class TransientBlockRenderState {
   public MovingBlockRenderState movingBlockRenderState = new MovingBlockRenderState();
   public long createTimeNs;
   public long liveUntilMs;
}
