package net.minecraft.network.protocol.game;

import net.minecraft.network.protocol.PacketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public abstract class ClientboundMoveEntityPacket implements MovementPacket<ClientGamePacketListener> {
   private static final int ON_GROUND_FLAG = 1;
   private static final int STEP_COUNT_OFFSET = 1;
   protected final int entityId;
   protected final VecDelta delta;
   protected final byte yRot;
   protected final byte xRot;
   protected final boolean onGround;
   protected final boolean hasRot;
   protected final boolean hasPos;

   protected static boolean unpackOnGround(final int properties) {
      return (properties & 1) != 0;
   }

   protected static int unpackStepCount(final int properties) {
      return properties >>> 1;
   }

   protected static int packProperties(final boolean onGround, final int stepCount) {
      return (onGround ? 1 : 0) | stepCount << 1;
   }

   protected ClientboundMoveEntityPacket(
      final int entityId, final VecDelta delta, final byte yRot, final byte xRot, final boolean onGround, final boolean hasRot, final boolean hasPos
   ) {
      this.entityId = entityId;
      this.delta = delta;
      this.yRot = yRot;
      this.xRot = xRot;
      this.onGround = onGround;
      this.hasRot = hasRot;
      this.hasPos = hasPos;
   }

   @Override
   public abstract PacketType<? extends ClientboundMoveEntityPacket> type();

   public void handle(final ClientGamePacketListener listener) {
      listener.handleMoveEntity(this);
   }

   @Override
   public String toString() {
      return "Entity_" + super.toString();
   }

   @Nullable
   public Entity getEntity(final Level level) {
      return level.getEntity(this.entityId);
   }

   public VecDelta getPositionDelta() {
      return this.delta;
   }

   public float getYRot() {
      return Mth.unpackDegrees(this.yRot);
   }

   public float getXRot() {
      return Mth.unpackDegrees(this.xRot);
   }

   @Override
   public boolean hasRotation() {
      return this.hasRot;
   }

   @Override
   public boolean hasPosition() {
      return this.hasPos;
   }

   public boolean isOnGround() {
      return this.onGround;
   }
}
