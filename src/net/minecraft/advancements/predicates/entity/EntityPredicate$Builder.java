package net.minecraft.advancements.predicates.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Codec;
import java.util.Map;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.DistancePredicate;
import net.minecraft.advancements.predicates.LocationPredicate$Builder;
import net.minecraft.advancements.predicates.MobEffectsPredicate$Builder;
import net.minecraft.advancements.predicates.NbtPredicate;
import net.minecraft.advancements.predicates.SlotsPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.component.predicates.DataComponentPredicate$Type;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityPredicate$Builder {
   private final Builder<Codec<? extends EntitySubPredicate>, EntitySubPredicate> parts = ImmutableMap.builder();

   public static EntityPredicate$Builder entity() {
      return new EntityPredicate$Builder();
   }

   public <T extends EntitySubPredicate> EntityPredicate$Builder put(final Codec<T> key, final T predicate) {
      this.parts.put(key, predicate);
      return this;
   }

   public EntityPredicate$Builder of(final HolderGetter<EntityType<?>> lookup, final EntityType<?> entityType) {
      return this.entityType(EntityTypePredicate.of(lookup, entityType));
   }

   public EntityPredicate$Builder of(final HolderGetter<EntityType<?>> lookup, final TagKey<EntityType<?>> entityTypeTag) {
      return this.entityType(EntityTypePredicate.of(lookup, entityTypeTag));
   }

   public EntityPredicate$Builder entityType(final EntityTypePredicate entityType) {
      return this.put(EntityTypePredicate.CODEC, entityType);
   }

   public EntityPredicate$Builder distance(final DistancePredicate distanceToPlayer) {
      return this.put(DistanceToPlayerPredicate.CODEC, new DistanceToPlayerPredicate(distanceToPlayer));
   }

   public EntityPredicate$Builder moving(final MovementPredicate movement) {
      return this.put(MovementPredicate.CODEC, movement);
   }

   public EntityPredicate$Builder located(final LocationPredicate$Builder location) {
      return this.put(EntityLocationPredicate.CODEC, new EntityLocationPredicate(location.build()));
   }

   public EntityPredicate$Builder steppingOn(final LocationPredicate$Builder location) {
      return this.put(SteppingOnPredicate.CODEC, new SteppingOnPredicate(location.build()));
   }

   public EntityPredicate$Builder movementAffectedBy(final LocationPredicate$Builder location) {
      return this.put(MovementAffectedByPredicate.CODEC, new MovementAffectedByPredicate(location.build()));
   }

   public EntityPredicate$Builder effects(final MobEffectsPredicate$Builder effects) {
      return this.put(EntityEffectsPredicate.CODEC, new EntityEffectsPredicate(effects.build()));
   }

   public EntityPredicate$Builder nbt(final NbtPredicate nbt) {
      return this.put(EntityNbtPredicate.CODEC, new EntityNbtPredicate(nbt));
   }

   public EntityPredicate$Builder flags(final EntityFlagsPredicate$Builder flags) {
      return this.put(EntityFlagsPredicate.CODEC, flags.build());
   }

   public EntityPredicate$Builder equipment(final EntityEquipmentPredicate$Builder equipment) {
      return this.put(EntityEquipmentPredicate.CODEC, equipment.build());
   }

   public EntityPredicate$Builder equipment(final EntityEquipmentPredicate equipment) {
      return this.put(EntityEquipmentPredicate.CODEC, equipment);
   }

   public EntityPredicate$Builder periodicTick(final int period) {
      return this.put(PeriodicEntityTickPredicate.CODEC, new PeriodicEntityTickPredicate(period));
   }

   public EntityPredicate$Builder vehicle(final EntityPredicate$Builder vehicle) {
      return this.put(VehiclePredicate.CODEC, new VehiclePredicate(vehicle.build()));
   }

   public EntityPredicate$Builder passenger(final EntityPredicate$Builder passenger) {
      return this.put(PassengerPredicate.CODEC, new PassengerPredicate(passenger.build()));
   }

   public EntityPredicate$Builder targetedEntity(final EntityPredicate$Builder targetedEntity) {
      return this.put(TargetedEntityPredicate.CODEC, new TargetedEntityPredicate(targetedEntity.build()));
   }

   public EntityPredicate$Builder team(final String team) {
      return this.put(TeamPredicate.CODEC, new TeamPredicate(team));
   }

   public EntityPredicate$Builder slots(final SlotsPredicate slots) {
      return this.put(EntitySlotsPredicate.CODEC, new EntitySlotsPredicate(slots));
   }

   public EntityPredicate$Builder components(final DataComponentMatchers components) {
      if (!components.exact().isEmpty()) {
         this.components(components.exact());
      }

      if (!components.partial().isEmpty()) {
         this.components(components.partial());
      }

      return this;
   }

   public EntityPredicate$Builder components(final DataComponentExactPredicate components) {
      return this.put(EntityExactDataComponentsPredicate.CODEC, new EntityExactDataComponentsPredicate(components));
   }

   public EntityPredicate$Builder components(final Map<DataComponentPredicate$Type<?>, DataComponentPredicate> components) {
      return this.put(EntityPartialComponentsPredicate.CODEC, new EntityPartialComponentsPredicate(components));
   }

   public EntityPredicate$Builder lightingBolt(final LightningBoltPredicate lightningBolt) {
      return this.put(LightningBoltPredicate.CODEC, lightningBolt);
   }

   public EntityPredicate$Builder player(final PlayerPredicate player) {
      return this.put(PlayerPredicate.CODEC, player);
   }

   public EntityPredicate$Builder sheep(final SheepPredicate sheep) {
      return this.put(SheepPredicate.CODEC, sheep);
   }

   public EntityPredicate$Builder cubeMob(final CubeMobPredicate cubeMob) {
      return this.put(CubeMobPredicate.CODEC, cubeMob);
   }

   public EntityPredicate$Builder raider(final RaiderPredicate raider) {
      return this.put(RaiderPredicate.CODEC, raider);
   }

   public EntityPredicate$Builder fishingHook(final FishingHookPredicate fishingHook) {
      return this.put(FishingHookPredicate.CODEC, fishingHook);
   }

   public EntityPredicate build() {
      return new EntityPredicate(this.parts.buildOrThrow());
   }
}
