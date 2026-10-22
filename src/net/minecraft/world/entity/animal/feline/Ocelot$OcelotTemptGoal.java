package net.minecraft.world.entity.animal.feline;

import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.ItemStack;

class Ocelot$OcelotTemptGoal extends TemptGoal {
   private final Ocelot ocelot;

   public Ocelot$OcelotTemptGoal(final Ocelot ocelot, final double speedModifier, final Predicate<ItemStack> items, final boolean canScare) {
      super(ocelot, speedModifier, items, canScare);
      this.ocelot = ocelot;
   }

   @Override
   protected boolean canScare() {
      return super.canScare() && !this.ocelot.isTrusting();
   }
}
