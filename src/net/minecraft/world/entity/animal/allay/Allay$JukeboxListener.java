package net.minecraft.world.entity.animal.allay;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

class Allay$JukeboxListener implements GameEventListener {
   private final PositionSource listenerSource;
   private final int listenerRadius;

   public Allay$JukeboxListener(final Allay this$0, final PositionSource listenerSource, final int listenerRadius) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.listenerSource = listenerSource;
      this.listenerRadius = listenerRadius;
   }

   @Override
   public PositionSource getListenerSource() {
      return this.listenerSource;
   }

   @Override
   public int getListenerRadius() {
      return this.listenerRadius;
   }

   @Override
   public boolean handleGameEvent(final ServerLevel level, final Holder<GameEvent> event, final GameEvent$Context context, final Vec3 sourcePosition) {
      if (event.is(GameEvent.JUKEBOX_PLAY)) {
         this.this$0.setJukeboxPlaying(BlockPos.containing(sourcePosition), true);
         return true;
      } else if (event.is(GameEvent.JUKEBOX_STOP_PLAY)) {
         this.this$0.setJukeboxPlaying(BlockPos.containing(sourcePosition), false);
         return true;
      } else {
         return false;
      }
   }
}
