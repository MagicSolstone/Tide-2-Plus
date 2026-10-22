package net.minecraft.client.color.block;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
public interface BlockTintSource {
   int color(BlockState state);

   default int colorInWorld(final BlockState state, final BlockAndTintGetter level, final BlockPos pos) {
      return this.color(state);
   }

   default int colorAsTerrainParticle(final BlockState state, final BlockAndTintGetter level, final BlockPos pos) {
      return this.colorInWorld(state, level, pos);
   }

   default Set<Property<?>> relevantProperties() {
      return Set.of();
   }
}
