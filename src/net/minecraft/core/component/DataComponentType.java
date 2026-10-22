package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

public interface DataComponentType<T> {
   Codec<DataComponentType<?>> CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec());
   StreamCodec<RegistryFriendlyByteBuf, DataComponentType<?>> STREAM_CODEC = StreamCodec.recursive(c -> ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE));
   Codec<DataComponentType<?>> PERSISTENT_CODEC = CODEC.validate(
      type -> type.isTransient()
         ? DataResult.error(() -> "Encountered transient component " + BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type))
         : DataResult.success(type)
   );
   Codec<Map<DataComponentType<?>, Object>> VALUE_MAP_CODEC = Codec.dispatchedMap(PERSISTENT_CODEC, DataComponentType::codecOrThrow);

   static <T> DataComponentType$Builder<T> builder() {
      return new DataComponentType$Builder<>();
   }

   @Nullable
   Codec<T> codec();

   default Codec<T> codecOrThrow() {
      Codec<T> codec = this.codec();
      if (codec == null) {
         throw new IllegalStateException(this + " is not a persistent component");
      } else {
         return codec;
      }
   }

   default boolean isTransient() {
      return this.codec() == null;
   }

   boolean ignoreSwapAnimation();

   StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
}
