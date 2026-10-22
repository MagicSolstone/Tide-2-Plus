package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record HoverEvent$ShowText(Component value) implements HoverEvent {
   public static final MapCodec<HoverEvent$ShowText> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ComponentSerialization.CODEC.fieldOf("value").forGetter(HoverEvent$ShowText::value)).apply(i, HoverEvent$ShowText::new)
   );

   @Override
   public HoverEvent$Action action() {
      return HoverEvent$Action.SHOW_TEXT;
   }
}
