package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jspecify.annotations.Nullable;

interface BehaviorBuilder$TriggerWithResult<E extends LivingEntity, R> {
   @Nullable
   R tryTrigger(final ServerLevel level, final E body, final long timestamp);

   Set<MemoryModuleType<?>> memories();

   String debugString();
}
