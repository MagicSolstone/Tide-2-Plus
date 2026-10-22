package net.minecraft.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ColorResolver;

@Environment(EnvType.CLIENT)
public interface BlockAndTintGetter extends BlockAndLightGetter {
   BlockAndTintGetter EMPTY = new BlockAndTintGetter$1();

   CardinalLighting cardinalLighting();

   int getBlockTint(BlockPos pos, ColorResolver color);
}
