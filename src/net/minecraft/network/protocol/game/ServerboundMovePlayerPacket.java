package net.minecraft.network.protocol.game;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public abstract class ServerboundMovePlayerPacket implements Packet<ServerGamePacketListener> {
   private static final int FLAG_ON_GROUND = 1;
   private static final int FLAG_HORIZONTAL_COLLISION = 2;
   protected final double x;
   protected final double y;
   protected final double z;
   protected final float yRot;
   protected final float xRot;
   protected final boolean onGround;
   protected final boolean horizontalCollision;
   protected final boolean hasPos;
   protected final boolean hasRot;

   private static int packFlags(final boolean onGround, final boolean horizontalCollision) {
      int flags = 0;
      if (onGround) {
         flags |= 1;
      }

      if (horizontalCollision) {
         flags |= 2;
      }

      return flags;
   }

   private static boolean unpackOnGround(final int flags) {
      return (flags & 1) != 0;
   }

   private static boolean unpackHorizontalCollision(final int flags) {
      return (flags & 2) != 0;
   }

   protected ServerboundMovePlayerPacket(
      final double x,
      final double y,
      final double z,
      final float yRot,
      final float xRot,
      final boolean onGround,
      final boolean horizontalCollision,
      final boolean hasPos,
      final boolean hasRot
   ) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.yRot = yRot;
      this.xRot = xRot;
      this.onGround = onGround;
      this.horizontalCollision = horizontalCollision;
      this.hasPos = hasPos;
      this.hasRot = hasRot;
   }

   @Override
   public abstract PacketType<? extends ServerboundMovePlayerPacket> type();

   public void handle(final ServerGamePacketListener listener) {
      listener.handleMovePlayer(this);
   }

   public double getX(final double fallback) {
      return this.hasPos ? this.x : fallback;
   }

   public double getY(final double fallback) {
      return this.hasPos ? this.y : fallback;
   }

   public double getZ(final double fallback) {
      return this.hasPos ? this.z : fallback;
   }

   public float getYRot(final float fallback) {
      return this.hasRot ? this.yRot : fallback;
   }

   public float getXRot(final float fallback) {
      return this.hasRot ? this.xRot : fallback;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public boolean horizontalCollision() {
      return this.horizontalCollision;
   }

   public boolean hasPosition() {
      return this.hasPos;
   }

   public boolean hasRotation() {
      return this.hasRot;
   }
}
