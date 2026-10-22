package net.minecraft.world.entity.animal.feline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public record CatSoundVariant$CatSoundSet(
   Holder<SoundEvent> ambientSound,
   Holder<SoundEvent> strayAmbientSound,
   Holder<SoundEvent> hissSound,
   Holder<SoundEvent> hurtSound,
   Holder<SoundEvent> deathSound,
   Holder<SoundEvent> eatSound,
   Holder<SoundEvent> begForFoodSound,
   Holder<SoundEvent> purrSound,
   Holder<SoundEvent> purreowSound
) {
   private static final Codec<CatSoundVariant$CatSoundSet> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(CatSoundVariant$CatSoundSet::ambientSound),
            SoundEvent.CODEC.fieldOf("stray_ambient_sound").forGetter(CatSoundVariant$CatSoundSet::strayAmbientSound),
            SoundEvent.CODEC.fieldOf("hiss_sound").forGetter(CatSoundVariant$CatSoundSet::hissSound),
            SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(CatSoundVariant$CatSoundSet::hurtSound),
            SoundEvent.CODEC.fieldOf("death_sound").forGetter(CatSoundVariant$CatSoundSet::deathSound),
            SoundEvent.CODEC.fieldOf("eat_sound").forGetter(CatSoundVariant$CatSoundSet::eatSound),
            SoundEvent.CODEC.fieldOf("beg_for_food_sound").forGetter(CatSoundVariant$CatSoundSet::begForFoodSound),
            SoundEvent.CODEC.fieldOf("purr_sound").forGetter(CatSoundVariant$CatSoundSet::purrSound),
            SoundEvent.CODEC.fieldOf("purreow_sound").forGetter(CatSoundVariant$CatSoundSet::purreowSound)
         )
         .apply(i, CatSoundVariant$CatSoundSet::new)
   );
}
