package net.minecraft.client.sounds;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.Sound$Type;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.MultipliedFloats;

@Environment(EnvType.CLIENT)
class SoundManager$Preparations$1 implements Weighted<Sound> {
   SoundManager$Preparations$1(final SoundManager$Preparations this$0, final Identifier val$soundLocation, final Sound val$sound) {
      this.val$soundLocation = val$soundLocationx;
      this.val$sound = val$soundx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int getWeight() {
      WeighedSoundEvents registration = this.this$0.registry.get(this.val$soundLocation);
      return registration == null ? 0 : registration.getWeight();
   }

   public Sound getSound(final RandomSource random) {
      WeighedSoundEvents registration = this.this$0.registry.get(this.val$soundLocation);
      if (registration == null) {
         return SoundManager.EMPTY_SOUND;
      } else {
         Sound wrappedSound = registration.getSound(random);
         return new Sound(
            wrappedSound.getLocation(),
            new MultipliedFloats(wrappedSound.getVolume(), this.val$sound.getVolume()),
            new MultipliedFloats(wrappedSound.getPitch(), this.val$sound.getPitch()),
            this.val$sound.getWeight(),
            Sound$Type.FILE,
            wrappedSound.shouldStream() || this.val$sound.shouldStream(),
            wrappedSound.shouldPreload(),
            wrappedSound.getAttenuationDistance()
         );
      }
   }

   @Override
   public void preloadIfRequired(final SoundEngine soundEngine) {
      WeighedSoundEvents registration = this.this$0.registry.get(this.val$soundLocation);
      if (registration != null) {
         registration.preloadIfRequired(soundEngine);
      }
   }
}
