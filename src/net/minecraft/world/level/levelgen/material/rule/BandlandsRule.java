package net.minecraft.world.level.levelgen.material.rule;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public enum BandlandsRule implements MaterialRule {
   INSTANCE;

   public static final MapCodec<BandlandsRule> CODEC = MapCodec.unit(INSTANCE);

   @Override
   public MapCodec<BandlandsRule> codec() {
      return CODEC;
   }

   @Override
   public RuleEvaluator compile(final MaterialRuleContext context) {
      return context::getBand;
   }
}
