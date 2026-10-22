package net.minecraft.client.renderer.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class LightmapRenderState {
   public boolean needsUpdate = false;
   public float blockFactor;
   public Vector3fc blockLightTint = LightmapRenderStateExtractor.WHITE;
   public float skyFactor;
   public Vector3fc skyLightColor = LightmapRenderStateExtractor.WHITE;
   public Vector3fc ambientColor = LightmapRenderStateExtractor.WHITE;
   public float brightness;
   public float darknessEffectScale;
   public float nightVisionEffectIntensity;
   public Vector3fc nightVisionColor = LightmapRenderStateExtractor.WHITE;
   public float bossOverlayWorldDarkening;
}
