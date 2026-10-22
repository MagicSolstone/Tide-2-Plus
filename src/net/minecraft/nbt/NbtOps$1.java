package net.minecraft.nbt;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapLike;
import java.util.Objects;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

class NbtOps$1 implements MapLike<Tag> {
   NbtOps$1(final NbtOps this$0, final CompoundTag val$tag) {
      this.val$tag = val$tagx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Nullable
   public Tag get(final Tag key) {
      if (key instanceof StringTag(String var4)) {
         return this.val$tag.get(var4);
      } else {
         throw new UnsupportedOperationException("Cannot get map entry with non-string key: " + key);
      }
   }

   @Nullable
   public Tag get(final String key) {
      return this.val$tag.get(key);
   }

   public Stream<Pair<Tag, Tag>> entries() {
      return this.val$tag.entrySet().stream().map(entry -> Pair.of(this.this$0.createString(entry.getKey()), entry.getValue()));
   }

   @Override
   public String toString() {
      return "MapLike[" + this.val$tag + "]";
   }
}
