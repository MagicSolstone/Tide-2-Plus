package net.minecraft.network.chat.contents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.ComponentContents;

public interface PlainTextContents extends ComponentContents {
   MapCodec<PlainTextContents> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.STRING.fieldOf("text").forGetter(PlainTextContents::text)).apply(i, PlainTextContents::create)
   );
   PlainTextContents EMPTY = new PlainTextContents$1();

   static PlainTextContents create(final String text) {
      return (PlainTextContents)(text.isEmpty() ? EMPTY : new PlainTextContents$LiteralContents(text));
   }

   String text();

   @Override
   default MapCodec<PlainTextContents> codec() {
      return MAP_CODEC;
   }
}
