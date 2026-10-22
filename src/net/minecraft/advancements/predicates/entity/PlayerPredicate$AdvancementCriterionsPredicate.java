package net.minecraft.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap.Entry;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.util.ExtraCodecs;

record PlayerPredicate$AdvancementCriterionsPredicate(Object2BooleanMap<String> criterions) implements PlayerPredicate$AdvancementPredicate {
   public static final Codec<PlayerPredicate$AdvancementCriterionsPredicate> CODEC = ExtraCodecs.object2BooleanMap(Codec.STRING)
      .xmap(PlayerPredicate$AdvancementCriterionsPredicate::new, PlayerPredicate$AdvancementCriterionsPredicate::criterions);

   public boolean test(final AdvancementProgress progress) {
      ObjectIterator var2 = this.criterions.object2BooleanEntrySet().iterator();

      while (var2.hasNext()) {
         Entry<String> e = (Entry<String>)var2.next();
         CriterionProgress criterion = progress.getCriterion((String)e.getKey());
         if (criterion == null || criterion.isDone() != e.getBooleanValue()) {
            return false;
         }
      }

      return true;
   }
}
