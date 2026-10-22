package net.minecraft.world.level.entity;

import net.minecraft.world.entity.Entity$RemovalReason;

public interface EntityInLevelCallback {
   EntityInLevelCallback NULL = new EntityInLevelCallback$1();

   void onMove();

   void onRemove(final Entity$RemovalReason reason);
}
