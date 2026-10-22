package net.minecraft.world.entity.animal.chicken;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public record ChickenSoundVariant$ChickenSoundSet(
   Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound
) {
   private static final Codec<ChickenSoundVariant$ChickenSoundSet> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(ChickenSoundVariant$ChickenSoundSet::ambientSound),
            SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(ChickenSoundVariant$ChickenSoundSet::hurtSound),
            SoundEvent.CODEC.fieldOf("death_sound").forGetter(ChickenSoundVariant$ChickenSoundSet::deathSound),
            SoundEvent.CODEC.fieldOf("step_sound").forGetter(ChickenSoundVariant$ChickenSoundSet::stepSound)
         )
         .apply(i, ChickenSoundVariant$ChickenSoundSet::new)
   );
}
