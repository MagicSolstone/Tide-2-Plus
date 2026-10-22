package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface StopAttackingIfTargetInvalid$TargetErasedCallback<E> {
   void accept(ServerLevel level, E body, LivingEntity target);
}
