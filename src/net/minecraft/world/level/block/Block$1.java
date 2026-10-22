package net.minecraft.world.level.block;

import com.google.common.cache.CacheLoader;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

class Block$1 extends CacheLoader<VoxelShape, Boolean> {
   public Boolean load(final VoxelShape shape) {
      return !Shapes.joinIsNotEmpty(Shapes.block(), shape, BooleanOp.NOT_SAME);
   }
}
