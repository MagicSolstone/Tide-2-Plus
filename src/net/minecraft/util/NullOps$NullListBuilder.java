package net.minecraft.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

final class NullOps$NullListBuilder extends AbstractListBuilder<Unit, Unit> {
   public NullOps$NullListBuilder(final DynamicOps<Unit> ops) {
      super(ops);
   }

   protected Unit initBuilder() {
      return Unit.INSTANCE;
   }

   protected Unit append(final Unit builder, final Unit value) {
      return builder;
   }

   protected DataResult<Unit> build(final Unit builder, final Unit prefix) {
      return DataResult.success(builder);
   }
}
