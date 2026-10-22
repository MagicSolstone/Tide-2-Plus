package net.minecraft.world.item.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Enchantment$Cost(int base, int perLevelAboveFirst) {
   public static final Codec<Enchantment$Cost> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.INT.fieldOf("base").forGetter(Enchantment$Cost::base),
            Codec.INT.fieldOf("per_level_above_first").forGetter(Enchantment$Cost::perLevelAboveFirst)
         )
         .apply(i, Enchantment$Cost::new)
   );

   public int calculate(final int level) {
      return this.base + this.perLevelAboveFirst * (level - 1);
   }
}
