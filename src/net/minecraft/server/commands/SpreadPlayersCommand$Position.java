package net.minecraft.server.commands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

class SpreadPlayersCommand$Position {
   private double x;
   private double z;

   private SpreadPlayersCommand$Position() {
   }

   public double dist(final SpreadPlayersCommand$Position target) {
      double dx = this.x - target.x;
      double dz = this.z - target.z;
      return Math.sqrt(dx * dx + dz * dz);
   }

   public void normalize() {
      double dist = this.getLength();
      this.x /= dist;
      this.z /= dist;
   }

   public double getLength() {
      return Math.sqrt(this.x * this.x + this.z * this.z);
   }

   public void moveAway(final SpreadPlayersCommand$Position pos) {
      this.x = this.x - pos.x;
      this.z = this.z - pos.z;
   }

   public boolean clamp(final double minX, final double minZ, final double maxX, final double maxZ) {
      boolean changed = false;
      if (this.x < minX) {
         this.x = minX;
         changed = true;
      } else if (this.x > maxX) {
         this.x = maxX;
         changed = true;
      }

      if (this.z < minZ) {
         this.z = minZ;
         changed = true;
      } else if (this.z > maxZ) {
         this.z = maxZ;
         changed = true;
      }

      return changed;
   }

   public int getSpawnY(final BlockGetter level, final int maxHeight) {
      BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos(this.x, (double)(maxHeight + 1), this.z);
      boolean air2Above = level.getBlockState(pos).isAir();
      pos.move(Direction.DOWN);
      boolean air1Above = level.getBlockState(pos).isAir();

      while (pos.getY() > level.getMinY()) {
         pos.move(Direction.DOWN);
         boolean currentIsAir = level.getBlockState(pos).isAir();
         if (!currentIsAir && air1Above && air2Above) {
            return pos.getY() + 1;
         }

         air2Above = air1Above;
         air1Above = currentIsAir;
      }

      return maxHeight + 1;
   }

   public boolean isSafe(final BlockGetter level, final int maxHeight) {
      BlockPos pos = BlockPos.containing(this.x, this.getSpawnY(level, maxHeight) - 1, this.z);
      BlockState state = level.getBlockState(pos);
      return pos.getY() < maxHeight && !state.liquid() && state.is(BlockTags.ENTITIES_CAN_TELEPORT_TO);
   }

   public void randomize(final RandomSource random, final double minX, final double minZ, final double maxX, final double maxZ) {
      this.x = Mth.nextDouble(random, minX, maxX);
      this.z = Mth.nextDouble(random, minZ, maxZ);
   }
}
