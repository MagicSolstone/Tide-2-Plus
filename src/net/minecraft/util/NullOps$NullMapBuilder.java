package net.minecraft.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.RecordBuilder.AbstractUniversalBuilder;

final class NullOps$NullMapBuilder extends AbstractUniversalBuilder<Unit, Unit> {
   public NullOps$NullMapBuilder(final DynamicOps<Unit> ops) {
      super(ops);
   }

   protected Unit initBuilder() {
      return Unit.INSTANCE;
   }

   protected Unit append(final Unit key, final Unit value, final Unit builder) {
      return builder;
   }

   protected DataResult<Unit> build(final Unit builder, final Unit prefix) {
      return DataResult.success(prefix);
   }
}
