package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public record VerticalGradientCondition(Identifier randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) implements MaterialCondition {
   public static final MapCodec<VerticalGradientCondition> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("random_name").forGetter(VerticalGradientCondition::randomName),
            VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(VerticalGradientCondition::trueAtAndBelow),
            VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(VerticalGradientCondition::falseAtAndAbove)
         )
         .apply(i, VerticalGradientCondition::new)
   );

   @Override
   public MapCodec<VerticalGradientCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext ruleContext) {
      int trueAtAndBelow = ruleContext.resolveAnchorY(this.trueAtAndBelow);
      int falseAtAndAbove = ruleContext.resolveAnchorY(this.falseAtAndAbove);
      PositionalRandomFactory randomFactory = ruleContext.getOrCreateRandomFactory(this.randomName);
      return new VerticalGradientCondition$1(this, ruleContext, trueAtAndBelow, falseAtAndAbove, randomFactory);
   }
}
