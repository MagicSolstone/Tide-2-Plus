package net.minecraft.advancements.predicates;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate$Builder;
import net.minecraft.world.damagesource.DamageType;

public class DamageSourcePredicate$Builder {
   private final Builder<TagPredicate<DamageType>> tags = ImmutableList.builder();
   private Optional<EntityPredicate> directEntity = Optional.empty();
   private Optional<EntityPredicate> sourceEntity = Optional.empty();
   private Optional<Boolean> isDirect = Optional.empty();

   public static DamageSourcePredicate$Builder damageType() {
      return new DamageSourcePredicate$Builder();
   }

   public DamageSourcePredicate$Builder tag(final TagPredicate<DamageType> tag) {
      this.tags.add(tag);
      return this;
   }

   public DamageSourcePredicate$Builder direct(final EntityPredicate$Builder directEntity) {
      this.directEntity = Optional.of(directEntity.build());
      return this;
   }

   public DamageSourcePredicate$Builder source(final EntityPredicate$Builder sourceEntity) {
      this.sourceEntity = Optional.of(sourceEntity.build());
      return this;
   }

   public DamageSourcePredicate$Builder isDirect(final boolean direct) {
      this.isDirect = Optional.of(direct);
      return this;
   }

   public DamageSourcePredicate build() {
      return new DamageSourcePredicate(this.tags.build(), this.directEntity, this.sourceEntity, this.isDirect);
   }
}
