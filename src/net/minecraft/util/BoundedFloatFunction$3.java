package net.minecraft.util;

import java.util.Objects;
import java.util.function.Function;

class BoundedFloatFunction$3 implements BoundedFloatFunction<C2> {
   BoundedFloatFunction$3(final BoundedFloatFunction this$0, final BoundedFloatFunction val$outer, final Function val$function) {
      this.val$outer = val$outerx;
      this.val$function = val$functionx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public float apply(final C2 c2) {
      return this.val$outer.apply(this.val$function.apply(c2));
   }

   @Override
   public Interval range() {
      return this.val$outer.range();
   }
}
