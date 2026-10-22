package net.minecraft.world.level.saveddata.maps;

import java.util.Collection;
import java.util.Objects;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public class MapItemSavedData$HoldingPlayer {
   public final Player player;
   private boolean dirtyData;
   private int minDirtyX;
   private int minDirtyY;
   private int maxDirtyX;
   private int maxDirtyY;
   private boolean dirtyDecorations;
   private int tick;
   public int step;

   private MapItemSavedData$HoldingPlayer(final MapItemSavedData this$0, final Player player) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.dirtyData = true;
      this.maxDirtyX = 127;
      this.maxDirtyY = 127;
      this.dirtyDecorations = true;
      this.player = player;
   }

   private MapItemSavedData$MapPatch createPatch() {
      int startX = this.minDirtyX;
      int startY = this.minDirtyY;
      int width = this.maxDirtyX + 1 - this.minDirtyX;
      int height = this.maxDirtyY + 1 - this.minDirtyY;
      byte[] patch = new byte[width * height];

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            patch[x + y * width] = this.this$0.colors[startX + x + (startY + y) * 128];
         }
      }

      return new MapItemSavedData$MapPatch(startX, startY, width, height, patch);
   }

   @Nullable
   private Packet<?> nextUpdatePacket(final MapId id) {
      MapItemSavedData$MapPatch patch;
      if (this.dirtyData) {
         this.dirtyData = false;
         patch = this.createPatch();
      } else {
         patch = null;
      }

      Collection<MapDecoration> decorations;
      if (this.dirtyDecorations && this.tick++ % 5 == 0) {
         this.dirtyDecorations = false;
         decorations = this.this$0.decorations.values();
      } else {
         decorations = null;
      }

      return decorations == null && patch == null ? null : new ClientboundMapItemDataPacket(id, this.this$0.scale, this.this$0.locked, decorations, patch);
   }

   private void markColorsDirty(final int x, final int y) {
      if (this.dirtyData) {
         this.minDirtyX = Math.min(this.minDirtyX, x);
         this.minDirtyY = Math.min(this.minDirtyY, y);
         this.maxDirtyX = Math.max(this.maxDirtyX, x);
         this.maxDirtyY = Math.max(this.maxDirtyY, y);
      } else {
         this.dirtyData = true;
         this.minDirtyX = x;
         this.minDirtyY = y;
         this.maxDirtyX = x;
         this.maxDirtyY = y;
      }
   }

   private void markDecorationsDirty() {
      this.dirtyDecorations = true;
   }
}
