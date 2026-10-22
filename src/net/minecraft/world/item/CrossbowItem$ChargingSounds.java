package net.minecraft.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public record CrossbowItem$ChargingSounds(Optional<Holder<SoundEvent>> start, Optional<Holder<SoundEvent>> mid, Optional<Holder<SoundEvent>> end) {
   public static final Codec<CrossbowItem$ChargingSounds> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            SoundEvent.CODEC.optionalFieldOf("start").forGetter(CrossbowItem$ChargingSounds::start),
            SoundEvent.CODEC.optionalFieldOf("mid").forGetter(CrossbowItem$ChargingSounds::mid),
            SoundEvent.CODEC.optionalFieldOf("end").forGetter(CrossbowItem$ChargingSounds::end)
         )
         .apply(i, CrossbowItem$ChargingSounds::new)
   );
}
