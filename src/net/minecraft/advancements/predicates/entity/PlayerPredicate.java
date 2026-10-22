package net.minecraft.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap.Entry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.predicates.FoodPredicate;
import net.minecraft.advancements.predicates.GameTypePredicate;
import net.minecraft.advancements.predicates.InputPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult$Type;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record PlayerPredicate(
   MinMaxBounds$Ints level,
   FoodPredicate food,
   GameTypePredicate gameType,
   List<PlayerPredicate$StatMatcher<?>> stats,
   Object2BooleanMap<ResourceKey<Recipe<?>>> recipes,
   Map<Identifier, PlayerPredicate$AdvancementPredicate> advancements,
   Optional<EntityPredicate> lookingAt,
   Optional<InputPredicate> input
) implements EntitySubPredicate {
   public static final int LOOKING_AT_RANGE = 100;
   public static final Codec<PlayerPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MinMaxBounds$Ints.CODEC.optionalFieldOf("level", MinMaxBounds$Ints.ANY).forGetter(PlayerPredicate::level),
            FoodPredicate.CODEC.optionalFieldOf("food", FoodPredicate.ANY).forGetter(PlayerPredicate::food),
            GameTypePredicate.CODEC.optionalFieldOf("gamemode", GameTypePredicate.ANY).forGetter(PlayerPredicate::gameType),
            PlayerPredicate$StatMatcher.CODEC.listOf().optionalFieldOf("stats", List.of()).forGetter(PlayerPredicate::stats),
            ExtraCodecs.object2BooleanMap(Recipe.KEY_CODEC).optionalFieldOf("recipes", Object2BooleanMaps.emptyMap()).forGetter(PlayerPredicate::recipes),
            Codec.unboundedMap(Identifier.CODEC, PlayerPredicate$AdvancementPredicate.CODEC)
               .optionalFieldOf("advancements", Map.of())
               .forGetter(PlayerPredicate::advancements),
            EntityPredicate.CODEC.optionalFieldOf("looking_at").forGetter(PlayerPredicate::lookingAt),
            InputPredicate.CODEC.optionalFieldOf("input").forGetter(PlayerPredicate::input)
         )
         .apply(i, PlayerPredicate::new)
   );

   @Override
   public boolean matches(final Entity entity, final ServerLevel level, @Nullable final Vec3 position) {
      if (!(entity instanceof ServerPlayer player)) {
         return false;
      } else if (!this.level.matches(player.experienceLevel)) {
         return false;
      } else if (!this.food.matches(player.getFoodData())) {
         return false;
      } else if (!this.gameType.matches(player.gameMode())) {
         return false;
      } else {
         StatsCounter stats = player.getStats();

         for (PlayerPredicate$StatMatcher<?> stat : this.stats) {
            if (!stat.matches(stats)) {
               return false;
            }
         }

         ServerRecipeBook recipes = player.getRecipeBook();
         ObjectIterator var13 = this.recipes.object2BooleanEntrySet().iterator();

         while (var13.hasNext()) {
            Entry<ResourceKey<Recipe<?>>> e = (Entry<ResourceKey<Recipe<?>>>)var13.next();
            if (recipes.contains((ResourceKey<Recipe<?>>)e.getKey()) != e.getBooleanValue()) {
               return false;
            }
         }

         if (!this.advancements.isEmpty()) {
            PlayerAdvancements advancements = player.getAdvancements();
            ServerAdvancementManager serverAdvancements = player.level().getServer().getAdvancements();

            for (java.util.Map.Entry<Identifier, PlayerPredicate$AdvancementPredicate> entry : this.advancements.entrySet()) {
               AdvancementHolder advancement = serverAdvancements.get(entry.getKey());
               if (advancement == null || !entry.getValue().test(advancements.getOrStartProgress(advancement))) {
                  return false;
               }
            }
         }

         if (this.lookingAt.isPresent()) {
            Vec3 from = player.getEyePosition();
            Vec3 viewVec = player.getViewVector(1.0F);
            Vec3 to = from.add(viewVec.x * 100.0, viewVec.y * 100.0, viewVec.z * 100.0);
            EntityHitResult lookingAtResult = ProjectileUtil.getEntityHitResult(
               player.level(), player, from, to, new AABB(from, to).inflate(1.0), e -> !e.isSpectator(), 0.0F
            );
            if (lookingAtResult == null || lookingAtResult.getType() != HitResult$Type.ENTITY) {
               return false;
            }

            Entity lookingAtEntity = lookingAtResult.getEntity();
            if (!this.lookingAt.get().matches(player, lookingAtEntity) || !player.hasLineOfSight(lookingAtEntity)) {
               return false;
            }
         }

         return !this.input.isPresent() || this.input.get().matches(player.getLastClientInput());
      }
   }
}
