package net.minecraft.world.entity.animal.equine;

import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;

public class Horse$HorseGroupData extends AgeableMob$AgeableMobGroupData {
   public final Variant variant;

   public Horse$HorseGroupData(final Variant variant) {
      super(true);
      this.variant = variant;
   }
}
