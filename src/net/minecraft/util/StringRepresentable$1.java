package net.minecraft.util;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import java.util.Arrays;
import java.util.stream.Stream;

class StringRepresentable$1 implements Keyable {
   StringRepresentable$1(final StringRepresentable[] val$values) {
      this.val$values = val$valuesx;
   }

   public <T> Stream<T> keys(final DynamicOps<T> ops) {
      return Arrays.stream(this.val$values).map(StringRepresentable::getSerializedName).map(ops::createString);
   }
}
