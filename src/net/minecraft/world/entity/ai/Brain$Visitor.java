package net.minecraft.world.entity.ai;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public interface Brain$Visitor {
   <U> void acceptEmpty(MemoryModuleType<U> type);

   <U> void accept(MemoryModuleType<U> type, U value);

   <U> void accept(MemoryModuleType<U> type, U value, long timeToLive);
}
