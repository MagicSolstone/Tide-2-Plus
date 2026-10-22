package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface StartAttacking$TargetFinder<E> {
   Optional<? extends LivingEntity> get(ServerLevel level, E body);
}
