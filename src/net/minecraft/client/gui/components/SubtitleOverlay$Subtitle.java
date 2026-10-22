package net.minecraft.client.gui.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class SubtitleOverlay$Subtitle {
   private final Component text;
   private final float range;
   private final List<SubtitleOverlay$SoundPlayedAt> playedAt = new ArrayList<>();
   private final boolean looping;

   public SubtitleOverlay$Subtitle(final Component text, final float range, final Vec3 location, final boolean looping) {
      this.text = text;
      this.range = range;
      this.looping = looping;
      this.playedAt.add(new SubtitleOverlay$SoundPlayedAt(location, Util.getMillis()));
   }

   private int getBrightness(final SubtitleOverlay$SoundPlayedAt closestRecentLocation, final double displayTimeMultiplier) {
      int displayFullBrightnessTime = this.looping ? 500 : 0;
      return Mth.floor(
         Mth.clampedLerp(
            (float)Math.max(0L, Util.getMillis() - closestRecentLocation.time - displayFullBrightnessTime)
               / (float)((3000L - displayFullBrightnessTime) * displayTimeMultiplier),
            255.0F,
            75.0F
         )
      );
   }

   public Component getText() {
      return this.text;
   }

   @Nullable
   public SubtitleOverlay$SoundPlayedAt getBestSubtitleCandidate(final Vec3 position) {
      if (this.playedAt.isEmpty()) {
         return null;
      } else {
         return this.playedAt.size() == 1
            ? this.playedAt.getFirst()
            : this.playedAt
               .stream()
               .filter(soundPlayedAt -> soundPlayedAt.location.closerThan(position, this.range))
               .max(Comparator.comparingLong(SubtitleOverlay$SoundPlayedAt::time).thenComparingDouble(sound -> sound.location.distanceTo(position)))
               .orElse(null);
      }
   }

   public void refresh(final Vec3 location) {
      this.playedAt.removeIf(soundPlayedAt -> location.equals(soundPlayedAt.location()));
      this.playedAt.add(new SubtitleOverlay$SoundPlayedAt(location, Util.getMillis()));
   }

   public boolean isAudibleFrom(final Vec3 camera) {
      if (Float.isInfinite(this.range)) {
         return true;
      } else if (this.playedAt.isEmpty()) {
         return false;
      } else {
         SubtitleOverlay$SoundPlayedAt closest = this.getBestSubtitleCandidate(camera);
         return closest == null ? false : camera.closerThan(closest.location, this.range);
      }
   }

   public void purgeOldInstances(final double maxAge) {
      long currentTime = Util.getMillis();
      this.playedAt.removeIf(soundPlayedAt -> currentTime - soundPlayedAt.time() > maxAge);
   }

   public boolean isStillActive() {
      return !this.playedAt.isEmpty();
   }
}
