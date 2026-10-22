package net.minecraft.util.debug;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.ai.Brain$Visitor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class DebugBrainDump$1 implements Brain$Visitor {
   DebugBrainDump$1(final long val$timestamp, final ServerLevel val$level, final List val$result) {
      this.val$timestamp = val$timestampx;
      this.val$level = val$levelx;
      this.val$result = val$resultx;
   }

   @Override
   public <U> void acceptEmpty(final MemoryModuleType<U> type) {
      this.collectResult(type, Optional.empty(), OptionalLong.empty());
   }

   @Override
   public <U> void accept(final MemoryModuleType<U> type, final U value) {
      this.collectResult(type, Optional.of(value), OptionalLong.empty());
   }

   @Override
   public <U> void accept(final MemoryModuleType<U> type, final U value, final long timeToLive) {
      this.collectResult(type, Optional.of(value), OptionalLong.of(this.val$timestamp));
   }

   private void collectResult(final MemoryModuleType<?> memoryType, final Optional<?> value, final OptionalLong ttl) {
      String description = DebugBrainDump.getMemoryDescription(this.val$level, this.val$timestamp, memoryType, value, ttl);
      this.val$result.add(StringUtil.truncateStringIfNecessary(description, 255, true));
   }
}
