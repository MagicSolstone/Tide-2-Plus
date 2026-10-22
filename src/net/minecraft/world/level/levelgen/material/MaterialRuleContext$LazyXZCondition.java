package net.minecraft.world.level.levelgen.material;

import net.minecraft.world.level.levelgen.material.condition.ConditionEvaluator;
import org.jspecify.annotations.Nullable;

public abstract class MaterialRuleContext$LazyXZCondition implements ConditionEvaluator {
   protected final MaterialRuleContext context;
   private long lastUpdate;
   @Nullable
   private Boolean result;

   protected MaterialRuleContext$LazyXZCondition(final MaterialRuleContext context) {
      this.context = context;
      this.lastUpdate = context.lastUpdateXZ - 1L;
   }

   @Override
   public boolean test() {
      long lastContextUpdate = this.context.lastUpdateXZ;
      if (lastContextUpdate == this.lastUpdate) {
         if (this.result == null) {
            throw new IllegalStateException("Update triggered but the result is null");
         } else {
            return this.result;
         }
      } else {
         this.lastUpdate = lastContextUpdate;
         this.result = this.compute();
         return this.result;
      }
   }

   protected abstract boolean compute();
}
