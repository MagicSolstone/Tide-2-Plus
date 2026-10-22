package net.minecraft.util.debug;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DebugSubscription$Update<T>(DebugSubscription<T> subscription, Optional<T> value) {
   public static final StreamCodec<RegistryFriendlyByteBuf, DebugSubscription$Update<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION)
      .dispatch(DebugSubscription$Update::subscription, DebugSubscription$Update::streamCodec);

   private static <T> StreamCodec<? super RegistryFriendlyByteBuf, DebugSubscription$Update<T>> streamCodec(final DebugSubscription<T> subscription) {
      return ByteBufCodecs.optional(Objects.requireNonNull(subscription.valueStreamCodec))
         .map(value -> new DebugSubscription$Update<>(subscription, (Optional<T>)value), DebugSubscription$Update::value);
   }
}
