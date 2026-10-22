package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.alchemy.PotionContents;

public class BrewedPotionTrigger extends SimpleCriterionTrigger<BrewedPotionTrigger$TriggerInstance> {
   @Override
   public Codec<BrewedPotionTrigger$TriggerInstance> codec() {
      return BrewedPotionTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final PotionContents potion) {
      this.trigger(player, t -> t.matches(potion));
   }
}
