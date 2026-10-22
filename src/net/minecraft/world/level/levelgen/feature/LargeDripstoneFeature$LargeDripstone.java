package net.minecraft.world.level.levelgen.feature;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap$Types;

final class LargeDripstoneFeature$LargeDripstone {
   private BlockPos root;
   private final boolean pointingUp;
   private int radius;
   private final double bluntness;
   private final double scale;

   private LargeDripstoneFeature$LargeDripstone(final BlockPos root, final boolean pointingUp, final int radius, final double bluntness, final double scale) {
      this.root = root;
      this.pointingUp = pointingUp;
      this.radius = radius;
      this.bluntness = bluntness;
      this.scale = scale;
   }

   private int getHeight() {
      return this.getHeightAtRadius(0.0F);
   }

   private boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(final WorldGenLevel level, final LargeDripstoneFeature$WindOffsetter wind) {
      while (this.radius > 1) {
         BlockPos$MutableBlockPos newRoot = this.root.mutable();
         int maxTries = Math.min(10, this.getHeight());

         for (int i = 0; i < maxTries; i++) {
            if (level.getBlockState(newRoot).is(Blocks.LAVA)) {
               return false;
            }

            if (SpeleothemUtils.isCircleMostlyEmbeddedInStone(level, wind.offset(newRoot), this.radius)) {
               this.root = newRoot;
               return true;
            }

            newRoot.move(this.pointingUp ? Direction.DOWN : Direction.UP);
         }

         this.radius /= 2;
      }

      return false;
   }

   private int getHeightAtRadius(final float checkRadius) {
      return (int)SpeleothemUtils.getSpeleothemHeight(checkRadius, this.radius, this.scale, this.bluntness);
   }

   private void placeBlocks(final WorldGenLevel level, final RandomSource random, final LargeDripstoneFeature$WindOffsetter wind) {
      for (int dx = -this.radius; dx <= this.radius; dx++) {
         for (int dz = -this.radius; dz <= this.radius; dz++) {
            float currentRadius = Mth.sqrt(dx * dx + dz * dz);
            if (!(currentRadius > this.radius)) {
               int height = this.getHeightAtRadius(currentRadius);
               if (height > 0) {
                  if (random.nextFloat() < 0.2) {
                     height = (int)(height * Mth.randomBetween(random, 0.8F, 1.0F));
                  }

                  BlockPos$MutableBlockPos pos = this.root.offset(dx, 0, dz).mutable();
                  boolean hasBeenOutOfStone = false;
                  int maxY = this.pointingUp ? level.getHeight(Heightmap$Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) : Integer.MAX_VALUE;

                  for (int i = 0; i < height && pos.getY() < maxY; i++) {
                     BlockPos windAdjustedPos = wind.offset(pos);
                     if (SpeleothemUtils.isEmptyOrWaterOrLava(level, windAdjustedPos)) {
                        hasBeenOutOfStone = true;
                        Block block = SharedConstants.DEBUG_LARGE_DRIPSTONE ? Blocks.GLASS : Blocks.DRIPSTONE_BLOCK;
                        level.setBlock(windAdjustedPos, block.defaultBlockState(), 2);
                     } else if (hasBeenOutOfStone && level.getBlockState(windAdjustedPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
                        break;
                     }

                     pos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                  }
               }
            }
         }
      }
   }

   private boolean isSuitableForWind(final int minRadiusForWind, final float minBluntnessForWind) {
      return this.radius >= minRadiusForWind && this.bluntness >= minBluntnessForWind;
   }
}
