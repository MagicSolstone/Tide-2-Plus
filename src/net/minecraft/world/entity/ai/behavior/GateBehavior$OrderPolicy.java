package net.minecraft.world.entity.ai.behavior;

import java.util.function.Consumer;

public enum GateBehavior$OrderPolicy {
   ORDERED(t -> {}),
   SHUFFLED(ShufflingList::shuffle);

   private final Consumer<ShufflingList<?>> consumer;

   private GateBehavior$OrderPolicy(final Consumer<ShufflingList<?>> consumer) {
      this.consumer = consumer;
   }

   public void apply(final ShufflingList<?> list) {
      this.consumer.accept(list);
   }
}
