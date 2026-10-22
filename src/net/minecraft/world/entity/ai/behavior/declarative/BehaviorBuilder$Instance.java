package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.IdF;
import com.mojang.datafixers.kinds.OptionalBox;
import com.mojang.datafixers.kinds.OptionalBox.Mu;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Unit;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public final class BehaviorBuilder$Instance<E extends LivingEntity> implements Applicative<BehaviorBuilder$Mu<E>, BehaviorBuilder$Instance$Mu<E>> {
   public <Value> Optional<Value> tryGet(final MemoryAccessor<com.mojang.datafixers.kinds.OptionalBox.Mu, Value> box) {
      return OptionalBox.unbox(box.value());
   }

   public <Value> Value get(final MemoryAccessor<com.mojang.datafixers.kinds.IdF.Mu, Value> box) {
      return (Value)IdF.get(box.value());
   }

   public <Value> BehaviorBuilder<E, MemoryAccessor<com.mojang.datafixers.kinds.OptionalBox.Mu, Value>> registered(final MemoryModuleType<Value> memory) {
      return new BehaviorBuilder$PureMemory<>(new MemoryCondition$Registered<>(memory));
   }

   public <Value> BehaviorBuilder<E, MemoryAccessor<com.mojang.datafixers.kinds.IdF.Mu, Value>> present(final MemoryModuleType<Value> memory) {
      return new BehaviorBuilder$PureMemory<>(new MemoryCondition$Present<>(memory));
   }

   public <Value> BehaviorBuilder<E, MemoryAccessor<com.mojang.datafixers.kinds.Const.Mu<Unit>, Value>> absent(final MemoryModuleType<Value> memory) {
      return new BehaviorBuilder$PureMemory<>(new MemoryCondition$Absent<>(memory));
   }

   public BehaviorBuilder<E, Unit> ifTriggered(final Trigger<? super E> dependentTrigger) {
      return new BehaviorBuilder$TriggerWrapper<>(dependentTrigger);
   }

   public <A> BehaviorBuilder<E, A> point(final A a) {
      return new BehaviorBuilder$Constant<>(a);
   }

   public <A> BehaviorBuilder<E, A> point(final Supplier<String> debugString, final A a) {
      return new BehaviorBuilder$Constant<>(a, debugString);
   }

   public <A, R> Function<App<BehaviorBuilder$Mu<E>, A>, App<BehaviorBuilder$Mu<E>, R>> lift1(final App<BehaviorBuilder$Mu<E>, Function<A, R>> function) {
      return a -> {
         BehaviorBuilder$TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
         BehaviorBuilder$TriggerWithResult<E, Function<A, R>> fTrigger = BehaviorBuilder.get(function);
         return BehaviorBuilder.create(new BehaviorBuilder$Instance$1(this, aTrigger, fTrigger));
      };
   }

   public <T, R> BehaviorBuilder<E, R> map(final Function<? super T, ? extends R> func, final App<BehaviorBuilder$Mu<E>, T> ts) {
      BehaviorBuilder$TriggerWithResult<E, T> tTrigger = BehaviorBuilder.get(ts);
      return BehaviorBuilder.create(new BehaviorBuilder$Instance$2(this, tTrigger, func));
   }

   public <A, B, R> BehaviorBuilder<E, R> ap2(
      final App<BehaviorBuilder$Mu<E>, BiFunction<A, B, R>> func, final App<BehaviorBuilder$Mu<E>, A> a, final App<BehaviorBuilder$Mu<E>, B> b
   ) {
      BehaviorBuilder$TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
      BehaviorBuilder$TriggerWithResult<E, B> bTrigger = BehaviorBuilder.get(b);
      BehaviorBuilder$TriggerWithResult<E, BiFunction<A, B, R>> fTrigger = BehaviorBuilder.get(func);
      return BehaviorBuilder.create(new BehaviorBuilder$Instance$3(this, aTrigger, bTrigger, fTrigger));
   }

   public <T1, T2, T3, R> BehaviorBuilder<E, R> ap3(
      final App<BehaviorBuilder$Mu<E>, Function3<T1, T2, T3, R>> func,
      final App<BehaviorBuilder$Mu<E>, T1> t1,
      final App<BehaviorBuilder$Mu<E>, T2> t2,
      final App<BehaviorBuilder$Mu<E>, T3> t3
   ) {
      BehaviorBuilder$TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
      BehaviorBuilder$TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
      BehaviorBuilder$TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
      BehaviorBuilder$TriggerWithResult<E, Function3<T1, T2, T3, R>> fTrigger = BehaviorBuilder.get(func);
      return BehaviorBuilder.create(new BehaviorBuilder$Instance$4(this, t1Trigger, t2Trigger, t3Trigger, fTrigger));
   }

   public <T1, T2, T3, T4, R> BehaviorBuilder<E, R> ap4(
      final App<BehaviorBuilder$Mu<E>, Function4<T1, T2, T3, T4, R>> func,
      final App<BehaviorBuilder$Mu<E>, T1> t1,
      final App<BehaviorBuilder$Mu<E>, T2> t2,
      final App<BehaviorBuilder$Mu<E>, T3> t3,
      final App<BehaviorBuilder$Mu<E>, T4> t4
   ) {
      BehaviorBuilder$TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
      BehaviorBuilder$TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
      BehaviorBuilder$TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
      BehaviorBuilder$TriggerWithResult<E, T4> t4Trigger = BehaviorBuilder.get(t4);
      BehaviorBuilder$TriggerWithResult<E, Function4<T1, T2, T3, T4, R>> fTrigger = BehaviorBuilder.get(func);
      return BehaviorBuilder.create(new BehaviorBuilder$Instance$5(this, t1Trigger, t2Trigger, t3Trigger, t4Trigger, fTrigger));
   }
}
