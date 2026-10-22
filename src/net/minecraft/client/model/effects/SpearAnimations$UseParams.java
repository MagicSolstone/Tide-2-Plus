package net.minecraft.client.model.effects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.KineticWeapon$Condition;

@Environment(EnvType.CLIENT)
record SpearAnimations$UseParams(
   float raiseProgress,
   float raiseProgressStart,
   float raiseProgressMiddle,
   float raiseProgressEnd,
   float swayProgress,
   float lowerProgress,
   float raiseBackProgress,
   float swayIntensity,
   float swayScaleSlow,
   float swayScaleFast
) {
   public static SpearAnimations$UseParams fromKineticWeapon(final KineticWeapon kineticWeapon, final float time) {
      int finishRaisingTick = kineticWeapon.delayTicks();
      int finishSwayingTick = kineticWeapon.dismountConditions().map(KineticWeapon$Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
      int startSwayingTick = finishSwayingTick - 20;
      int finishLoweringTick = kineticWeapon.knockbackConditions().map(KineticWeapon$Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
      int startLoweringTick = finishLoweringTick - 40;
      int finishRaisingBackTick = kineticWeapon.damageConditions().map(KineticWeapon$Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
      float raiseProgress = SpearAnimations.progress(time, 0.0F, (float)finishRaisingTick);
      float raiseProgressStart = SpearAnimations.progress(raiseProgress, 0.0F, 0.5F);
      float raiseProgressMiddle = SpearAnimations.progress(raiseProgress, 0.5F, 0.8F);
      float raiseProgressEnd = SpearAnimations.progress(raiseProgress, 0.8F, 1.0F);
      float swayProgress = SpearAnimations.progress(time, (float)startSwayingTick, (float)finishSwayingTick);
      float lowerProgress = Ease.outCubic(Ease.inOutElastic(SpearAnimations.progress(time - 20.0F, (float)startLoweringTick, (float)finishLoweringTick)));
      float raiseBackProgress = SpearAnimations.progress(time, (float)(finishRaisingBackTick - 5), (float)finishRaisingBackTick);
      float swayIntensity = 2.0F * Ease.outCirc(swayProgress) - 2.0F * Ease.inCirc(raiseBackProgress);
      float swayScaleSlow = Mth.sin(time * 19.0F * (float) (Math.PI / 180.0)) * swayIntensity;
      float swayScaleFast = Mth.sin(time * 30.0F * (float) (Math.PI / 180.0)) * swayIntensity;
      return new SpearAnimations$UseParams(
         raiseProgress,
         raiseProgressStart,
         raiseProgressMiddle,
         raiseProgressEnd,
         swayProgress,
         lowerProgress,
         raiseBackProgress,
         swayIntensity,
         swayScaleSlow,
         swayScaleFast
      );
   }
}
