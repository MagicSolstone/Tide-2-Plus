package net.minecraft.client.renderer.oit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum OitStage {
   DEPTH_BOUNDS,
   TRANSMITTANCE,
   ACCUMULATE;
}
