package net.minecraft.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;

public class KeyframeTrack$Builder<T> {
   private final Builder<Keyframe<T>> keyframes = ImmutableList.builder();
   private EasingType easing = EasingType.LINEAR;

   public KeyframeTrack$Builder<T> addKeyframe(final int ticks, final T value) {
      this.keyframes.add(new Keyframe(ticks, value));
      return this;
   }

   public KeyframeTrack$Builder<T> setEasing(final EasingType easing) {
      this.easing = easing;
      return this;
   }

   public KeyframeTrack<T> build() {
      List<Keyframe<T>> keyframes = (List<Keyframe<T>>)KeyframeTrack.validateKeyframes(this.keyframes.build()).getOrThrow();
      return new KeyframeTrack<>(keyframes, this.easing);
   }
}
