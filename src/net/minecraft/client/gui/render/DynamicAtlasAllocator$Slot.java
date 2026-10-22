package net.minecraft.client.gui.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DynamicAtlasAllocator$Slot {
   private final int x;
   private final int y;
   private boolean fresh = true;
   private boolean discardAfterFrame;
   private DynamicAtlasAllocator$SlotState externalState = DynamicAtlasAllocator$SlotState.EMPTY;

   private DynamicAtlasAllocator$Slot(final int x, final int y) {
      this.x = x;
      this.y = y;
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public DynamicAtlasAllocator$SlotState state() {
      return this.externalState;
   }
}
