package net.minecraft.server.jsonrpc.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record MethodInfo$Named<Params, Result>(Identifier name, MethodInfo<Params, Result> contents) {
   public static final Codec<MethodInfo$Named<?, ?>> CODEC = typedCodec();

   public static <Params, Result> Codec<MethodInfo$Named<Params, Result>> typedCodec() {
      return RecordCodecBuilder.create(
         i -> i.group(Identifier.CODEC.fieldOf("name").forGetter(MethodInfo$Named::name), MethodInfo.typedCodec().forGetter(MethodInfo$Named::contents))
            .apply(i, MethodInfo$Named::new)
      );
   }
}
