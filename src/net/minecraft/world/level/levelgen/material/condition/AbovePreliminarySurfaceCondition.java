package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public enum AbovePreliminarySurfaceCondition implements MaterialCondition {
   INSTANCE;

   public static final MapCodec<AbovePreliminarySurfaceCondition> CODEC = MapCodec.unit(INSTANCE);

   @Override
   public MapCodec<AbovePreliminarySurfaceCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext context) {
      return () -> context.blockY() >= context.getMinSurfaceLevel();
   }
}
