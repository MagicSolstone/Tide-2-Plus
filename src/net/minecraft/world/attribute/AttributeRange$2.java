package net.minecraft.world.attribute;

import com.mojang.serialization.DataResult;
import net.minecraft.util.Mth;

class AttributeRange$2 implements AttributeRange<Float> {
   AttributeRange$2(final float val$minValue, final float val$maxValue) {
      this.val$minValue = val$minValuex;
      this.val$maxValue = val$maxValuex;
   }

   public DataResult<Float> validate(final Float value) {
      return value >= this.val$minValue && value <= this.val$maxValue
         ? DataResult.success(value)
         : DataResult.error(() -> value + " is not in range [" + minValue + "; " + maxValue + "]");
   }

   public Float sanitize(final Float value) {
      return value >= this.val$minValue && value <= this.val$maxValue ? value : Mth.clamp(value, this.val$minValue, this.val$maxValue);
   }
}
