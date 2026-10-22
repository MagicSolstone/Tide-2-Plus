package net.minecraft.core.registries;

import java.util.function.Supplier;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.Holder$Reference$Type;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

class PatchedRegistry$LazyHolder<T> extends Holder$Reference<T> {
   @Nullable
   private Supplier<T> supplier;

   protected PatchedRegistry$LazyHolder(final HolderOwner<T> owner, @Nullable final ResourceKey<T> key) {
      super(Holder$Reference$Type.STAND_ALONE, owner, key, null);
   }

   @Override
   protected void bindValue(final T value) {
      super.bindValue(value);
      this.supplier = null;
   }

   @Override
   public T value() {
      if (this.supplier != null) {
         this.bindValue(this.supplier.get());
      }

      return super.value();
   }
}
