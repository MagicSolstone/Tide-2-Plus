package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.dimension.DimensionType;

public record VerticalAnchor$BelowTop(int offset) implements VerticalAnchor {
   public static final Codec<VerticalAnchor$BelowTop> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y)
      .fieldOf("below_top")
      .xmap(VerticalAnchor$BelowTop::new, VerticalAnchor$BelowTop::offset)
      .codec();

   @Override
   public int resolveY(final WorldGenerationContext heightAccessor) {
      return heightAccessor.getGenDepth() - 1 + heightAccessor.getMinGenY() - this.offset;
   }

   @Override
   public String toString() {
      return this.offset + " below top";
   }
}
