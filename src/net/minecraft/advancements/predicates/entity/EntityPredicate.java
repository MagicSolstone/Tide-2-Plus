package net.minecraft.advancements.predicates.entity;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext$Builder;
import net.minecraft.world.level.storage.loot.LootContext$EntityTarget;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class EntityPredicate {
   private static final Codec<Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate>> MAP_CODEC = Codec.dispatchedMap(
      BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE.byNameCodec(), c -> c
   );
   public static final Codec<EntityPredicate> CODEC = MAP_CODEC.xmap(EntityPredicate::new, p -> p.parts);
   private static final Comparator<Entry<Codec<? extends EntitySubPredicate>, EntitySubPredicate>> PREDICATE_TYPE_ORDER = Entry.comparingByKey(
      Comparator.comparing(
         codec -> {
            if (codec == EntityTypePredicate.CODEC) {
               return -1;
            } else if (codec == VehiclePredicate.CODEC
               || codec == PassengerPredicate.CODEC
               || codec == TargetedEntityPredicate.CODEC
               || codec == LightningBoltPredicate.CODEC
               || codec == PlayerPredicate.CODEC) {
               return 1;
            } else {
               return codec == EntityNbtPredicate.CODEC ? 2 : 0;
            }
         }
      )
   );
   private final Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate> parts;
   private final EntitySubPredicate combinedPart;

   public EntityPredicate(final Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate> parts) {
      this.parts = parts;
      this.combinedPart = combine(parts);
   }

   public static Holder<LootItemCondition> wrap(final EntityPredicate$Builder singlePredicate) {
      return wrap(singlePredicate.build());
   }

   public static Optional<Holder<LootItemCondition>> wrap(final Optional<EntityPredicate> singlePredicate) {
      return singlePredicate.map(EntityPredicate::wrap);
   }

   public static List<Holder<LootItemCondition>> wrap(final EntityPredicate$Builder... predicates) {
      return Stream.of(predicates).map(EntityPredicate::wrap).toList();
   }

   public static Holder<LootItemCondition> wrap(final EntityPredicate singlePredicate) {
      return Holder.direct(LootItemEntityPropertyCondition.hasProperties(LootContext$EntityTarget.THIS, singlePredicate).build());
   }

   public boolean matches(final ServerPlayer player, @Nullable final Entity entity) {
      return this.matches(player.level(), player.position(), entity);
   }

   public boolean matches(final ServerLevel level, @Nullable final Vec3 position, @Nullable final Entity entity) {
      return entity == null ? false : this.combinedPart.matches(entity, level, position);
   }

   public static LootContext createContext(final ServerPlayer player, final Entity entity) {
      LootParams lootParams = new LootParams$Builder(player.level())
         .withParameter(LootContextParams.THIS_ENTITY, entity)
         .withParameter(LootContextParams.ORIGIN, player.position())
         .create(LootContextParamSets.ADVANCEMENT_ENTITY);
      return new LootContext$Builder(lootParams).create(Optional.empty());
   }

   @Override
   public boolean equals(final Object o) {
      return o == this || o instanceof EntityPredicate that && Objects.equals(this.parts, that.parts);
   }

   @Override
   public int hashCode() {
      return this.parts.hashCode();
   }

   @Override
   public String toString() {
      return "EntityPredicate[parts=" + this.parts + "]";
   }

   private static EntitySubPredicate combine(final Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate> predicateMap) {
      if (predicateMap.isEmpty()) {
         return EntitySubPredicate.ALWAYS_TRUE;
      } else if (predicateMap.size() == 1) {
         return (EntitySubPredicate)Iterables.getOnlyElement(predicateMap.values());
      } else {
         EntitySubPredicate[] predicates = predicateMap.entrySet()
            .stream()
            .sorted(PREDICATE_TYPE_ORDER)
            .map(Entry::getValue)
            .toArray(EntitySubPredicate[]::new);
         return predicates.length == 2 ? predicates[0].and(predicates[1]) : (entity, level, position) -> {
            for (EntitySubPredicate part : predicates) {
               if (!part.matches(entity, level, position)) {
                  return false;
               }
            }

            return true;
         };
      }
   }
}
