package net.minecraft.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.Nullable;

public class KilledByArrowTrigger extends SimpleCriterionTrigger<KilledByArrowTrigger$TriggerInstance> {
   @Override
   public Codec<KilledByArrowTrigger$TriggerInstance> codec() {
      return KilledByArrowTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Collection<Entity> victims, @Nullable final ItemStack firedByWeapon) {
      List<LootContext> victimContexts = Lists.newArrayList();
      Set<EntityType<?>> entityTypes = Sets.newHashSet();

      for (Entity victim : victims) {
         entityTypes.add(victim.getType());
         victimContexts.add(EntityPredicate.createContext(player, victim));
      }

      this.trigger(player, t -> t.matches(victimContexts, entityTypes.size(), firedByWeapon));
   }
}
