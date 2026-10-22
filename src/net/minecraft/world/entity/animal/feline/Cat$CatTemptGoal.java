package net.minecraft.world.entity.animal.feline;

import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

class Cat$CatTemptGoal extends TemptGoal {
   @Nullable
   private Player selectedPlayer;
   private final Cat cat;

   public Cat$CatTemptGoal(final Cat mob, final double speedModifier, final Predicate<ItemStack> items, final boolean canScare) {
      super(mob, speedModifier, items, canScare);
      this.cat = mob;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
         this.selectedPlayer = this.player;
      } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
         this.selectedPlayer = null;
      }
   }

   @Override
   protected boolean canScare() {
      return this.selectedPlayer != null && this.selectedPlayer.equals(this.player) ? false : super.canScare();
   }

   @Override
   public boolean canUse() {
      return super.canUse() && !this.cat.isTame();
   }
}
