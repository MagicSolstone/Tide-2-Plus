package net.minecraft.world.level.levelgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

final class LargeDripstoneFeature$WindOffsetter {
   private final int originY;
   @Nullable
   private final Vec3 windSpeed;
   private final int maxOffset;

   private LargeDripstoneFeature$WindOffsetter(final int originY, final RandomSource random, final FloatProvider windSpeedRange, final int maxOffset) {
      this.originY = originY;
      this.maxOffset = maxOffset;
      float speed = windSpeedRange.sample(random);
      float direction = Mth.randomBetween(random, 0.0F, (float) Math.PI);
      this.windSpeed = new Vec3(Mth.cos(direction) * speed, 0.0, Mth.sin(direction) * speed);
   }

   private LargeDripstoneFeature$WindOffsetter() {
      this.originY = 0;
      this.windSpeed = null;
      this.maxOffset = 0;
   }

   private static LargeDripstoneFeature$WindOffsetter noWind() {
      return new LargeDripstoneFeature$WindOffsetter();
   }

   private BlockPos offset(final BlockPos pos) {
      if (this.windSpeed == null) {
         return pos;
      } else {
         int dy = this.originY - pos.getY();
         Vec3 totalWindAdjust = this.windSpeed.scale(dy);
         int dx = Mth.clamp(Mth.floor(totalWindAdjust.x), -this.maxOffset, this.maxOffset);
         int dz = Mth.clamp(Mth.floor(totalWindAdjust.z), -this.maxOffset, this.maxOffset);
         return pos.offset(dx, 0, dz);
      }
   }
}
