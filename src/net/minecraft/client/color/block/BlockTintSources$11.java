package net.minecraft.client.color.block;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
class BlockTintSources$11 implements BlockTintSource {
   @Override
   public int color(final BlockState state) {
      int age = state.getValue(StemBlock.AGE);
      return ARGB.color(age * 32, 255 - age * 8, age * 4);
   }

   @Override
   public Set<Property<?>> relevantProperties() {
      return Set.of(StemBlock.AGE);
   }
}
