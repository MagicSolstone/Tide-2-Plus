package net.minecraft.world.phys.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class EntityCollisionContext$Empty extends EntityCollisionContext {
   protected static final CollisionContext WITHOUT_FLUID_COLLISIONS = new EntityCollisionContext$Empty(false);
   protected static final CollisionContext WITH_FLUID_COLLISIONS = new EntityCollisionContext$Empty(true);

   public EntityCollisionContext$Empty(final boolean alwaysCollideWithFluid) {
      super(false, false, -Double.MAX_VALUE, ItemStack.EMPTY, alwaysCollideWithFluid, null);
   }

   @Override
   public boolean isAbove(final VoxelShape shape, final BlockPos pos, final boolean defaultValue) {
      return defaultValue;
   }
}
