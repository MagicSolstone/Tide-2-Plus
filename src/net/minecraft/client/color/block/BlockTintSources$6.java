package net.minecraft.client.color.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
class BlockTintSources$6 implements BlockTintSource {
   @Override
   public int color(final BlockState state) {
      return -12012264;
   }

   @Override
   public int colorInWorld(final BlockState state, final BlockAndTintGetter level, final BlockPos pos) {
      return BiomeColors.getAverageFoliageColor(level, pos);
   }
}
