package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ClickEvent$CopyToClipboard(String value) implements ClickEvent {
   public static final MapCodec<ClickEvent$CopyToClipboard> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.STRING.fieldOf("value").forGetter(ClickEvent$CopyToClipboard::value)).apply(i, ClickEvent$CopyToClipboard::new)
   );

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.COPY_TO_CLIPBOARD;
   }
}
