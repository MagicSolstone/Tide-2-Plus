package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public record Particle$LifetimeAlpha(float startAlpha, float endAlpha, float startAtNormalizedAge, float endAtNormalizedAge) {
   public static final Particle$LifetimeAlpha ALWAYS_OPAQUE = new Particle$LifetimeAlpha(1.0F, 1.0F, 0.0F, 1.0F);

   public boolean isOpaque() {
      return this.startAlpha >= 1.0F && this.endAlpha >= 1.0F;
   }

   public float currentAlphaForAge(final int age, final int lifetime, final float partialTickTime) {
      if (Mth.equal(this.startAlpha, this.endAlpha)) {
         return this.startAlpha;
      } else {
         float timeNormalized = Mth.inverseLerp((age + partialTickTime) / lifetime, this.startAtNormalizedAge, this.endAtNormalizedAge);
         return Mth.clampedLerp(timeNormalized, this.startAlpha, this.endAlpha);
      }
   }
}
