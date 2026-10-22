package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClipContext {
   private final Vec3 from;
   private final Vec3 to;
   private final ClipContext$Block block;
   private final ClipContext$Fluid fluid;
   private final CollisionContext collisionContext;

   public ClipContext(final Vec3 from, final Vec3 to, final ClipContext$Block block, final ClipContext$Fluid fluid, final Entity entity) {
      this(from, to, block, fluid, CollisionContext.of(entity));
   }

   public ClipContext(final Vec3 from, final Vec3 to, final ClipContext$Block block, final ClipContext$Fluid fluid, final CollisionContext collisionContext) {
      this.from = from;
      this.to = to;
      this.block = block;
      this.fluid = fluid;
      this.collisionContext = collisionContext;
   }

   public Vec3 getTo() {
      return this.to;
   }

   public Vec3 getFrom() {
      return this.from;
   }

   public VoxelShape getBlockShape(final BlockState blockState, final BlockGetter level, final BlockPos pos) {
      return this.block.get(blockState, level, pos, this.collisionContext);
   }

   public VoxelShape getFluidShape(final FluidState fluidState, final BlockGetter level, final BlockPos pos) {
      return this.fluid.canPick(fluidState) ? fluidState.getShape(level, pos) : Shapes.empty();
   }
}
