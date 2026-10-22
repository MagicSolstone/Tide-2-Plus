package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public record DamagePredicate(
   MinMaxBounds$Doubles dealtDamage,
   MinMaxBounds$Doubles takenDamage,
   Optional<EntityPredicate> sourceEntity,
   Optional<Boolean> blocked,
   Optional<DamageSourcePredicate> type
) {
   public static final Codec<DamagePredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MinMaxBounds$Doubles.CODEC.optionalFieldOf("dealt", MinMaxBounds$Doubles.ANY).forGetter(DamagePredicate::dealtDamage),
            MinMaxBounds$Doubles.CODEC.optionalFieldOf("taken", MinMaxBounds$Doubles.ANY).forGetter(DamagePredicate::takenDamage),
            EntityPredicate.CODEC.optionalFieldOf("source_entity").forGetter(DamagePredicate::sourceEntity),
            Codec.BOOL.optionalFieldOf("blocked").forGetter(DamagePredicate::blocked),
            DamageSourcePredicate.CODEC.optionalFieldOf("type").forGetter(DamagePredicate::type)
         )
         .apply(i, DamagePredicate::new)
   );

   public boolean matches(final ServerPlayer player, final DamageSource source, final float originalDamage, final float actualDamage, final boolean blocked) {
      if (!this.dealtDamage.matches(originalDamage)) {
         return false;
      } else if (!this.takenDamage.matches(actualDamage)) {
         return false;
      } else if (this.sourceEntity.isPresent() && !this.sourceEntity.get().matches(player, source.getEntity())) {
         return false;
      } else {
         return this.blocked.isPresent() && this.blocked.get() != blocked ? false : !this.type.isPresent() || this.type.get().matches(player, source);
      }
   }
}
