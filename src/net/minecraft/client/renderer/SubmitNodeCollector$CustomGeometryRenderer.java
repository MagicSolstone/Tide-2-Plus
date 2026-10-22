package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SubmitNodeCollector$CustomGeometryRenderer {
   void render(PoseStack$Pose pose, VertexConsumer buffer);
}
