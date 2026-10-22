package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public enum TemperatureCondition implements MaterialCondition {
   INSTANCE;

   public static final MapCodec<TemperatureCondition> CODEC = MapCodec.unit(INSTANCE);

   @Override
   public MapCodec<TemperatureCondition> codec() {
      return CODEC;
   }

   @Override
   public ConditionEvaluator compile(final MaterialRuleContext context) {
      return () -> context.getBiome().value().coldEnoughToSnow(context.blockPos(), context.getSeaLevel());
   }
}
