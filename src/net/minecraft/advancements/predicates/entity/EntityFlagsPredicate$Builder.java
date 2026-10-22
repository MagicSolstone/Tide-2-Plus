package net.minecraft.advancements.predicates.entity;

import java.util.Optional;

public class EntityFlagsPredicate$Builder {
   private Optional<Boolean> isOnGround = Optional.empty();
   private Optional<Boolean> isOnFire = Optional.empty();
   private Optional<Boolean> isCrouching = Optional.empty();
   private Optional<Boolean> isSprinting = Optional.empty();
   private Optional<Boolean> isSwimming = Optional.empty();
   private Optional<Boolean> isFlying = Optional.empty();
   private Optional<Boolean> isBaby = Optional.empty();
   private Optional<Boolean> isInWater = Optional.empty();
   private Optional<Boolean> isFallFlying = Optional.empty();

   public static EntityFlagsPredicate$Builder flags() {
      return new EntityFlagsPredicate$Builder();
   }

   public EntityFlagsPredicate$Builder setOnGround(final Boolean onGround) {
      this.isOnGround = Optional.of(onGround);
      return this;
   }

   public EntityFlagsPredicate$Builder setOnFire(final Boolean onFire) {
      this.isOnFire = Optional.of(onFire);
      return this;
   }

   public EntityFlagsPredicate$Builder setCrouching(final Boolean crouching) {
      this.isCrouching = Optional.of(crouching);
      return this;
   }

   public EntityFlagsPredicate$Builder setSprinting(final Boolean sprinting) {
      this.isSprinting = Optional.of(sprinting);
      return this;
   }

   public EntityFlagsPredicate$Builder setSwimming(final Boolean swimming) {
      this.isSwimming = Optional.of(swimming);
      return this;
   }

   public EntityFlagsPredicate$Builder setIsFlying(final Boolean flying) {
      this.isFlying = Optional.of(flying);
      return this;
   }

   public EntityFlagsPredicate$Builder setIsBaby(final Boolean baby) {
      this.isBaby = Optional.of(baby);
      return this;
   }

   public EntityFlagsPredicate$Builder setIsInWater(final Boolean inWater) {
      this.isInWater = Optional.of(inWater);
      return this;
   }

   public EntityFlagsPredicate$Builder setIsFallFlying(final Boolean fallFlying) {
      this.isFallFlying = Optional.of(fallFlying);
      return this;
   }

   public EntityFlagsPredicate build() {
      return new EntityFlagsPredicate(
         this.isOnGround, this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isFlying, this.isBaby, this.isInWater, this.isFallFlying
      );
   }
}
