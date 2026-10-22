package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.dimension.DimensionType;

public record VerticalAnchor$Absolute(int y) implements VerticalAnchor {
   public static final Codec<VerticalAnchor$Absolute> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y)
      .fieldOf("absolute")
      .xmap(VerticalAnchor$Absolute::new, VerticalAnchor$Absolute::y)
      .codec();

   @Override
   public int resolveY(final WorldGenerationContext heightAccessor) {
      return this.y;
   }

   @Override
   public String toString() {
      return this.y + " absolute";
   }
}
