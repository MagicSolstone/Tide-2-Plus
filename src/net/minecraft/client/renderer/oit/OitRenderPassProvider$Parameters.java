package net.minecraft.client.renderer.oit;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class OitRenderPassProvider$Parameters {
   private GpuTextureView depthBoundsTargetView;
   private final GpuTextureView[] transmittanceTargetViews;
   private final GpuTextureView accumulateTargetView;
   private final GpuTextureView depthTextureView;

   public OitRenderPassProvider$Parameters(
      final GpuTextureView depthBoundsTargetView,
      final GpuTextureView[] transmittanceTargetViews,
      final GpuTextureView accumulateTargetView,
      final GpuTextureView depthTextureView
   ) {
      this.depthBoundsTargetView = depthBoundsTargetView;
      this.transmittanceTargetViews = transmittanceTargetViews;
      this.accumulateTargetView = accumulateTargetView;
      this.depthTextureView = depthTextureView;
   }

   public void setDepthBoundsTargetView(final GpuTextureView depthBoundsTargetView) {
      this.depthBoundsTargetView = depthBoundsTargetView;
   }
}
