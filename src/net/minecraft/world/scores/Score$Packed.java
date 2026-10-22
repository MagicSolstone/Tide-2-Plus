package net.minecraft.world.scores;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.chat.numbers.NumberFormatTypes;

public record Score$Packed(int value, boolean locked, Optional<Component> display, Optional<NumberFormat> numberFormat) {
   public static final MapCodec<Score$Packed> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.INT.optionalFieldOf("Score", 0).forGetter(Score$Packed::value),
            Codec.BOOL.optionalFieldOf("Locked", false).forGetter(Score$Packed::locked),
            ComponentSerialization.CODEC.optionalFieldOf("display").forGetter(Score$Packed::display),
            NumberFormatTypes.CODEC.optionalFieldOf("format").forGetter(Score$Packed::numberFormat)
         )
         .apply(i, Score$Packed::new)
   );
}
