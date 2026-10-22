package net.minecraft.world.level.levelgen.material.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public record MaterialCondition$HolderHolder(Holder<MaterialCondition> holder) implements MaterialCondition {
   @Override
   public ConditionEvaluator compile(final MaterialRuleContext context) {
      return this.holder.value().compile(context);
   }

   @Override
   public MapCodec<MaterialCondition$HolderHolder> codec() {
      throw new UnsupportedOperationException("HolderHolder cannot be serialized");
   }
}
