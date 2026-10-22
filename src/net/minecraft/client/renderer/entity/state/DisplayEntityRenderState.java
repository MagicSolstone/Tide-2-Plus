package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Display$RenderState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class DisplayEntityRenderState extends EntityRenderState {
   @Nullable
   public Display$RenderState renderState;
   public float interpolationProgress;
   public float entityYRot;
   public float entityXRot;
   public float cameraYRot;
   public float cameraXRot;

   public abstract boolean hasSubState();
}
