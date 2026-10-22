package net.minecraft.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record EntityFlagsPredicate(
   Optional<Boolean> isOnGround,
   Optional<Boolean> isOnFire,
   Optional<Boolean> isCrouching,
   Optional<Boolean> isSprinting,
   Optional<Boolean> isSwimming,
   Optional<Boolean> isFlying,
   Optional<Boolean> isBaby,
   Optional<Boolean> isInWater,
   Optional<Boolean> isFallFlying
) implements EntitySubPredicate {
   public static final Codec<EntityFlagsPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.BOOL.optionalFieldOf("is_on_ground").forGetter(EntityFlagsPredicate::isOnGround),
            Codec.BOOL.optionalFieldOf("is_on_fire").forGetter(EntityFlagsPredicate::isOnFire),
            Codec.BOOL.optionalFieldOf("is_sneaking").forGetter(EntityFlagsPredicate::isCrouching),
            Codec.BOOL.optionalFieldOf("is_sprinting").forGetter(EntityFlagsPredicate::isSprinting),
            Codec.BOOL.optionalFieldOf("is_swimming").forGetter(EntityFlagsPredicate::isSwimming),
            Codec.BOOL.optionalFieldOf("is_flying").forGetter(EntityFlagsPredicate::isFlying),
            Codec.BOOL.optionalFieldOf("is_baby").forGetter(EntityFlagsPredicate::isBaby),
            Codec.BOOL.optionalFieldOf("is_in_water").forGetter(EntityFlagsPredicate::isInWater),
            Codec.BOOL.optionalFieldOf("is_fall_flying").forGetter(EntityFlagsPredicate::isFallFlying)
         )
         .apply(i, EntityFlagsPredicate::new)
   );

   public boolean matches(final Entity entity) {
      if (this.isOnGround.isPresent() && entity.onGround() != this.isOnGround.get()) {
         return false;
      } else if (this.isOnFire.isPresent() && entity.isOnFire() != this.isOnFire.get()) {
         return false;
      } else if (this.isCrouching.isPresent() && entity.isCrouching() != this.isCrouching.get()) {
         return false;
      } else if (this.isSprinting.isPresent() && entity.isSprinting() != this.isSprinting.get()) {
         return false;
      } else if (this.isSwimming.isPresent() && entity.isSwimming() != this.isSwimming.get()) {
         return false;
      } else {
         if (this.isFlying.isPresent()) {
            boolean entityIsFlying = entity instanceof LivingEntity living
               && (living.isFallFlying() || living instanceof Player player && player.getAbilities().flying);
            if (entityIsFlying != this.isFlying.get()) {
               return false;
            }
         }

         if (this.isInWater.isPresent() && entity.isInWater() != this.isInWater.get()) {
            return false;
         } else {
            return this.isFallFlying.isPresent() && entity instanceof LivingEntity livingx && livingx.isFallFlying() != this.isFallFlying.get()
               ? false
               : !(this.isBaby.isPresent() && entity instanceof LivingEntity living) || living.isBaby() == this.isBaby.get();
         }
      }
   }

   @Override
   public boolean matches(final Entity entity, final ServerLevel level, @Nullable final Vec3 position) {
      return this.matches(entity);
   }
}
