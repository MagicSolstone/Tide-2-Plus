package net.minecraft.world.entity.animal.armadillo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;

class ArmadilloAi$1 extends MoveToTargetSink {
   @Override
   protected boolean checkExtraStartConditions(final ServerLevel level, final Mob body) {
      return body instanceof Armadillo armadillo && armadillo.isScared() ? false : super.checkExtraStartConditions(level, body);
   }
}
