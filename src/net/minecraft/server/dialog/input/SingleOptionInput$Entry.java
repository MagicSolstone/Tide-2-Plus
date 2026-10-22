package net.minecraft.server.dialog.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record SingleOptionInput$Entry(String id, Optional<Component> display, boolean initial) {
   public static final Codec<SingleOptionInput$Entry> FULL_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("id").forGetter(SingleOptionInput$Entry::id),
            ComponentSerialization.CODEC.optionalFieldOf("display").forGetter(SingleOptionInput$Entry::display),
            Codec.BOOL.optionalFieldOf("initial", false).forGetter(SingleOptionInput$Entry::initial)
         )
         .apply(i, SingleOptionInput$Entry::new)
   );
   public static final Codec<SingleOptionInput$Entry> CODEC = Codec.withAlternative(
      FULL_CODEC, Codec.STRING, id -> new SingleOptionInput$Entry(id, Optional.empty(), false)
   );

   public Component displayOrDefault() {
      return this.display.orElseGet(() -> Component.literal(this.id));
   }
}
