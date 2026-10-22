package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public record YCondition(VerticalAnchor anchor, int surfaceDepthMultiplier, boolean addStoneDepth) implements MaterialCondition {
   public static final MapCodec<YCondition> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            VerticalAnchor.CODEC.fieldOf("anchor").forGetter(YCondition::anchor),
            Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(YCondition::surfaceDepthMultiplier),
            Codec.BOOL.fieldOf("add_stone_depth").forGetter(YCondition::addStoneDepth)
         )
         .apply(i, YCondition::new)
   );

   @Override
   public MapCodec<YCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext ruleContext) {
      return new YCondition$1(this, ruleContext);
   }
}
