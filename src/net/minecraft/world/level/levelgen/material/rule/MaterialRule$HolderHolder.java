package net.minecraft.world.level.levelgen.material.rule;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public record MaterialRule$HolderHolder(Holder<MaterialRule> holder) implements MaterialRule {
   @Override
   public RuleEvaluator compile(final MaterialRuleContext context) {
      return this.holder.value().compile(context);
   }

   @Override
   public MapCodec<MaterialRule$HolderHolder> codec() {
      throw new UnsupportedOperationException("HolderHolder cannot be serialized");
   }
}
