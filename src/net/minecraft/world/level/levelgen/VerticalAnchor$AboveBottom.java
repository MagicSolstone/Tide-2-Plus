package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.dimension.DimensionType;

public record VerticalAnchor$AboveBottom(int offset) implements VerticalAnchor {
   public static final Codec<VerticalAnchor$AboveBottom> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y)
      .fieldOf("above_bottom")
      .xmap(VerticalAnchor$AboveBottom::new, VerticalAnchor$AboveBottom::offset)
      .codec();

   @Override
   public int resolveY(final WorldGenerationContext heightAccessor) {
      return heightAccessor.getMinGenY() + this.offset;
   }

   @Override
   public String toString() {
      return this.offset + " above bottom";
   }
}
