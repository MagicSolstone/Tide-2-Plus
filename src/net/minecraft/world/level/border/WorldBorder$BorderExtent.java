package net.minecraft.world.level.border;

import net.minecraft.world.phys.shapes.VoxelShape;

interface WorldBorder$BorderExtent {
   double getMinX(final float deltaPartialTick);

   double getMaxX(final float deltaPartialTick);

   double getMinZ(final float deltaPartialTick);

   double getMaxZ(final float deltaPartialTick);

   double getSize();

   double getLerpSpeed();

   long getLerpTime();

   double getLerpTarget();

   BorderStatus getStatus();

   void onAbsoluteMaxSizeChange();

   void onCenterChange();

   WorldBorder$BorderExtent update();

   VoxelShape getCollisionShape();
}
