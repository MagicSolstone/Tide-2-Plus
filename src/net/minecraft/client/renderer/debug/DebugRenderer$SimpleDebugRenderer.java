package net.minecraft.client.renderer.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.debug.DebugValueAccess;

@Environment(EnvType.CLIENT)
public interface DebugRenderer$SimpleDebugRenderer {
   void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, final Frustum frustum, final float partialTicks);
}
