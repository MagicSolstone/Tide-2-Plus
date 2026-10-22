package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import java.util.Objects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

public class DataComponentType$Builder<T> {
   @Nullable
   private Codec<T> codec;
   @Nullable
   private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
   private boolean cacheEncoding;
   private boolean ignoreSwapAnimation;

   public DataComponentType$Builder<T> persistent(final Codec<T> codec) {
      this.codec = codec;
      return this;
   }

   public DataComponentType$Builder<T> networkSynchronized(final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
      this.streamCodec = streamCodec;
      return this;
   }

   public DataComponentType$Builder<T> cacheEncoding() {
      this.cacheEncoding = true;
      return this;
   }

   public DataComponentType<T> build() {
      StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec = Objects.requireNonNullElseGet(
         this.streamCodec, () -> ByteBufCodecs.fromCodecWithRegistries(Objects.requireNonNull(this.codec, "Missing Codec for component"))
      );
      Codec<T> cachingCodec = this.cacheEncoding && this.codec != null ? DataComponents.ENCODER_CACHE.wrap(this.codec) : this.codec;
      return new DataComponentType$Builder$SimpleType<>(cachingCodec, streamCodec, this.ignoreSwapAnimation);
   }

   public DataComponentType$Builder<T> ignoreSwapAnimation() {
      this.ignoreSwapAnimation = true;
      return this;
   }
}
