package net.minecraft.world.entity.animal.fox;

import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;

public class Fox$FoxGroupData extends AgeableMob$AgeableMobGroupData {
   public final Fox$Variant variant;

   public Fox$FoxGroupData(final Fox$Variant variant) {
      super(false);
      this.variant = variant;
   }
}
