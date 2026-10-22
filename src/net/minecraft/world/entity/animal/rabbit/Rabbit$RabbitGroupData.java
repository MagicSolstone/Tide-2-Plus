package net.minecraft.world.entity.animal.rabbit;

import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;

public class Rabbit$RabbitGroupData extends AgeableMob$AgeableMobGroupData {
   public final Rabbit$Variant variant;

   public Rabbit$RabbitGroupData(final Rabbit$Variant variant) {
      super(1.0F);
      this.variant = variant;
   }
}
