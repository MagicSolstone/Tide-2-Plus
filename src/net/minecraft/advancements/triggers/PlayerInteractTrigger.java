package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerInteractTrigger extends SimpleCriterionTrigger<PlayerInteractTrigger$TriggerInstance> {
   @Override
   public Codec<PlayerInteractTrigger$TriggerInstance> codec() {
      return PlayerInteractTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack itemStack, final Entity interactedWith) {
      LootContext context = EntityPredicate.createContext(player, interactedWith);
      this.trigger(player, t -> t.matches(itemStack, context));
   }
}
