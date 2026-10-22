package net.minecraft.core.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class TypedDataComponent$1 implements StreamCodec<RegistryFriendlyByteBuf, TypedDataComponent<?>> {
   public TypedDataComponent<?> decode(final RegistryFriendlyByteBuf input) {
      DataComponentType<?> type = DataComponentType.STREAM_CODEC.decode(input);
      return decodeTyped(input, type);
   }

   private static <T> TypedDataComponent<T> decodeTyped(final RegistryFriendlyByteBuf input, final DataComponentType<T> type) {
      return new TypedDataComponent<>(type, type.streamCodec().decode(input));
   }

   public void encode(final RegistryFriendlyByteBuf output, final TypedDataComponent<?> value) {
      encodeCap(output, value);
   }

   private static <T> void encodeCap(final RegistryFriendlyByteBuf output, final TypedDataComponent<T> component) {
      DataComponentType.STREAM_CODEC.encode(output, component.type());
      component.type().streamCodec().encode(output, component.value());
   }
}
