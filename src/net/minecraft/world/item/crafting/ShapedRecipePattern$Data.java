package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs;

public record ShapedRecipePattern$Data(Map<Character, Ingredient> key, List<String> pattern) {
   private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(strings -> {
      if (strings.size() > 3) {
         return DataResult.error(() -> "Invalid pattern: too many rows, 3 is maximum");
      } else if (strings.isEmpty()) {
         return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
      } else {
         int firstLength = ((String)strings.getFirst()).length();

         for (String line : strings) {
            if (line.length() > 3) {
               return DataResult.error(() -> "Invalid pattern: too many columns, 3 is maximum");
            }

            if (firstLength != line.length()) {
               return DataResult.error(() -> "Invalid pattern: each row must be the same width");
            }
         }

         return DataResult.success(strings);
      }
   }, Function.identity());
   private static final Codec<Character> SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> {
      if (symbol.length() != 1) {
         return DataResult.error(() -> "Invalid key entry: '" + symbol + "' is an invalid symbol (must be 1 character only).");
      } else {
         return " ".equals(symbol) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(symbol.charAt(0));
      }
   }, String::valueOf);
   public static final MapCodec<ShapedRecipePattern$Data> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC).fieldOf("key").forGetter(d -> d.key),
            PATTERN_CODEC.fieldOf("pattern").forGetter(d -> d.pattern)
         )
         .apply(i, ShapedRecipePattern$Data::new)
   );
}
