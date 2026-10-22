package net.minecraft.client.color.block;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.RedstoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
class BlockTintSources$10 implements BlockTintSource {
   @Override
   public int color(final BlockState state) {
      return RedstoneWireBlock.getColorForPower(state.getValue(RedstoneWireBlock.POWER));
   }

   @Override
   public Set<Property<?>> relevantProperties() {
      return Set.of(RedstoneWireBlock.POWER);
   }
}
