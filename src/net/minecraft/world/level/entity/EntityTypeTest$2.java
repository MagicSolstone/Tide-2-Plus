package net.minecraft.world.level.entity;

import org.jspecify.annotations.Nullable;

class EntityTypeTest$2 implements EntityTypeTest<B, T> {
   EntityTypeTest$2(final Class val$cls) {
      this.val$cls = val$clsx;
   }

   @Nullable
   @Override
   public T tryCast(final B entity) {
      return (T)(this.val$cls.equals(entity.getClass()) ? entity : null);
   }

   @Override
   public Class<? extends B> getBaseClass() {
      return this.val$cls;
   }
}
