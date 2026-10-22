package net.minecraft.world.entity.player;

public class Abilities {
   private static final boolean DEFAULT_INVULNERABLE = false;
   private static final boolean DEFAULY_FLYING = false;
   private static final boolean DEFAULT_MAY_FLY = false;
   private static final boolean DEFAULT_INSTABUILD = false;
   private static final boolean DEFAULT_MAY_BUILD = true;
   private static final float DEFAULT_FLYING_SPEED = 0.05F;
   private static final float DEFAULT_WALKING_SPEED = 0.1F;
   public boolean invulnerable;
   public boolean flying;
   public boolean mayfly;
   public boolean instabuild;
   public boolean mayBuild = true;
   private float flyingSpeed = 0.05F;
   private float walkingSpeed = 0.1F;

   public float getFlyingSpeed() {
      return this.flyingSpeed;
   }

   public void setFlyingSpeed(final float value) {
      this.flyingSpeed = value;
   }

   public float getWalkingSpeed() {
      return this.walkingSpeed;
   }

   public void setWalkingSpeed(final float value) {
      this.walkingSpeed = value;
   }

   public Abilities$Packed pack() {
      return new Abilities$Packed(this.invulnerable, this.flying, this.mayfly, this.instabuild, this.mayBuild, this.flyingSpeed, this.walkingSpeed);
   }

   public void apply(final Abilities$Packed packed) {
      this.invulnerable = packed.invulnerable;
      this.flying = packed.flying;
      this.mayfly = packed.mayFly;
      this.instabuild = packed.instabuild;
      this.mayBuild = packed.mayBuild;
      this.flyingSpeed = packed.flyingSpeed;
      this.walkingSpeed = packed.walkingSpeed;
   }
}
