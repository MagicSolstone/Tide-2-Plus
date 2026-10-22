package net.minecraft.client.renderer.gizmos;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public record DrawableGizmoPrimitives$Line(Vec3 start, Vec3 end, int color, float width) {
}
