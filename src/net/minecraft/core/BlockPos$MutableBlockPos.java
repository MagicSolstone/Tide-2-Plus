package net.minecraft.core;

import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;

public class BlockPos$MutableBlockPos extends BlockPos {
   public BlockPos$MutableBlockPos() {
      this(0, 0, 0);
   }

   public BlockPos$MutableBlockPos(final int x, final int y, final int z) {
      super(x, y, z);
   }

   public BlockPos$MutableBlockPos(final double x, final double y, final double z) {
      this(Mth.floor(x), Mth.floor(y), Mth.floor(z));
   }

   @Override
   public BlockPos offset(final int x, final int y, final int z) {
      return super.offset(x, y, z).immutable();
   }

   @Override
   public BlockPos multiply(final int scale) {
      return super.multiply(scale).immutable();
   }

   @Override
   public BlockPos relative(final Direction direction, final int steps) {
      return super.relative(direction, steps).immutable();
   }

   @Override
   public BlockPos relative(final Direction$Axis axis, final int steps) {
      return super.relative(axis, steps).immutable();
   }

   @Override
   public BlockPos rotate(final Rotation rotation) {
      return super.rotate(rotation).immutable();
   }

   public BlockPos$MutableBlockPos set(final int x, final int y, final int z) {
      this.setX(x);
      this.setY(y);
      this.setZ(z);
      return this;
   }

   public BlockPos$MutableBlockPos set(final double x, final double y, final double z) {
      return this.set(Mth.floor(x), Mth.floor(y), Mth.floor(z));
   }

   public BlockPos$MutableBlockPos set(final Vec3i vec) {
      return this.set(vec.getX(), vec.getY(), vec.getZ());
   }

   public BlockPos$MutableBlockPos set(final long pos) {
      return this.set(getX(pos), getY(pos), getZ(pos));
   }

   public BlockPos$MutableBlockPos set(final AxisCycle transform, final int x, final int y, final int z) {
      return this.set(transform.cycle(x, y, z, Direction$Axis.X), transform.cycle(x, y, z, Direction$Axis.Y), transform.cycle(x, y, z, Direction$Axis.Z));
   }

   public BlockPos$MutableBlockPos setWithOffset(final Vec3i pos, final Direction direction) {
      return this.set(pos.getX() + direction.getStepX(), pos.getY() + direction.getStepY(), pos.getZ() + direction.getStepZ());
   }

   public BlockPos$MutableBlockPos setWithOffset(final Vec3i pos, final int x, final int y, final int z) {
      return this.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
   }

   public BlockPos$MutableBlockPos setWithOffset(final Vec3i pos, final Vec3i offset) {
      return this.set(pos.getX() + offset.getX(), pos.getY() + offset.getY(), pos.getZ() + offset.getZ());
   }

   public BlockPos$MutableBlockPos move(final Direction direction) {
      return this.move(direction, 1);
   }

   public BlockPos$MutableBlockPos move(final Direction direction, final int steps) {
      return this.set(this.getX() + direction.getStepX() * steps, this.getY() + direction.getStepY() * steps, this.getZ() + direction.getStepZ() * steps);
   }

   public BlockPos$MutableBlockPos move(final int x, final int y, final int z) {
      return this.set(this.getX() + x, this.getY() + y, this.getZ() + z);
   }

   public BlockPos$MutableBlockPos move(final Vec3i pos) {
      return this.set(this.getX() + pos.getX(), this.getY() + pos.getY(), this.getZ() + pos.getZ());
   }

   public BlockPos$MutableBlockPos clamp(final Direction$Axis axis, final int minimum, final int maximum) {
      return switch (axis) {
         case X -> this.set(Mth.clamp(this.getX(), minimum, maximum), this.getY(), this.getZ());
         case Y -> this.set(this.getX(), Mth.clamp(this.getY(), minimum, maximum), this.getZ());
         case Z -> this.set(this.getX(), this.getY(), Mth.clamp(this.getZ(), minimum, maximum));
      };
   }

   public BlockPos$MutableBlockPos setX(final int x) {
      super.setX(x);
      return this;
   }

   public BlockPos$MutableBlockPos setY(final int y) {
      super.setY(y);
      return this;
   }

   public BlockPos$MutableBlockPos setZ(final int z) {
      super.setZ(z);
      return this;
   }

   @Override
   public BlockPos immutable() {
      return new BlockPos(this.getX(), this.getY(), this.getZ());
   }
}
