package net.minecraft.world.entity.animal.pig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public record PigSoundVariant$PigSoundSet(
   Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound, Holder<SoundEvent> eatSound
) {
   public static final Codec<PigSoundVariant$PigSoundSet> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(PigSoundVariant$PigSoundSet::ambientSound),
            SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(PigSoundVariant$PigSoundSet::hurtSound),
            SoundEvent.CODEC.fieldOf("death_sound").forGetter(PigSoundVariant$PigSoundSet::deathSound),
            SoundEvent.CODEC.fieldOf("step_sound").forGetter(PigSoundVariant$PigSoundSet::stepSound),
            SoundEvent.CODEC.fieldOf("eat_sound").forGetter(PigSoundVariant$PigSoundSet::eatSound)
         )
         .apply(i, PigSoundVariant$PigSoundSet::new)
   );
}
