package net.minecraft.client.renderer.fog.environment;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class LavaFogEnvironment extends FogEnvironment {
   private static final Vector3fc COLOR = ARGB.vector3fFromRGB24(-6743808);

   @Override
   public Vector3fc getBaseColor(final ClientLevel level, final Camera camera, final int renderDistance, final float partialTicks) {
      return COLOR;
   }

   @Override
   public void setupFog(final FogData fog, final Camera camera, final ClientLevel level, final float renderDistance, final DeltaTracker deltaTracker) {
      if (camera.entity().isSpectator()) {
         fog.environmentalStart = -8.0F;
         fog.environmentalEnd = renderDistance * 0.5F;
      } else if (camera.entity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
         fog.environmentalStart = 0.0F;
         fog.environmentalEnd = 5.0F;
      } else {
         fog.environmentalStart = 0.25F;
         fog.environmentalEnd = 1.0F;
      }

      fog.skyEnd = fog.environmentalEnd;
      fog.cloudEnd = fog.environmentalEnd;
   }

   @Override
   public boolean isApplicable(@Nullable final FogType fogType, final Entity entity) {
      return fogType == FogType.LAVA;
   }
}
