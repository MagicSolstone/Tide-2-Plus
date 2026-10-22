package net.minecraft.world.level.block;

import net.minecraft.world.phys.shapes.VoxelShape;

record Block$ShapePairKey(VoxelShape first, VoxelShape second) {
   @Override
   public boolean equals(final Object o) {
      return o instanceof Block$ShapePairKey that && this.first == that.first && this.second == that.second;
   }

   @Override
   public int hashCode() {
      return System.identityHashCode(this.first) * 31 + System.identityHashCode(this.second);
   }
}
