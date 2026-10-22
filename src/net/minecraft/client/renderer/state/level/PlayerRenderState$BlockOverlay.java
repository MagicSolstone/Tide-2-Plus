package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record PlayerRenderState$BlockOverlay(Identifier atlasLocation, float u0, float v0, float u1, float v1) {
}
