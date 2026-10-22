package net.minecraft.server.players;

import java.util.Objects;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;

class PlayerList$1 implements BorderChangeListener {
   PlayerList$1(final PlayerList this$0, final ServerLevel val$level) {
      this.val$level = val$levelx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void onSetSize(final WorldBorder border, final double newSize) {
      this.this$0.broadcastAll(new ClientboundSetBorderSizePacket(border), this.val$level.dimension());
   }

   @Override
   public void onLerpSize(final WorldBorder border, final double fromSize, final double targetSize, final long ticks, final long gameTime) {
      this.this$0.broadcastAll(new ClientboundSetBorderLerpSizePacket(border), this.val$level.dimension());
   }

   @Override
   public void onSetCenter(final WorldBorder border, final double x, final double z) {
      this.this$0.broadcastAll(new ClientboundSetBorderCenterPacket(border), this.val$level.dimension());
   }

   @Override
   public void onSetWarningTime(final WorldBorder border, final int time) {
      this.this$0.broadcastAll(new ClientboundSetBorderWarningDelayPacket(border), this.val$level.dimension());
   }

   @Override
   public void onSetWarningBlocks(final WorldBorder border, final int blocks) {
      this.this$0.broadcastAll(new ClientboundSetBorderWarningDistancePacket(border), this.val$level.dimension());
   }

   @Override
   public void onSetDamagePerBlock(final WorldBorder border, final double damagePerBlock) {
   }

   @Override
   public void onSetSafeZone(final WorldBorder border, final double safeZone) {
   }
}
