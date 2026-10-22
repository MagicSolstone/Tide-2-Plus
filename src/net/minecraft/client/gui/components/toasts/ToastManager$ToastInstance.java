package net.minecraft.client.gui.components.toasts;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
class ToastManager$ToastInstance<T extends Toast> {
   private static final long SLIDE_ANIMATION_DURATION_MS = 600L;
   private final T toast;
   private final int firstSlotIndex;
   private final int occupiedSlotCount;
   private long animationStartTime;
   private long becameFullyVisibleAt;
   private Toast$Visibility visibility;
   private long fullyVisibleFor;
   private float visiblePortion;
   protected boolean hasFinishedRendering;

   private ToastManager$ToastInstance(final T this$0, final int toast, final int firstSlotIndex, final int occupiedSlotCount) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.toast = toast;
      this.firstSlotIndex = firstSlotIndex;
      this.occupiedSlotCount = occupiedSlotCount;
      this.resetToast();
   }

   public T getToast() {
      return this.toast;
   }

   public void resetToast() {
      this.animationStartTime = -1L;
      this.becameFullyVisibleAt = -1L;
      this.visibility = Toast$Visibility.HIDE;
      this.fullyVisibleFor = 0L;
      this.visiblePortion = 0.0F;
      this.hasFinishedRendering = false;
   }

   public boolean hasFinishedRendering() {
      return this.hasFinishedRendering;
   }

   private void calculateVisiblePortion(final long now) {
      float animationProgress = Mth.clamp((float)(now - this.animationStartTime) / 600.0F, 0.0F, 1.0F);
      animationProgress *= animationProgress;
      if (this.visibility == Toast$Visibility.HIDE) {
         this.visiblePortion = 1.0F - animationProgress;
      } else {
         this.visiblePortion = animationProgress;
      }
   }

   public void update() {
      long now = Util.getMillis();
      if (this.animationStartTime == -1L) {
         this.animationStartTime = now;
         this.visibility = Toast$Visibility.SHOW;
      }

      if (this.visibility == Toast$Visibility.SHOW && now - this.animationStartTime <= 600L) {
         this.becameFullyVisibleAt = now;
      }

      this.fullyVisibleFor = now - this.becameFullyVisibleAt;
      this.calculateVisiblePortion(now);
      this.toast.update(this.this$0, this.fullyVisibleFor);
      Toast$Visibility wantedVisibility = this.toast.getWantedVisibility();
      if (wantedVisibility != this.visibility) {
         this.animationStartTime = now - (int)((1.0F - this.visiblePortion) * 600.0F);
         this.visibility = wantedVisibility;
      }

      boolean wasAlreadyFinishedRendering = this.hasFinishedRendering;
      this.hasFinishedRendering = this.visibility == Toast$Visibility.HIDE && now - this.animationStartTime > 600L;
      if (this.hasFinishedRendering && !wasAlreadyFinishedRendering) {
         this.toast.onFinishedRendering();
      }
   }

   public void extractRenderState(final GuiGraphicsExtractor graphics, final int screenWidth) {
      if (!this.hasFinishedRendering) {
         graphics.pose().pushMatrix();
         graphics.pose().translate(this.toast.xPos(screenWidth, this.visiblePortion), this.toast.yPos(this.firstSlotIndex));
         this.toast.extractRenderState(graphics, this.this$0.minecraft.font, this.fullyVisibleFor);
         graphics.pose().popMatrix();
      }
   }
}
