package net.minecraft.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public class AnimationChannel$Targets {
   public static final AnimationChannel$Target POSITION = ModelPart::offsetPos;
   public static final AnimationChannel$Target ROTATION = ModelPart::offsetRotation;
   public static final AnimationChannel$Target SCALE = ModelPart::offsetScale;
}
