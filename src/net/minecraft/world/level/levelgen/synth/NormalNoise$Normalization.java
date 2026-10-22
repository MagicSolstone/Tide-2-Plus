package net.minecraft.world.level.levelgen.synth;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public enum NormalNoise$Normalization {
   DISABLED,
   ENABLED,
   @Deprecated
   LEGACY;

   public static final Codec<NormalNoise$Normalization> CODEC = Codec.either(
         Codec.BOOL,
         Codec.STRING
            .validate(string -> string.equals("legacy") ? DataResult.success(string) : DataResult.error(() -> "Invalid normalization type: " + string))
      )
      .xmap(either -> (NormalNoise$Normalization)either.map(enabled -> enabled ? ENABLED : DISABLED, var0x -> LEGACY), normalization -> {
         return switch (normalization) {
            case DISABLED -> Either.left(false);
            case ENABLED -> Either.left(true);
            case LEGACY -> Either.right("legacy");
         };
      });
}
