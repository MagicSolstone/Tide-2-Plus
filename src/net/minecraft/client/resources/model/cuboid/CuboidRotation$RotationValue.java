package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public interface CuboidRotation$RotationValue {
   Matrix4f transformation();
}
