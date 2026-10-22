package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;

public final class DataComponentPredicate$ConcreteType<T extends DataComponentPredicate> extends DataComponentPredicate$TypeBase<T> {
   public DataComponentPredicate$ConcreteType(final Codec<T> codec) {
      super(codec);
   }
}
