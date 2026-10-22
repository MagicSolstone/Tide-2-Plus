package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import java.util.Collection;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class FishingRodHookedTrigger extends SimpleCriterionTrigger<FishingRodHookedTrigger$TriggerInstance> {
   @Override
   public Codec<FishingRodHookedTrigger$TriggerInstance> codec() {
      return FishingRodHookedTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final ItemStack rod, final FishingHook hook, final Collection<ItemStack> items) {
      LootContext hookedInContext = EntityPredicate.createContext(player, (Entity)(hook.getHookedIn() != null ? hook.getHookedIn() : hook));
      this.trigger(player, t -> t.matches(rod, hookedInContext, items));
   }
}
