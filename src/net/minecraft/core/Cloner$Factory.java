package net.minecraft.core;

import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public class Cloner$Factory {
   private final Map<ResourceKey<? extends Registry<?>>, Cloner<?>> codecs = new HashMap<>();

   public <T> Cloner$Factory addCodec(final ResourceKey<? extends Registry<? extends T>> key, final Codec<T> codec) {
      this.codecs.put(key, new Cloner(codec));
      return this;
   }

   @Nullable
   public <T> Cloner<T> cloner(final ResourceKey<? extends Registry<? extends T>> key) {
      return (Cloner<T>)this.codecs.get(key);
   }
}
