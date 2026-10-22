package net.minecraft.world.entity.vehicle.minecart;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;

class MinecartSpawner$1 extends BaseSpawner {
   MinecartSpawner$1(final MinecartSpawner this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void broadcastEvent(final Level level, final BlockPos pos, final int id) {
      level.broadcastEntityEvent(this.this$0, (byte)id);
   }
}
