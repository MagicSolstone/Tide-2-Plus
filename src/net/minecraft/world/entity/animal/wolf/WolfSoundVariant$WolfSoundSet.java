package net.minecraft.world.entity.animal.wolf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public record WolfSoundVariant$WolfSoundSet(
   Holder<SoundEvent> ambientSound,
   Holder<SoundEvent> deathSound,
   Holder<SoundEvent> growlSound,
   Holder<SoundEvent> hurtSound,
   Holder<SoundEvent> pantSound,
   Holder<SoundEvent> whineSound,
   Holder<SoundEvent> stepSound
) {
   public static final Codec<WolfSoundVariant$WolfSoundSet> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(WolfSoundVariant$WolfSoundSet::ambientSound),
            SoundEvent.CODEC.fieldOf("death_sound").forGetter(WolfSoundVariant$WolfSoundSet::deathSound),
            SoundEvent.CODEC.fieldOf("growl_sound").forGetter(WolfSoundVariant$WolfSoundSet::growlSound),
            SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(WolfSoundVariant$WolfSoundSet::hurtSound),
            SoundEvent.CODEC.fieldOf("pant_sound").forGetter(WolfSoundVariant$WolfSoundSet::pantSound),
            SoundEvent.CODEC.fieldOf("whine_sound").forGetter(WolfSoundVariant$WolfSoundSet::whineSound),
            SoundEvent.CODEC.fieldOf("step_sound").forGetter(WolfSoundVariant$WolfSoundSet::stepSound)
         )
         .apply(i, WolfSoundVariant$WolfSoundSet::new)
   );
}
