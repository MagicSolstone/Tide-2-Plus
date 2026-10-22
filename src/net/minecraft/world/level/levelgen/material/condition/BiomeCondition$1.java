package net.minecraft.world.level.levelgen.material.condition;

import java.util.Objects;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext$LazyYCondition;

class BiomeCondition$1 extends MaterialRuleContext$LazyYCondition {
   BiomeCondition$1(final BiomeCondition this$0, final MaterialRuleContext context) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(context);
   }

   @Override
   protected boolean compute() {
      return this.this$0.biomes.contains(this.context.getBiome());
   }
}
