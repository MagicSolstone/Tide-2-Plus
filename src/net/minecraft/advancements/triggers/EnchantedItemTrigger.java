package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class EnchantedItemTrigger extends SimpleCriterionTrigger<EnchantedItemTrigger$TriggerInstance> {
   @Override
   public Codec<EnchantedItemTrigger$TriggerInstance> codec() {
      return EnchantedItemTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack itemStack, final int levels) {
      this.trigger(player, t -> t.matches(itemStack, levels));
   }
}
