package net.minecraft.client.resources.sounds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public abstract class AbstractTickableSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
   private boolean stopped;

   protected AbstractTickableSoundInstance(final SoundEvent event, final SoundSource source, final RandomSource random) {
      super(event, source, random);
   }

   @Override
   public boolean isStopped() {
      return this.stopped;
   }

   protected final void stop() {
      this.stopped = true;
      this.looping = false;
   }
}
