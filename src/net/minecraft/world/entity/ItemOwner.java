package net.minecraft.world.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface ItemOwner {
   Level level();

   Vec3 position();

   float getVisualRotationYInDegrees();

   @Nullable
   default LivingEntity asLivingEntity() {
      return null;
   }

   static ItemOwner offsetFromOwner(final ItemOwner owner, final Vec3 offset) {
      return new ItemOwner$OffsetFromOwner(owner, offset);
   }
}
