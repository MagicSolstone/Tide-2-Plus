package net.minecraft.client.color.block;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
class BlockTintSources$2 implements BlockTintSource {
   @Override
   public int color(final BlockState state) {
      return GrassColor.getDefaultColor();
   }

   @Override
   public int colorInWorld(final BlockState state, final BlockAndTintGetter level, final BlockPos pos) {
      return BiomeColors.getAverageGrassColor(level, state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos);
   }

   @Override
   public Set<Property<?>> relevantProperties() {
      return Set.of(DoublePlantBlock.HALF);
   }
}
