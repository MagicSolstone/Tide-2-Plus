package net.minecraft.world.entity.animal.axolotl;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob$AgeableMobGroupData;

public class Axolotl$AxolotlGroupData extends AgeableMob$AgeableMobGroupData {
   public final Axolotl$Variant[] types;

   public Axolotl$AxolotlGroupData(final Axolotl$Variant... types) {
      super(false);
      this.types = types;
   }

   public Axolotl$Variant getVariant(final RandomSource random) {
      return this.types[random.nextInt(this.types.length)];
   }
}
