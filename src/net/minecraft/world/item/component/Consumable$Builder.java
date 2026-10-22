package net.minecraft.world.item.component;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;

public class Consumable$Builder {
   private float consumeSeconds = 1.6F;
   private ItemUseAnimation animation = ItemUseAnimation.EAT;
   private Holder<SoundEvent> sound = SoundEvents.GENERIC_EAT;
   private boolean hasConsumeParticles = true;
   private final List<ConsumeEffect> onConsumeEffects = new ArrayList<>();

   private Consumable$Builder() {
   }

   public Consumable$Builder consumeSeconds(final float consumeSeconds) {
      this.consumeSeconds = consumeSeconds;
      return this;
   }

   public Consumable$Builder animation(final ItemUseAnimation animation) {
      this.animation = animation;
      return this;
   }

   public Consumable$Builder sound(final Holder<SoundEvent> sound) {
      this.sound = sound;
      return this;
   }

   public Consumable$Builder soundAfterConsume(final Holder<SoundEvent> soundAfterConsume) {
      return this.onConsume(new PlaySoundConsumeEffect(soundAfterConsume));
   }

   public Consumable$Builder hasConsumeParticles(final boolean hasConsumeParticles) {
      this.hasConsumeParticles = hasConsumeParticles;
      return this;
   }

   public Consumable$Builder onConsume(final ConsumeEffect effect) {
      this.onConsumeEffects.add(effect);
      return this;
   }

   public Consumable build() {
      return new Consumable(this.consumeSeconds, this.animation, this.sound, this.hasConsumeParticles, this.onConsumeEffects);
   }
}
