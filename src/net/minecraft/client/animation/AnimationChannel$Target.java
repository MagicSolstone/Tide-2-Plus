package net.minecraft.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public interface AnimationChannel$Target {
   void apply(final ModelPart animationBone, final Vector3f target);
}
