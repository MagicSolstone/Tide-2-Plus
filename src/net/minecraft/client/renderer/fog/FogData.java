package net.minecraft.client.renderer.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector4f;

@Environment(EnvType.CLIENT)
public class FogData {
   public float environmentalStart;
   public float renderDistanceStart;
   public float environmentalEnd;
   public float renderDistanceEnd;
   public float skyEnd;
   public float cloudEnd;
   public Vector4f color = new Vector4f();
}
