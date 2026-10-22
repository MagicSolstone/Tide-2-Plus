package net.minecraft.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface ModelPart$Visitor {
   void visit(final PoseStack$Pose pose, final String partPath, final int cubeIndex, final ModelPart$Cube cube);
}
