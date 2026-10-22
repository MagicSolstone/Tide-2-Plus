package net.minecraft.client.gui.components.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractContainerEventHandler implements ContainerEventHandler {
   @Nullable
   private GuiEventListener focused;
   private boolean isDragging;

   @Override
   public final boolean isDragging() {
      return this.isDragging;
   }

   @Override
   public final void setDragging(final boolean dragging) {
      this.isDragging = dragging;
   }

   @Nullable
   @Override
   public GuiEventListener getFocused() {
      return this.focused;
   }

   @Override
   public void setFocused(@Nullable final GuiEventListener focused) {
      if (this.focused != focused) {
         if (this.focused != null) {
            this.focused.setFocused(false);
         }

         if (focused != null) {
            focused.setFocused(true);
         }

         this.focused = focused;
      }
   }
}
