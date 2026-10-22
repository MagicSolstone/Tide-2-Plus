package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record ModelFeatureRenderer$CrumblingOverlay(int progress, PoseStack$Pose cameraPose) {
}
