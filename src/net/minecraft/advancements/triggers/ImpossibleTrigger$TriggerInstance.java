package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.world.level.storage.loot.ValidationContextSource;

public record ImpossibleTrigger$TriggerInstance() implements CriterionTriggerInstance {
   public static final Codec<ImpossibleTrigger$TriggerInstance> CODEC = MapCodec.unitCodec(new ImpossibleTrigger$TriggerInstance());

   @Override
   public void validate(final ValidationContextSource validator) {
   }
}
