package net.minecraft.core;

import java.util.List;
import net.minecraft.tags.TagKey;

class HolderSet$1 extends HolderSet$Named<T> {
   HolderSet$1(final HolderOwner owner, final TagKey key) {
      super(owner, key);
   }

   @Override
   protected List<Holder<T>> contents() {
      throw new UnsupportedOperationException("Tag " + this.key() + " can't be dereferenced during construction");
   }
}
