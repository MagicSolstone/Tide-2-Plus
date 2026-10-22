package net.minecraft.world.entity.monster.spider;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.SpawnGroupData;
import org.jspecify.annotations.Nullable;

public class Spider$SpiderEffectsGroupData implements SpawnGroupData {
   @Nullable
   public Holder<MobEffect> effect;

   public void setRandomEffect(final RandomSource random) {
      int selection = random.nextInt(5);
      if (selection <= 1) {
         this.effect = MobEffects.SPEED;
      } else if (selection <= 2) {
         this.effect = MobEffects.STRENGTH;
      } else if (selection <= 3) {
         this.effect = MobEffects.REGENERATION;
      } else if (selection <= 4) {
         this.effect = MobEffects.INVISIBILITY;
      }
   }
}
