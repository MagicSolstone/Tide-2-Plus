package net.minecraft.world.level.timers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TimerQueue$Event$Packed<T>(long triggerTime, String id, TimerCallback<T> callback) {
   public static <T> Codec<TimerQueue$Event$Packed<T>> codec(final Codec<TimerCallback<T>> callbackCodec) {
      return RecordCodecBuilder.create(
         i -> i.group(
               Codec.LONG.fieldOf("trigger_time").forGetter(TimerQueue$Event$Packed::triggerTime),
               Codec.STRING.fieldOf("id").forGetter(TimerQueue$Event$Packed::id),
               callbackCodec.fieldOf("callback").forGetter(TimerQueue$Event$Packed::callback)
            )
            .apply(i, TimerQueue$Event$Packed::new)
      );
   }
}
