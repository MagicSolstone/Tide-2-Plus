package net.minecraft.world.attribute;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

class EnvironmentAttributeProbe$ValueProbe<Value> {
   private Value lastValue;
   @Nullable
   private Value newValue;

   public EnvironmentAttributeProbe$ValueProbe(final EnvironmentAttribute<Value> this$0, final EnvironmentAttribute attribute) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      Value value = this.getValueFromLevel(attribute);
      this.lastValue = value;
      this.newValue = value;
   }

   private Value getValueFromLevel(final EnvironmentAttribute<Value> attribute) {
      return this.this$0.level != null && this.this$0.position != null
         ? this.this$0.level.environmentAttributes().getValue(attribute, this.this$0.position, this.this$0.biomeInterpolator)
         : attribute.defaultValue();
   }

   public boolean tick() {
      if (this.newValue == null) {
         return true;
      } else {
         this.lastValue = this.newValue;
         this.newValue = null;
         return false;
      }
   }

   public Value get(final EnvironmentAttribute<Value> attribute, final float partialTicks) {
      if (this.newValue == null) {
         this.newValue = this.getValueFromLevel(attribute);
      }

      return attribute.type().partialTickLerp().apply(partialTicks, this.lastValue, this.newValue);
   }
}
