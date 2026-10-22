package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public interface HeadedModel {
   ModelPart getHead();

   default void translateToHead(final PoseStack poseStack) {
      this.getHead().translateAndRotate(poseStack);
   }
}
