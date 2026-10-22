package net.minecraft.world.level.gameevent;

import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;

class GameEventListenerRegistry$1 implements GameEventListenerRegistry {
   @Override
   public boolean isEmpty() {
      return true;
   }

   @Override
   public void register(final GameEventListener listener) {
   }

   @Override
   public void unregister(final GameEventListener listener) {
   }

   @Override
   public boolean visitInRangeListeners(
      final Holder<GameEvent> event, final Vec3 sourcePosition, final GameEvent$Context context, final GameEventListenerRegistry$ListenerVisitor action
   ) {
      return false;
   }
}
