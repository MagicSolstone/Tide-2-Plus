package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.world.level.block.entity.PotDecorations;

@Environment(EnvType.CLIENT)
public record DecoratedPotSpecialRenderer$Unbaked() implements SpecialModelRenderer$Unbaked<PotDecorations> {
   public static final MapCodec<DecoratedPotSpecialRenderer$Unbaked> MAP_CODEC = MapCodec.unit(new DecoratedPotSpecialRenderer$Unbaked());

   @Override
   public MapCodec<DecoratedPotSpecialRenderer$Unbaked> type() {
      return MAP_CODEC;
   }

   public DecoratedPotSpecialRenderer bake(final SpecialModelRenderer$BakingContext context) {
      return new DecoratedPotSpecialRenderer(new DecoratedPotRenderer(context));
   }
}
