package net.minecraft.network.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.Utf8String;
import net.minecraft.util.LenientJsonParser;

class ByteBufCodecs$37 implements StreamCodec<ByteBuf, JsonElement> {
   private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

   ByteBufCodecs$37(final int val$maxStringLength) {
      this.val$maxStringLength = val$maxStringLengthx;
   }

   public JsonElement decode(final ByteBuf input) {
      String payload = Utf8String.read(input, this.val$maxStringLength);

      try {
         return LenientJsonParser.parse(payload);
      } catch (JsonSyntaxException var4) {
         throw new DecoderException("Failed to parse JSON", var4);
      }
   }

   public void encode(final ByteBuf output, final JsonElement value) {
      String payload = GSON.toJson(value);
      Utf8String.write(output, payload, this.val$maxStringLength);
   }
}
