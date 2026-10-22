package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableTrigger extends SimpleCriterionTrigger<LootTableTrigger$TriggerInstance> {
   @Override
   public Codec<LootTableTrigger$TriggerInstance> codec() {
      return LootTableTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ResourceKey<LootTable> lootTable) {
      this.trigger(player, t -> t.matches(lootTable));
   }
}
