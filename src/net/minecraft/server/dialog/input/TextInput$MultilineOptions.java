package net.minecraft.server.dialog.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.util.ExtraCodecs;

public record TextInput$MultilineOptions(Optional<Integer> maxLines, Optional<Integer> height) {
   public static final int MAX_HEIGHT = 512;
   public static final Codec<TextInput$MultilineOptions> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_lines").forGetter(TextInput$MultilineOptions::maxLines),
            ExtraCodecs.intRange(1, 512).optionalFieldOf("height").forGetter(TextInput$MultilineOptions::height)
         )
         .apply(i, TextInput$MultilineOptions::new)
   );
}
