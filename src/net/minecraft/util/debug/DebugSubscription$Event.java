package net.minecraft.util.debug;

import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DebugSubscription$Event<T>(DebugSubscription<T> subscription, T value) {
   public static final StreamCodec<RegistryFriendlyByteBuf, DebugSubscription$Event<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION)
      .dispatch(DebugSubscription$Event::subscription, DebugSubscription$Event::streamCodec);

   private static <T> StreamCodec<? super RegistryFriendlyByteBuf, DebugSubscription$Event<T>> streamCodec(final DebugSubscription<T> subscription) {
      return Objects.requireNonNull(subscription.valueStreamCodec)
         .map(value -> new DebugSubscription$Event<>(subscription, (T)value), DebugSubscription$Event::value);
   }
}
