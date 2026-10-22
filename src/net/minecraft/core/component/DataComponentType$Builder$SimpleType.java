package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

class DataComponentType$Builder$SimpleType<T> implements DataComponentType<T> {
   @Nullable
   private final Codec<T> codec;
   private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
   private final boolean ignoreSwapAnimation;

   private DataComponentType$Builder$SimpleType(
      @Nullable final Codec<T> codec, final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, final boolean ignoreSwapAnimation
   ) {
      this.codec = codec;
      this.streamCodec = streamCodec;
      this.ignoreSwapAnimation = ignoreSwapAnimation;
   }

   @Override
   public boolean ignoreSwapAnimation() {
      return this.ignoreSwapAnimation;
   }

   @Nullable
   @Override
   public Codec<T> codec() {
      return this.codec;
   }

   @Override
   public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
      return this.streamCodec;
   }

   @Override
   public String toString() {
      return Util.getRegisteredName((Registry<DataComponentType$Builder$SimpleType<T>>)BuiltInRegistries.DATA_COMPONENT_TYPE, this);
   }
}
