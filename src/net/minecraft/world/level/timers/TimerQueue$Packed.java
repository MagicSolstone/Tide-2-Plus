package net.minecraft.world.level.timers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record TimerQueue$Packed<T>(List<TimerQueue$Event$Packed<T>> events) {
   public static <T> Codec<TimerQueue$Packed<T>> codec(final Codec<TimerCallback<T>> callbackCodec) {
      return RecordCodecBuilder.create(
         i -> i.group(TimerQueue$Event$Packed.codec(callbackCodec).listOf().fieldOf("events").forGetter(TimerQueue$Packed::events))
            .apply(i, TimerQueue$Packed::new)
      );
   }
}
