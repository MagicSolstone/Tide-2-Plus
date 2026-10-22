package net.minecraft.client.renderer.state.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class GuiItemRenderState implements ScreenArea {
   private final Matrix3x2f pose;
   private final TrackingItemStackRenderState itemStackRenderState;
   private final int x;
   private final int y;
   @Nullable
   private final ScreenRectangle scissorArea;
   @Nullable
   private final ScreenRectangle oversizedItemBounds;
   @Nullable
   private final ScreenRectangle bounds;

   public GuiItemRenderState(
      final Matrix3x2f pose, final TrackingItemStackRenderState itemStackRenderState, final int x, final int y, @Nullable final ScreenRectangle scissorArea
   ) {
      this.pose = pose;
      this.itemStackRenderState = itemStackRenderState;
      this.x = x;
      this.y = y;
      this.scissorArea = scissorArea;
      this.oversizedItemBounds = this.itemStackRenderState().isOversizedInGui() ? this.calculateOversizedItemBounds() : null;
      this.bounds = this.calculateBounds(this.oversizedItemBounds != null ? this.oversizedItemBounds : new ScreenRectangle(this.x, this.y, 16, 16));
   }

   @Nullable
   private ScreenRectangle calculateOversizedItemBounds() {
      AABB aabb = this.itemStackRenderState.getModelBoundingBox();
      int actualXSize = Mth.ceil(aabb.getXsize() * 16.0);
      int actualYSize = Mth.ceil(aabb.getYsize() * 16.0);
      if (actualXSize <= 16 && actualYSize <= 16) {
         return null;
      } else {
         float xOffset = (float)(aabb.minX * 16.0);
         float yOffset = (float)(aabb.maxY * 16.0);
         int flooredXOffset = Mth.floor(xOffset);
         int flooredYOffset = Mth.floor(yOffset);
         int actualX = this.x + flooredXOffset + 8;
         int actualY = this.y - flooredYOffset + 8;
         return new ScreenRectangle(actualX, actualY, actualXSize, actualYSize);
      }
   }

   @Nullable
   private ScreenRectangle calculateBounds(final ScreenRectangle itemBounds) {
      ScreenRectangle bounds = itemBounds.transformMaxBounds(this.pose);
      return this.scissorArea != null ? this.scissorArea.intersection(bounds) : bounds;
   }

   public Matrix3x2f pose() {
      return this.pose;
   }

   public TrackingItemStackRenderState itemStackRenderState() {
      return this.itemStackRenderState;
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   @Nullable
   public ScreenRectangle scissorArea() {
      return this.scissorArea;
   }

   @Nullable
   public ScreenRectangle oversizedItemBounds() {
      return this.oversizedItemBounds;
   }

   @Nullable
   @Override
   public ScreenRectangle bounds() {
      return this.bounds;
   }
}
