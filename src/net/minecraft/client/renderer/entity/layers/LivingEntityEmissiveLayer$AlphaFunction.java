package net.minecraft.client.renderer.entity.layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public interface LivingEntityEmissiveLayer$AlphaFunction<S extends LivingEntityRenderState> {
   float apply(final S state, final float ageInTicks);
}
