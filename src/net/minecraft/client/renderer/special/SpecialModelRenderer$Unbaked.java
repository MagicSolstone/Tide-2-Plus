package net.minecraft.client.renderer.special;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface SpecialModelRenderer$Unbaked<T> {
   @Nullable
   SpecialModelRenderer<T> bake(SpecialModelRenderer$BakingContext context);

   MapCodec<? extends SpecialModelRenderer$Unbaked<T>> type();
}
