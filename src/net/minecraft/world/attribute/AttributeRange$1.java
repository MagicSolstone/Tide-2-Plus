package net.minecraft.world.attribute;

import com.mojang.serialization.DataResult;

class AttributeRange$1 implements AttributeRange<Value> {
   @Override
   public DataResult<Value> validate(final Value value) {
      return DataResult.success(value);
   }

   @Override
   public Value sanitize(final Value value) {
      return value;
   }
}
