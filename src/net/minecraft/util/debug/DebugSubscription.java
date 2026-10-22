package net.minecraft.util.debug;

import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

public class DebugSubscription<T> {
   public static final int DOES_NOT_EXPIRE = 0;
   @Nullable
   private final StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec;
   private final int expireAfterTicks;

   public DebugSubscription(@Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec, final int expireAfterTicks) {
      this.valueStreamCodec = valueStreamCodec;
      this.expireAfterTicks = expireAfterTicks;
   }

   public DebugSubscription(@Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec) {
      this(valueStreamCodec, 0);
   }

   public DebugSubscription$Update<T> packUpdate(@Nullable final T value) {
      return new DebugSubscription$Update<>(this, Optional.ofNullable(value));
   }

   public DebugSubscription$Update<T> emptyUpdate() {
      return new DebugSubscription$Update<>(this, Optional.empty());
   }

   public DebugSubscription$Event<T> packEvent(final T value) {
      return new DebugSubscription$Event<>(this, value);
   }

   @Override
   public String toString() {
      return Util.getRegisteredName(BuiltInRegistries.DEBUG_SUBSCRIPTION, this);
   }

   @Nullable
   public StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec() {
      return this.valueStreamCodec;
   }

   public int expireAfterTicks() {
      return this.expireAfterTicks;
   }
}
