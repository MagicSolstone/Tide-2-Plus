package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class TradeTrigger extends SimpleCriterionTrigger<TradeTrigger$TriggerInstance> {
   @Override
   public Codec<TradeTrigger$TriggerInstance> codec() {
      return TradeTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final AbstractVillager villager, final ItemStack itemStack) {
      LootContext villagerContext = EntityPredicate.createContext(player, villager);
      this.trigger(player, t -> t.matches(villagerContext, itemStack));
   }
}
