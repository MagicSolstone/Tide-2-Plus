package net.minecraft.client.resources.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class BiomeAmbientSoundsHandler$LoopSoundInstance extends AbstractTickableSoundInstance {
   private int fadeDirection;
   private int fade;

   public BiomeAmbientSoundsHandler$LoopSoundInstance(final SoundEvent soundEvent) {
      super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
      this.looping = true;
      this.delay = 0;
      this.volume = 1.0F;
      this.relative = true;
   }

   @Override
   public void tick() {
      if (this.fade < 0) {
         this.stop();
      }

      this.fade = this.fade + this.fadeDirection;
      this.volume = Mth.clamp(this.fade / 40.0F, 0.0F, 1.0F);
   }

   public void fadeOut() {
      this.fade = Math.min(this.fade, 40);
      this.fadeDirection = -1;
   }

   public void fadeIn() {
      this.fade = Math.max(0, this.fade);
      this.fadeDirection = 1;
   }
}
