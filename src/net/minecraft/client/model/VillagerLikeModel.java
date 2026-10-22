package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public interface VillagerLikeModel<T extends EntityRenderState> {
   void translateToArms(final T state, PoseStack outputPoseStack);
}
