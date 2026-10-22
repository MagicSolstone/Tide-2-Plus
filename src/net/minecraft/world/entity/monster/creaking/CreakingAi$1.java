package net.minecraft.world.entity.monster.creaking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Swim;

class CreakingAi$1 extends Swim<Creaking> {
   CreakingAi$1(final float chance) {
      super(chance);
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Creaking body) {
      return body.canMove() && super.checkExtraStartConditions(level, (LivingEntity)body);
   }
}
