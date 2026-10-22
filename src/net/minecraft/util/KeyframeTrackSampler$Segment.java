package net.minecraft.util;

record KeyframeTrackSampler$Segment<T>(EasingType easing, T fromValue, int fromTicks, T toValue, int toTicks) {
   public KeyframeTrackSampler$Segment(final KeyframeTrack<T> track, final Keyframe<T> from, final int fromTicks, final Keyframe<T> to, final int toTicks) {
      this(track.easingType(), from.value(), fromTicks, to.value(), toTicks);
   }
}
