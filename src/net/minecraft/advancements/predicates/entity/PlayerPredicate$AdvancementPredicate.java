package net.minecraft.advancements.predicates.entity;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.advancements.AdvancementProgress;

interface PlayerPredicate$AdvancementPredicate extends Predicate<AdvancementProgress> {
   Codec<PlayerPredicate$AdvancementPredicate> CODEC = Codec.either(
         PlayerPredicate$AdvancementDonePredicate.CODEC, PlayerPredicate$AdvancementCriterionsPredicate.CODEC
      )
      .xmap(Either::unwrap, predicate -> {
         if (predicate instanceof PlayerPredicate$AdvancementDonePredicate done) {
            return Either.left(done);
         } else if (predicate instanceof PlayerPredicate$AdvancementCriterionsPredicate criterions) {
            return Either.right(criterions);
         } else {
            throw new UnsupportedOperationException();
         }
      });
}
