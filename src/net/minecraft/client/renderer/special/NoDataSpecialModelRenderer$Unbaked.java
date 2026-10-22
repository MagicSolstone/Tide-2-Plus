package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface NoDataSpecialModelRenderer$Unbaked extends SpecialModelRenderer$Unbaked<Void> {
   @Override
   MapCodec<? extends NoDataSpecialModelRenderer$Unbaked> type();
}
