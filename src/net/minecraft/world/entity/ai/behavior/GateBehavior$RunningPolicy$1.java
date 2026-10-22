package net.minecraft.world.entity.ai.behavior;

import java.util.stream.Stream;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

enum GateBehavior$RunningPolicy$1 {
   @Override
   public <E extends LivingEntity> void apply(final Stream<BehaviorControl<? super E>> behaviors, final ServerLevel level, final E body, final long timestamp) {
      behaviors.filter(goal -> goal.getStatus() == Behavior$Status.STOPPED).filter(goal -> goal.tryStart(level, body, timestamp)).findFirst();
   }
}
