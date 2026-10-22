package net.minecraft.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record AnimationChannel(AnimationChannel$Target target, Keyframe... keyframes) {
}
