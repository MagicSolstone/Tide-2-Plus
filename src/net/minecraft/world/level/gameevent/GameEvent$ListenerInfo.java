package net.minecraft.world.level.gameevent;

import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;

public final class GameEvent$ListenerInfo implements Comparable<GameEvent$ListenerInfo> {
   private final Holder<GameEvent> gameEvent;
   private final Vec3 source;
   private final GameEvent$Context context;
   private final GameEventListener recipient;
   private final double distanceToRecipient;

   public GameEvent$ListenerInfo(
      final Holder<GameEvent> gameEvent, final Vec3 source, final GameEvent$Context context, final GameEventListener recipient, final Vec3 recipientPos
   ) {
      this.gameEvent = gameEvent;
      this.source = source;
      this.context = context;
      this.recipient = recipient;
      this.distanceToRecipient = source.distanceToSqr(recipientPos);
   }

   public int compareTo(final GameEvent$ListenerInfo other) {
      return Double.compare(this.distanceToRecipient, other.distanceToRecipient);
   }

   public Holder<GameEvent> gameEvent() {
      return this.gameEvent;
   }

   public Vec3 source() {
      return this.source;
   }

   public GameEvent$Context context() {
      return this.context;
   }

   public GameEventListener recipient() {
      return this.recipient;
   }
}
