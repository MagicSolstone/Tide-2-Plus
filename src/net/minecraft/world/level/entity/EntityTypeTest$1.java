package net.minecraft.world.level.entity;

import org.jspecify.annotations.Nullable;

class EntityTypeTest$1 implements EntityTypeTest<B, T> {
   EntityTypeTest$1(final Class val$cls) {
      this.val$cls = val$clsx;
   }

   @Nullable
   @Override
   public T tryCast(final B entity) {
      return (T)(this.val$cls.isInstance(entity) ? entity : null);
   }

   @Override
   public Class<? extends B> getBaseClass() {
      return this.val$cls;
   }
}
