package net.minecraft.world.level.entity;

import org.jspecify.annotations.Nullable;

public interface EntityTypeTest<B, T extends B> {
   static <B, T extends B> EntityTypeTest<B, T> forClass(final Class<T> cls) {
      return new EntityTypeTest$1(cls);
   }

   static <B, T extends B> EntityTypeTest<B, T> forExactClass(final Class<T> cls) {
      return new EntityTypeTest$2(cls);
   }

   @Nullable
   T tryCast(B entity);

   Class<? extends B> getBaseClass();
}
