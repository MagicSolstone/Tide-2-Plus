package net.minecraft.world.entity.animal.equine;

import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;

class Llama$LlamaGroupData extends AgeableMob$AgeableMobGroupData {
   public final Llama$Variant variant;

   private Llama$LlamaGroupData(final Llama$Variant variant) {
      super(true);
      this.variant = variant;
   }
}
