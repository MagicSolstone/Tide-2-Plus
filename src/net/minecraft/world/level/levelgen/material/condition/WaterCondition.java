package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public record WaterCondition(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) implements MaterialCondition {
   public static final MapCodec<WaterCondition> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.INT.fieldOf("offset").forGetter(WaterCondition::offset),
            Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(WaterCondition::surfaceDepthMultiplier),
            Codec.BOOL.fieldOf("add_stone_depth").forGetter(WaterCondition::addStoneDepth)
         )
         .apply(i, WaterCondition::new)
   );

   @Override
   public MapCodec<WaterCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext ruleContext) {
      return new WaterCondition$1(this, ruleContext);
   }
}
