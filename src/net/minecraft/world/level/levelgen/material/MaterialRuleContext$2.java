package net.minecraft.world.level.levelgen.material;

import java.util.Objects;
import java.util.function.DoubleSupplier;
import net.minecraft.world.level.levelgen.synth.Noise;

class MaterialRuleContext$2 implements DoubleSupplier {
   private long lastUpdateY;
   private double lastNoise;

   MaterialRuleContext$2(final MaterialRuleContext this$0, final Noise val$noise) {
      this.val$noise = val$noisex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.lastUpdateY = this.this$0.lastUpdateY - 1L;
   }

   @Override
   public double getAsDouble() {
      if (this.lastUpdateY != this.this$0.lastUpdateY) {
         this.lastNoise = this.val$noise.get(this.this$0.blockX, this.this$0.blockY, this.this$0.blockZ);
         this.lastUpdateY = this.this$0.lastUpdateY;
      }

      return this.lastNoise;
   }
}
