package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.net.URI;
import net.minecraft.util.ExtraCodecs;

public record ClickEvent$OpenUrl(URI uri) implements ClickEvent {
   public static final MapCodec<ClickEvent$OpenUrl> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ExtraCodecs.UNTRUSTED_URI.fieldOf("url").forGetter(ClickEvent$OpenUrl::uri)).apply(i, ClickEvent$OpenUrl::new)
   );

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.OPEN_URL;
   }
}
