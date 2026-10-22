package net.minecraft.world.entity.ai.behavior;

import java.util.stream.Stream;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public enum GateBehavior$RunningPolicy {
   RUN_ONE,
   TRY_ALL;

   public abstract <E extends LivingEntity> void apply(
      final Stream<BehaviorControl<? super E>> behaviors, final ServerLevel level, final E body, final long timestamp
   );
}
