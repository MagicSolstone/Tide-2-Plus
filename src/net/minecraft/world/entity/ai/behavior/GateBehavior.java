package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class GateBehavior<E extends LivingEntity> implements BehaviorControl<E> {
   private final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
   private final Set<MemoryModuleType<?>> exitErasedMemories;
   private final GateBehavior$OrderPolicy orderPolicy;
   private final GateBehavior$RunningPolicy runningPolicy;
   private final ShufflingList<BehaviorControl<? super E>> behaviors = new ShufflingList<>();
   private Behavior$Status status = Behavior$Status.STOPPED;

   public GateBehavior(
      final Map<MemoryModuleType<?>, MemoryStatus> entryCondition,
      final Set<MemoryModuleType<?>> exitErasedMemories,
      final GateBehavior$OrderPolicy orderPolicy,
      final GateBehavior$RunningPolicy runningPolicy,
      final List<Pair<? extends BehaviorControl<? super E>, Integer>> behaviors
   ) {
      this.entryCondition = entryCondition;
      this.exitErasedMemories = exitErasedMemories;
      this.orderPolicy = orderPolicy;
      this.runningPolicy = runningPolicy;
      behaviors.forEach(entry -> this.behaviors.add((BehaviorControl)entry.getFirst(), (Integer)entry.getSecond()));
   }

   @Override
   public Behavior$Status getStatus() {
      return this.status;
   }

   @Override
   public Set<MemoryModuleType<?>> getRequiredMemories() {
      Set<MemoryModuleType<?>> memories = new HashSet<>(this.entryCondition.keySet());

      for (BehaviorControl<? super E> behavior : this.behaviors) {
         memories.addAll(behavior.getRequiredMemories());
      }

      return memories;
   }

   private boolean hasRequiredMemories(final E body) {
      for (Entry<MemoryModuleType<?>, MemoryStatus> entry : this.entryCondition.entrySet()) {
         MemoryModuleType<?> memoryType = entry.getKey();
         MemoryStatus requiredStatus = entry.getValue();
         if (!body.getBrain().checkMemory(memoryType, requiredStatus)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public final boolean tryStart(final ServerLevel level, final E body, final long timestamp) {
      if (this.hasRequiredMemories(body)) {
         this.status = Behavior$Status.RUNNING;
         this.orderPolicy.apply(this.behaviors);
         this.runningPolicy.apply(this.behaviors.stream(), level, body, timestamp);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public final void tickOrStop(final ServerLevel level, final E body, final long timestamp) {
      this.behaviors.stream().filter(goal -> goal.getStatus() == Behavior$Status.RUNNING).forEach(goal -> goal.tickOrStop(level, body, timestamp));
      if (this.behaviors.stream().noneMatch(g -> g.getStatus() == Behavior$Status.RUNNING)) {
         this.doStop(level, body, timestamp);
      }
   }

   @Override
   public final void doStop(final ServerLevel level, final E body, final long timestamp) {
      this.status = Behavior$Status.STOPPED;
      this.behaviors.stream().filter(goal -> goal.getStatus() == Behavior$Status.RUNNING).forEach(goal -> goal.doStop(level, body, timestamp));
      this.exitErasedMemories.forEach(body.getBrain()::eraseMemory);
   }

   @Override
   public String debugString() {
      Set<String> runningBehaviours = this.behaviors
         .stream()
         .filter(goal -> goal.getStatus() == Behavior$Status.RUNNING)
         .map(b -> b.getClass().getSimpleName())
         .collect(Collectors.toSet());
      return this.getClass().getSimpleName() + ": " + runningBehaviours;
   }
}
