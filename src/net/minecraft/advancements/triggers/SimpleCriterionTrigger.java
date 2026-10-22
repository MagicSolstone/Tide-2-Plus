package net.minecraft.advancements.triggers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.PlayerAdvancements$TriggerInstanceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class SimpleCriterionTrigger<T extends SimpleCriterionTrigger$SimpleInstance> implements CriterionTrigger<T> {
   protected void trigger(final ServerPlayer player, final Predicate<T> matcher) {
      PlayerAdvancements advancements = player.getAdvancements();
      Map<PlayerAdvancements$TriggerInstanceKey, T> listenersForType = advancements.getTriggerMapForType(this);
      if (listenersForType != null && !listenersForType.isEmpty()) {
         LootContext playerContext = EntityPredicate.createContext(player, player);
         List<PlayerAdvancements$TriggerInstanceKey> matchedConditions = null;

         for (Entry<PlayerAdvancements$TriggerInstanceKey, T> entry : listenersForType.entrySet()) {
            T value = entry.getValue();
            if (matcher.test(value)) {
               Optional<Holder<LootItemCondition>> predicate = value.player();
               if (!predicate.isPresent() || predicate.get().value().test(playerContext)) {
                  if (matchedConditions == null) {
                     matchedConditions = new ArrayList<>();
                  }

                  matchedConditions.add(entry.getKey());
               }
            }
         }

         if (matchedConditions != null) {
            for (PlayerAdvancements$TriggerInstanceKey criterion : matchedConditions) {
               advancements.award(criterion.advancement(), criterion.criterion());
            }
         }
      }
   }
}
