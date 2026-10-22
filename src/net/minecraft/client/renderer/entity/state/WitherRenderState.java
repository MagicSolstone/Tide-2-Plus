package net.minecraft.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WitherRenderState extends LivingEntityRenderState {
   public final float[] xHeadRots = new float[2];
   public final float[] yHeadRots = new float[2];
   public float invulnerableTicks;
   public boolean isPowered;
}
