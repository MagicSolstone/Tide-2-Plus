package net.minecraft.world.level.gameevent;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface GameEventListenerRegistry$ListenerVisitor {
   void visit(GameEventListener listener, Vec3 position);
}
