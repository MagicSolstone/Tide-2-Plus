package net.minecraft.client.renderer.gizmos;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.gizmos.TextGizmo$Style;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public record DrawableGizmoPrimitives$Text(Vec3 pos, String text, TextGizmo$Style style) {
}
