package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface StopAttackingIfTargetInvalid$StopAttackCondition {
   boolean test(ServerLevel level, LivingEntity target);
}
