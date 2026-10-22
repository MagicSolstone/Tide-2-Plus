package net.minecraft.world.entity;

import java.util.List;
import net.minecraft.world.phys.Vec3;

public enum EntityAttachment {
   PASSENGER(EntityAttachment$Fallback.AT_HEIGHT),
   VEHICLE(EntityAttachment$Fallback.AT_FEET),
   NAME_TAG(EntityAttachment$Fallback.AT_HEIGHT),
   WARDEN_CHEST(EntityAttachment$Fallback.AT_CENTER);

   private final EntityAttachment$Fallback fallback;

   private EntityAttachment(final EntityAttachment$Fallback fallback) {
      this.fallback = fallback;
   }

   public List<Vec3> createFallbackPoints(final float width, final float height) {
      return this.fallback.create(width, height);
   }
}
