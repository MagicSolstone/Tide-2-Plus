package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelRenderState;

@Environment(EnvType.CLIENT)
public class BlockDisplayEntityRenderState extends DisplayEntityRenderState {
   public final BlockModelRenderState blockModel = new BlockModelRenderState();

   @Override
   public boolean hasSubState() {
      return !this.blockModel.isEmpty();
   }
}
