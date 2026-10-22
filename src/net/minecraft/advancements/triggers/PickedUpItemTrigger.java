package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.Nullable;

public class PickedUpItemTrigger extends SimpleCriterionTrigger<PickedUpItemTrigger$TriggerInstance> {
   @Override
   public Codec<PickedUpItemTrigger$TriggerInstance> codec() {
      return PickedUpItemTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack itemStack, @Nullable final Entity entity) {
      LootContext context = EntityPredicate.createContext(player, entity);
      this.trigger(player, t -> t.matches(player, itemStack, context));
   }
}
