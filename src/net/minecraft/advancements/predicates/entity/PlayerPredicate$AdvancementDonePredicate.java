package net.minecraft.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.AdvancementProgress;

record PlayerPredicate$AdvancementDonePredicate(boolean state) implements PlayerPredicate$AdvancementPredicate {
   public static final Codec<PlayerPredicate$AdvancementDonePredicate> CODEC = Codec.BOOL
      .xmap(PlayerPredicate$AdvancementDonePredicate::new, PlayerPredicate$AdvancementDonePredicate::state);

   public boolean test(final AdvancementProgress progress) {
      return progress.isDone() == this.state;
   }
}
