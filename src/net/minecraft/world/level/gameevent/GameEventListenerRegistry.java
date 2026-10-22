package net.minecraft.world.level.gameevent;

import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;

public interface GameEventListenerRegistry {
   GameEventListenerRegistry NOOP = new GameEventListenerRegistry$1();

   boolean isEmpty();

   void register(GameEventListener listener);

   void unregister(GameEventListener listener);

   boolean visitInRangeListeners(Holder<GameEvent> event, Vec3 sourcePosition, GameEvent$Context context, GameEventListenerRegistry$ListenerVisitor action);
}
