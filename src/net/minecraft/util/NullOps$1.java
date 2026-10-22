package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapLike;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

class NullOps$1 implements MapLike<Unit> {
   @Nullable
   public Unit get(final Unit key) {
      return null;
   }

   @Nullable
   public Unit get(final String key) {
      return null;
   }

   public Stream<Pair<Unit, Unit>> entries() {
      return Stream.empty();
   }
}
