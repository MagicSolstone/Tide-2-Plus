package net.minecraft.tags;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

class TagLoader$1 implements TagEntry$Lookup<T> {
   TagLoader$1(final TagLoader this$0, final Map val$newTags) {
      this.val$newTags = val$newTagsx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Nullable
   @Override
   public T element(final Identifier key, final boolean required) {
      return (T)this.this$0.elementLookup.get(key, required).orElse(null);
   }

   @Nullable
   @Override
   public Collection<T> tag(final Identifier key) {
      return (Collection<T>)this.val$newTags.get(key);
   }
}
