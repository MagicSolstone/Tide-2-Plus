package net.minecraft.client.gui.components.toasts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

@Environment(EnvType.CLIENT)
public enum Toast$Visibility {
   SHOW(SoundEvents.UI_TOAST_IN),
   HIDE(SoundEvents.UI_TOAST_OUT);

   private final SoundEvent soundEvent;

   private Toast$Visibility(final SoundEvent soundEvent) {
      this.soundEvent = soundEvent;
   }

   public void playSound(final SoundManager manager) {
      manager.play(SimpleSoundInstance.forUI(this.soundEvent, 1.0F, 1.0F));
   }
}
