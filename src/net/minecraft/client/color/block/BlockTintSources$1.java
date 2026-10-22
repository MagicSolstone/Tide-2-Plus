package net.minecraft.client.color.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
class BlockTintSources$1 implements BlockTintSource {
   BlockTintSources$1(final int val$colorInHand, final int val$colorInWorld) {
      this.val$colorInHand = val$colorInHandx;
      this.val$colorInWorld = val$colorInWorldx;
   }

   @Override
   public int color(final BlockState state) {
      return this.val$colorInHand;
   }

   @Override
   public int colorInWorld(final BlockState state, final BlockAndTintGetter level, final BlockPos pos) {
      return this.val$colorInWorld;
   }
}
