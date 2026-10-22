package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;

public class ImpossibleTrigger implements CriterionTrigger<ImpossibleTrigger$TriggerInstance> {
   @Override
   public Codec<ImpossibleTrigger$TriggerInstance> codec() {
      return ImpossibleTrigger$TriggerInstance.CODEC;
   }
}
