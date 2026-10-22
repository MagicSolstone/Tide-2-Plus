package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public enum HoleCondition implements MaterialCondition {
   INSTANCE;

   public static final MapCodec<HoleCondition> CODEC = MapCodec.unit(INSTANCE);

   @Override
   public MapCodec<HoleCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext context) {
      return new HoleCondition$1(this, context);
   }
}
