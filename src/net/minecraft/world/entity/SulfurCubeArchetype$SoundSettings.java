package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public record SulfurCubeArchetype$SoundSettings(
   Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float pushSoundImpulseThreshold, float pushSoundCooldown
) {
   public static final Codec<SulfurCubeArchetype$SoundSettings> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            SoundEvent.CODEC.fieldOf("hit_sound").forGetter(SulfurCubeArchetype$SoundSettings::hitSound),
            SoundEvent.CODEC.fieldOf("push_sound").forGetter(SulfurCubeArchetype$SoundSettings::pushSound),
            Codec.FLOAT.fieldOf("push_sound_impulse_threshold").forGetter(SulfurCubeArchetype$SoundSettings::pushSoundImpulseThreshold),
            Codec.FLOAT.fieldOf("push_sound_cooldown").forGetter(SulfurCubeArchetype$SoundSettings::pushSoundCooldown)
         )
         .apply(i, SulfurCubeArchetype$SoundSettings::new)
   );
}
