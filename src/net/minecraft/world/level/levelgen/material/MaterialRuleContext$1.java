package net.minecraft.world.level.levelgen.material;

import java.util.Objects;
import java.util.function.DoubleSupplier;
import net.minecraft.world.level.levelgen.synth.Noise;

class MaterialRuleContext$1 implements DoubleSupplier {
   private long lastUpdateXZ;
   private double lastNoise;

   MaterialRuleContext$1(final MaterialRuleContext this$0, final Noise val$noise) {
      this.val$noise = val$noisex;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.lastUpdateXZ = this.this$0.lastUpdateXZ - 1L;
   }

   @Override
   public double getAsDouble() {
      if (this.lastUpdateXZ != this.this$0.lastUpdateXZ) {
         this.lastNoise = this.val$noise.get(this.this$0.blockX, 0.0, this.this$0.blockZ);
         this.lastUpdateXZ = this.this$0.lastUpdateXZ;
      }

      return this.lastNoise;
   }
}
