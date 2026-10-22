package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.K1;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jspecify.annotations.Nullable;

public interface MemoryCondition<F extends K1, Value> {
   MemoryModuleType<Value> memory();

   MemoryStatus condition();

   @Nullable
   MemoryAccessor<F, Value> createAccessor(Brain<?> brain, final Optional<Value> value);
}
