package net.minecraft.core.component.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;

public final class DataComponentPredicate$AnyValueType extends DataComponentPredicate$TypeBase<AnyValue> {
   private final AnyValue predicate;

   public DataComponentPredicate$AnyValueType(final AnyValue predicate) {
      super(MapCodec.unitCodec(predicate));
      this.predicate = predicate;
   }

   public AnyValue predicate() {
      return this.predicate;
   }

   public DataComponentType<?> componentType() {
      return this.predicate.type();
   }

   public static DataComponentPredicate$AnyValueType create(final DataComponentType<?> componentType) {
      return new DataComponentPredicate$AnyValueType(new AnyValue(componentType));
   }
}
