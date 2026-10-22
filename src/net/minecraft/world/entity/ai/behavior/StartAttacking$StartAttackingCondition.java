package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface StartAttacking$StartAttackingCondition<E> {
   boolean test(ServerLevel level, E body);
}
