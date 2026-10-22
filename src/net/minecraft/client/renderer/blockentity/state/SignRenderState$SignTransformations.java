package net.minecraft.client.renderer.blockentity.state;

import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record SignRenderState$SignTransformations(Transformation frontText, Transformation backText) {
   public static final SignRenderState$SignTransformations IDENTITY = new SignRenderState$SignTransformations(Transformation.IDENTITY, Transformation.IDENTITY);
}
