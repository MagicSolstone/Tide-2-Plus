package net.minecraft.world.attribute;

import com.mojang.serialization.DataResult;

public interface AttributeRange<Value> {
   AttributeRange<Float> UNIT_FLOAT = ofFloat(0.0F, 1.0F);
   AttributeRange<Float> UNIT_FLOAT_EPSILON = ofFloat(0.0F, 0.9999999F);
   AttributeRange<Float> NON_NEGATIVE_FLOAT = ofFloat(0.0F, Float.POSITIVE_INFINITY);

   static <Value> AttributeRange<Value> any() {
      return new AttributeRange$1();
   }

   static AttributeRange<Float> ofFloat(final float minValue, final float maxValue) {
      return new AttributeRange$2(minValue, maxValue);
   }

   DataResult<Value> validate(Value value);

   Value sanitize(Value value);
}
