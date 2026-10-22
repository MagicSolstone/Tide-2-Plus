package net.minecraft.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel$Unbaked;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface BuiltInBlockModels$SpecialModelFactory extends BuiltInBlockModels$ModelFactory {
   @Override
   default BlockModel$Unbaked create(final BlockColors colors, final BlockState state) {
      return BuiltInBlockModels.combineSpecialAndBlockModels(this.createSpecial(state), colors, state);
   }

   BlockModel$Unbaked createSpecial(BlockState state);
}
