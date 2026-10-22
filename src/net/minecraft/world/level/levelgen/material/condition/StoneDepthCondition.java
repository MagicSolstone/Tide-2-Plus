package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public record StoneDepthCondition(int offset, boolean addSurfaceDepth, int secondaryDepthRange, CaveSurface surfaceType) implements MaterialCondition {
   public static final MapCodec<StoneDepthCondition> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.INT.fieldOf("offset").forGetter(StoneDepthCondition::offset),
            Codec.BOOL.fieldOf("add_surface_depth").forGetter(StoneDepthCondition::addSurfaceDepth),
            Codec.INT.fieldOf("secondary_depth_range").forGetter(StoneDepthCondition::secondaryDepthRange),
            CaveSurface.CODEC.fieldOf("surface_type").forGetter(StoneDepthCondition::surfaceType)
         )
         .apply(i, StoneDepthCondition::new)
   );

   @Override
   public MapCodec<StoneDepthCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext ruleContext) {
      boolean ceiling = this.surfaceType == CaveSurface.CEILING;
      return new StoneDepthCondition$1(this, ruleContext, ceiling);
   }
}
