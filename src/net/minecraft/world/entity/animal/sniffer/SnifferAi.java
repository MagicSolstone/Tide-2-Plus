package net.minecraft.world.entity.animal.sniffer;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Set;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import org.slf4j.Logger;

public class SnifferAi {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int MAX_LOOK_DISTANCE = 6;
   private static final int SNIFFING_COOLDOWN_TICKS = 9600;
   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
   private static final float SPEED_MULTIPLIER_WHEN_SNIFFING = 1.25F;
   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;

   public static List<ActivityData<Sniffer>> getActivities() {
      return List.of(initCoreActivity(), initIdleActivity(), initSniffingActivity(), initDigActivity());
   }

   private static Sniffer resetSniffing(final Sniffer body) {
      body.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
      body.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
      return body.transitionTo(Sniffer$State.IDLING);
   }

   private static ActivityData<Sniffer> initCoreActivity() {
      return ActivityData.create(
         Activity.CORE,
         0,
         ImmutableList.of(
            new Swim(0.8F), new SnifferAi$1(2.0F), new MoveToTargetSink(500, 700), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
         )
      );
   }

   private static ActivityData<Sniffer> initSniffingActivity() {
      return ActivityData.create(
         Activity.SNIFF,
         ImmutableList.of(Pair.of(0, new SnifferAi$Searching())),
         Set.of(
            Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT)
         )
      );
   }

   private static ActivityData<Sniffer> initDigActivity() {
      return ActivityData.create(
         Activity.DIG,
         ImmutableList.of(Pair.of(0, new SnifferAi$Digging(160, 180)), Pair.of(0, new SnifferAi$FinishedDigging(40))),
         Set.of(
            Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT)
         )
      );
   }

   private static ActivityData<Sniffer> initIdleActivity() {
      return ActivityData.create(
         Activity.IDLE,
         ImmutableList.of(
            Pair.of(0, new SnifferAi$2(EntityTypes.SNIFFER)),
            Pair.of(1, new SnifferAi$3(sniffer -> 1.25F, sniffer -> sniffer.isBaby() ? 2.5 : 3.5)),
            Pair.of(2, new LookAtTargetSink(45, 90)),
            Pair.of(3, new SnifferAi$FeelingHappy(40, 100)),
            Pair.of(
               4,
               new RunOne(
                  ImmutableList.of(
                     Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 2),
                     Pair.of(new SnifferAi$Scenting(40, 80), 1),
                     Pair.of(new SnifferAi$Sniffing(40, 80), 1),
                     Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 6.0F), 1),
                     Pair.of(RandomStroll.stroll(1.0F), 1),
                     Pair.of(new DoNothing(5, 20), 2)
                  )
               )
            )
         ),
         Set.of(Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT))
      );
   }

   public static void updateActivity(final Sniffer body) {
      body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.DIG, Activity.SNIFF, Activity.IDLE));
   }
}
