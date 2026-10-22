package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.App;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.OneShot;

public class BehaviorBuilder<E extends LivingEntity, M> implements App<BehaviorBuilder$Mu<E>, M> {
   private final BehaviorBuilder$TriggerWithResult<E, M> trigger;

   public static <E extends LivingEntity, M> BehaviorBuilder<E, M> unbox(final App<BehaviorBuilder$Mu<E>, M> box) {
      return (BehaviorBuilder<E, M>)box;
   }

   public static <E extends LivingEntity> BehaviorBuilder$Instance<E> instance() {
      return new BehaviorBuilder$Instance<>();
   }

   public static <E extends LivingEntity> OneShot<E> create(
      final Function<BehaviorBuilder$Instance<E>, ? extends App<BehaviorBuilder$Mu<E>, Trigger<E>>> builder
   ) {
      BehaviorBuilder$TriggerWithResult<E, Trigger<E>> resolvedBuilder = get((App<BehaviorBuilder$Mu<E>, Trigger<E>>)builder.apply(instance()));
      return new BehaviorBuilder$1(resolvedBuilder);
   }

   public static <E extends LivingEntity> OneShot<E> sequence(final Trigger<? super E> first, final OneShot<? super E> second) {
      OneShot<E> wrapped = create(i -> i.group(i.ifTriggered(first)).apply(i, var1x -> second::trigger));
      return new BehaviorBuilder$2(wrapped, second);
   }

   public static <E extends LivingEntity> OneShot<E> triggerIf(final Predicate<E> predicate, final OneShot<? super E> behavior) {
      return sequence(triggerIf(predicate), behavior);
   }

   public static <E extends LivingEntity> OneShot<E> triggerIf(final Predicate<E> predicate) {
      return create(i -> i.point((level, body, timestamp) -> predicate.test(body)));
   }

   public static <E extends LivingEntity> OneShot<E> triggerIf(final BiPredicate<ServerLevel, E> predicate) {
      return create(i -> i.point((level, body, timestamp) -> predicate.test(level, body)));
   }

   private static <E extends LivingEntity, M> BehaviorBuilder$TriggerWithResult<E, M> get(final App<BehaviorBuilder$Mu<E>, M> box) {
      return unbox(box).trigger;
   }

   private BehaviorBuilder(final BehaviorBuilder$TriggerWithResult<E, M> trigger) {
      this.trigger = trigger;
   }

   private static <E extends LivingEntity, M> BehaviorBuilder<E, M> create(final BehaviorBuilder$TriggerWithResult<E, M> instanceFactory) {
      return new BehaviorBuilder<>(instanceFactory);
   }
}
