package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public enum SteepCondition implements MaterialCondition {
   INSTANCE;

   public static final MapCodec<SteepCondition> CODEC = MapCodec.unit(INSTANCE);

   @Override
   public MapCodec<SteepCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext context) {
      return new SteepCondition$1(this, context);
   }
}
