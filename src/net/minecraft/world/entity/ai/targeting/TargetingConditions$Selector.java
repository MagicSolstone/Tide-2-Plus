package net.minecraft.world.entity.ai.targeting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TargetingConditions$Selector {
   boolean test(LivingEntity target, ServerLevel level);
}
