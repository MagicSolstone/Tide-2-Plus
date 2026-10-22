package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry$NarrationPriority;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationTrigger;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.FocusNavigationEvent$ArrowNavigation;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenDirection;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class ContainerObjectSelectionList<E extends ContainerObjectSelectionList$Entry<E>> extends AbstractSelectionList<E> {
   public ContainerObjectSelectionList(final Minecraft minecraft, final int width, final int height, final int y, final int itemHeight) {
      super(minecraft, width, height, y, itemHeight);
   }

   @Nullable
   @Override
   public ComponentPath nextFocusPath(final FocusNavigationEvent navigationEvent) {
      if (this.getItemCount() == 0) {
         return null;
      } else if (!(navigationEvent instanceof FocusNavigationEvent$ArrowNavigation arrowNavigation)) {
         return super.nextFocusPath(navigationEvent);
      } else {
         E focused = this.getFocused();
         if (arrowNavigation.direction().getAxis() == ScreenAxis.HORIZONTAL && focused != null) {
            return ComponentPath.path(this, focused.nextFocusPath(navigationEvent));
         } else {
            int index = -1;
            ScreenDirection direction = arrowNavigation.direction();
            if (focused != null) {
               index = focused.children().indexOf(focused.getFocused());
            }

            if (index == -1) {
               switch (direction) {
                  case LEFT:
                     index = Integer.MAX_VALUE;
                     direction = ScreenDirection.DOWN;
                     break;
                  case RIGHT:
                     index = 0;
                     direction = ScreenDirection.DOWN;
                     break;
                  default:
                     index = 0;
               }
            }

            E entry = focused;

            ComponentPath componentPath;
            do {
               entry = this.nextEntry(direction, e -> !e.children().isEmpty(), entry);
               if (entry == null) {
                  return null;
               }

               componentPath = entry.focusPathAtIndex(arrowNavigation, index);
            } while (componentPath == null);

            return ComponentPath.path(this, componentPath);
         }
      }
   }

   @Override
   public void setFocused(@Nullable final GuiEventListener focused) {
      if (this.getFocused() != focused) {
         super.setFocused(focused);
         if (focused == null) {
            this.setSelected(null);
         }
      }
   }

   @Override
   public NarratableEntry$NarrationPriority narrationPriority() {
      return this.isFocused() ? NarratableEntry$NarrationPriority.FOCUSED : super.narrationPriority();
   }

   @Override
   protected boolean entriesCanBeSelected() {
      return false;
   }

   @Override
   public void updateWidgetNarration(final NarrationElementOutput output) {
      E hovered = this.getHovered();
      NarrationTrigger narrationTrigger = output.narrationTrigger();
      if ((narrationTrigger == NarrationTrigger.MOUSE || narrationTrigger == NarrationTrigger.SYSTEM) && hovered != null) {
         hovered.updateNarration(output.nest());
         this.narrateListElementPosition(output, hovered);
      } else {
         E focused = this.getFocused();
         if (focused != null) {
            focused.updateNarration(output.nest());
            this.narrateListElementPosition(output, focused);
         }
      }
   }
}
