package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PlayerRenderState {
   public boolean hasPlayer;
   @Nullable
   public AvatarRenderState avatarRenderState;
   public final FirstPersonHandsAndItemsRenderState firstPersonHandsAndItems = new FirstPersonHandsAndItemsRenderState();
   public float portalEffectIntensity;
   public float nauseaEffectIntensity;
   public float spinningEffectAngle;
   public boolean isEyeInWater;
   public boolean isOnFire;
   public boolean isUnderWater;
   public double eyePositionY;
   @Nullable
   public PlayerRenderState$BlockOverlay blockOverlay;
   @Nullable
   public PlayerRenderState$WaterOverlay waterOverlay;
   @Nullable
   public PlayerRenderState$ItemActivationRenderState itemActivation;

   public void reset() {
      this.hasPlayer = false;
      this.avatarRenderState = null;
      this.blockOverlay = null;
      this.waterOverlay = null;
      this.itemActivation = null;
   }
}
