package net.minecraft.world.entity.animal.wolf;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;

public class Wolf$WolfPackData extends AgeableMob$AgeableMobGroupData {
   public final Holder<WolfVariant> type;

   public Wolf$WolfPackData(final Holder<WolfVariant> type) {
      super(false);
      this.type = type;
   }
}
