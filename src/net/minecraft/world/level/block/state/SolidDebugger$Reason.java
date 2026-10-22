package net.minecraft.world.level.block.state;

public enum SolidDebugger$Reason {
   FORCE_SOLID_ON(true, "forceSolidOn called on properties"),
   LARGE_ENOUGH_COLLISION_SHAPE(true, "collision shape bounds size is large enough"),
   HIGH_ENOUGH_COLLISION_SHAPE(true, "collision shape bounds are higher than 1"),
   FORCE_SOLID_OFF(false, "forceSolidOff called on properties"),
   NULL_CACHE(false, "cache is null"),
   EMPTY_COLLISION_SHAPE(false, "collision shape is empty"),
   FALLTHROUGH(false, "fallthrough");

   private final boolean causesSolid;
   private final String message;

   private SolidDebugger$Reason(final boolean causesSolid, final String message) {
      this.causesSolid = causesSolid;
      this.message = message;
   }
}
