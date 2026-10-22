package net.minecraft.world.entity.animal.axolotl;

import java.util.Objects;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;

class Axolotl$AxolotlLookControl extends SmoothSwimmingLookControl {
   public Axolotl$AxolotlLookControl(final Axolotl this$0, final Axolotl axolotl, final int maxYRotFromCenter) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(axolotl, maxYRotFromCenter);
   }

   @Override
   public void tick() {
      if (!this.this$0.isPlayingDead()) {
         super.tick();
      }
   }
}
