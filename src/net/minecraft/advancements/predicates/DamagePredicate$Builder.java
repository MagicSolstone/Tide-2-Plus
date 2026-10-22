package net.minecraft.advancements.predicates;

import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;

public class DamagePredicate$Builder {
   private MinMaxBounds$Doubles dealtDamage = MinMaxBounds$Doubles.ANY;
   private MinMaxBounds$Doubles takenDamage = MinMaxBounds$Doubles.ANY;
   private Optional<EntityPredicate> sourceEntity = Optional.empty();
   private Optional<Boolean> blocked = Optional.empty();
   private Optional<DamageSourcePredicate> type = Optional.empty();

   public static DamagePredicate$Builder damageInstance() {
      return new DamagePredicate$Builder();
   }

   public DamagePredicate$Builder dealtDamage(final MinMaxBounds$Doubles dealtDamage) {
      this.dealtDamage = dealtDamage;
      return this;
   }

   public DamagePredicate$Builder takenDamage(final MinMaxBounds$Doubles takenDamage) {
      this.takenDamage = takenDamage;
      return this;
   }

   public DamagePredicate$Builder sourceEntity(final EntityPredicate sourceEntity) {
      this.sourceEntity = Optional.of(sourceEntity);
      return this;
   }

   public DamagePredicate$Builder blocked(final Boolean blocked) {
      this.blocked = Optional.of(blocked);
      return this;
   }

   public DamagePredicate$Builder type(final DamageSourcePredicate type) {
      this.type = Optional.of(type);
      return this;
   }

   public DamagePredicate$Builder type(final DamageSourcePredicate$Builder type) {
      this.type = Optional.of(type.build());
      return this;
   }

   public DamagePredicate build() {
      return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
   }
}
