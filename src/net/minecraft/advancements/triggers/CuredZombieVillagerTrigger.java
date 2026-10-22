package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.storage.loot.LootContext;

public class CuredZombieVillagerTrigger extends SimpleCriterionTrigger<CuredZombieVillagerTrigger$TriggerInstance> {
   @Override
   public Codec<CuredZombieVillagerTrigger$TriggerInstance> codec() {
      return CuredZombieVillagerTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Zombie zombie, final Villager villager) {
      LootContext zombieContext = EntityPredicate.createContext(player, zombie);
      LootContext villagerContext = EntityPredicate.createContext(player, villager);
      this.trigger(player, t -> t.matches(zombieContext, villagerContext));
   }
}
