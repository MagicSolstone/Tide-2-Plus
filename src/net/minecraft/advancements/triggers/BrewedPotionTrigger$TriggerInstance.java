package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record BrewedPotionTrigger$TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<PotionsPredicate> potion)
   implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<BrewedPotionTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(BrewedPotionTrigger$TriggerInstance::player),
            PotionsPredicate.CODEC.optionalFieldOf("potion").forGetter(BrewedPotionTrigger$TriggerInstance::potion)
         )
         .apply(i, BrewedPotionTrigger$TriggerInstance::new)
   );

   public static Criterion<BrewedPotionTrigger$TriggerInstance> brewedPotion() {
      return CriteriaTriggers.BREWED_POTION.createCriterion(new BrewedPotionTrigger$TriggerInstance(Optional.empty(), Optional.empty()));
   }

   public boolean matches(final PotionContents potion) {
      return !this.potion.isPresent() || this.potion.get().matches(potion);
   }
}
