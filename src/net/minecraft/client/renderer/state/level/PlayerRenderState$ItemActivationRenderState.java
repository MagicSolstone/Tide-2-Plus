package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class PlayerRenderState$ItemActivationRenderState {
   public final ItemStack item;
   public final int ticks;
   public final float offX;
   public final float offY;
   public final ItemStackRenderState itemState = new ItemStackRenderState();

   public PlayerRenderState$ItemActivationRenderState(final ItemStack item, final int ticks, final float offX, final float offY) {
      this.item = item;
      this.ticks = ticks;
      this.offX = offX;
      this.offY = offY;
   }
}
