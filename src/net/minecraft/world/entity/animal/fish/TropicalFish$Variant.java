package net.minecraft.world.entity.animal.fish;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.DyeColor;

public record TropicalFish$Variant(TropicalFish$Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
   public static final Codec<TropicalFish$Variant> CODEC = Codec.INT.xmap(TropicalFish$Variant::new, TropicalFish$Variant::getPackedId);

   public TropicalFish$Variant(final int packedId) {
      this(TropicalFish.getPattern(packedId), TropicalFish.getBaseColor(packedId), TropicalFish.getPatternColor(packedId));
   }

   public int getPackedId() {
      return TropicalFish.packVariant(this.pattern, this.baseColor, this.patternColor);
   }
}
