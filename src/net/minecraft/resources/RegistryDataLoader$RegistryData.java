package net.minecraft.resources;

import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.Registry;

public record RegistryDataLoader$RegistryData<T>(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec, RegistryValidator<T> validator) {
   private RegistryDataLoader$RegistryData(final ResourceKey<? extends Registry<T>> key, final Codec<T> elementCodec) {
      this(key, elementCodec, RegistryValidator.none());
   }

   public void runWithArguments(final BiConsumer<ResourceKey<? extends Registry<T>>, Codec<T>> output) {
      output.accept(this.key, this.elementCodec);
   }
}
