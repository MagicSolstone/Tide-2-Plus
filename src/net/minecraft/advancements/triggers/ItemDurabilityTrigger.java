package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemDurabilityTrigger extends SimpleCriterionTrigger<ItemDurabilityTrigger$TriggerInstance> {
   @Override
   public Codec<ItemDurabilityTrigger$TriggerInstance> codec() {
      return ItemDurabilityTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack itemStack, final int newDurability) {
      this.trigger(player, t -> t.matches(itemStack, newDurability));
   }
}
