package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.dimension.DimensionType;

public record VerticalAnchor$RelativeToSeaLevel(int offset) implements VerticalAnchor {
   public static final Codec<VerticalAnchor$RelativeToSeaLevel> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y)
      .fieldOf("relative_to_sea_level")
      .xmap(VerticalAnchor$RelativeToSeaLevel::new, VerticalAnchor$RelativeToSeaLevel::offset)
      .codec();

   @Override
   public int resolveY(final WorldGenerationContext heightAccessor) {
      return heightAccessor.seaLevel() + this.offset;
   }

   @Override
   public String toString() {
      return this.offset + " relative to sea level";
   }
}
