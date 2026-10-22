package net.minecraft.util;

import com.google.common.collect.Comparators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.attribute.LerpFunction;

public record KeyframeTrack<T>(List<Keyframe<T>> keyframes, EasingType easingType) {
   public KeyframeTrack(List<Keyframe<T>> keyframes, EasingType easingType) {
      if (keyframes.isEmpty()) {
         throw new IllegalArgumentException("Track has no keyframes");
      } else {
         this.keyframes = keyframes;
         this.easingType = easingType;
      }
   }

   public static <T> MapCodec<KeyframeTrack<T>> mapCodec(final Codec<T> valueCodec) {
      Codec<List<Keyframe<T>>> keyframesCodec = Keyframe.codec(valueCodec).listOf().validate(KeyframeTrack::validateKeyframes);
      return RecordCodecBuilder.mapCodec(
         i -> i.group(
               keyframesCodec.fieldOf("keyframes").forGetter(KeyframeTrack::keyframes),
               EasingType.CODEC.optionalFieldOf("ease", EasingType.LINEAR).forGetter(KeyframeTrack::easingType)
            )
            .apply(i, KeyframeTrack::new)
      );
   }

   private static <T> DataResult<List<Keyframe<T>>> validateKeyframes(final List<Keyframe<T>> keyframes) {
      if (keyframes.isEmpty()) {
         return DataResult.error(() -> "Keyframes must not be empty");
      } else if (!Comparators.isInOrder(keyframes, Comparator.comparingInt(Keyframe::ticks))) {
         return DataResult.error(() -> "Keyframes must be ordered by ticks field");
      } else {
         if (keyframes.size() > 1) {
            int repeatCount = 0;
            int lastTicks = keyframes.getLast().ticks();

            for (Keyframe<T> keyframe : keyframes) {
               if (keyframe.ticks() == lastTicks) {
                  if (++repeatCount > 2) {
                     return DataResult.error(() -> "More than 2 keyframes on same tick: " + keyframe.ticks());
                  }
               } else {
                  repeatCount = 0;
               }

               lastTicks = keyframe.ticks();
            }
         }

         return DataResult.success(keyframes);
      }
   }

   public static DataResult<KeyframeTrack<?>> validatePeriod(final KeyframeTrack<?> track, final int periodTicks) {
      for (Keyframe<?> keyframe : track.keyframes()) {
         int tick = keyframe.ticks();
         if (tick < 0 || tick > periodTicks) {
            return DataResult.error(() -> "Keyframe at tick " + keyframe.ticks() + " must be in range [0; " + periodTicks + "]");
         }
      }

      return DataResult.success(track);
   }

   public KeyframeTrackSampler<T> bakeSampler(final Optional<Integer> periodTicks, final LerpFunction<T> lerp) {
      return new KeyframeTrackSampler<>(this, periodTicks, lerp);
   }
}
