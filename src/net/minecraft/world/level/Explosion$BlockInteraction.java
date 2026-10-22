package net.minecraft.world.level;

public enum Explosion$BlockInteraction {
   KEEP(false),
   DESTROY(true),
   DESTROY_WITH_DECAY(true),
   TRIGGER_BLOCK(false);

   private final boolean shouldAffectBlocklikeEntities;

   private Explosion$BlockInteraction(final boolean shouldAffectBlocklikeEntities) {
      this.shouldAffectBlocklikeEntities = shouldAffectBlocklikeEntities;
   }

   public boolean shouldAffectBlocklikeEntities() {
      return this.shouldAffectBlocklikeEntities;
   }
}
