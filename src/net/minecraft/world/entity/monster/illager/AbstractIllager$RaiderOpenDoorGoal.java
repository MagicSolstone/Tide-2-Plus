package net.minecraft.world.entity.monster.illager;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.raid.Raider;

public class AbstractIllager$RaiderOpenDoorGoal extends OpenDoorGoal {
   public AbstractIllager$RaiderOpenDoorGoal(final AbstractIllager this$0, final Raider raider) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(raider, false);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.this$0.hasActiveRaid();
   }
}
