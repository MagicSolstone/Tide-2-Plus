package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import org.jspecify.annotations.Nullable;

final class Removed {
   public static final Removed INSTANCE = new Removed();
   public static final Codec<Removed> CODEC = MapCodec.unitCodec(INSTANCE);

   private Removed() {
   }

   @Override
   public String toString() {
      return "<removed>";
   }

   public static boolean isNotRemoved(final Object value) {
      return value != INSTANCE;
   }

   public static boolean isRemoved(final Object value) {
      return value == INSTANCE;
   }

   public static Object nullToRemoved(@Nullable final Object value) {
      return value == null ? INSTANCE : value;
   }

   @Nullable
   public static Object removedToNull(final Object value) {
      return value == INSTANCE ? null : value;
   }
}
