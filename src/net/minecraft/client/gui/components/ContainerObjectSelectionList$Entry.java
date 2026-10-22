package net.minecraft.client.gui.components;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.FocusNavigationEvent$ArrowNavigation;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.Screen$NarratableSearchResult;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class ContainerObjectSelectionList$Entry<E extends ContainerObjectSelectionList$Entry<E>>
   extends AbstractSelectionList$Entry<E>
   implements ContainerEventHandler {
   @Nullable
   private GuiEventListener focused;
   @Nullable
   private NarratableEntry lastNarratable;
   private boolean dragging;

   @Override
   public boolean isDragging() {
      return this.dragging;
   }

   @Override
   public void setDragging(final boolean dragging) {
      this.dragging = dragging;
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      return ContainerEventHandler.super.mouseClicked(event, doubleClick);
   }

   @Override
   public void setFocused(@Nullable final GuiEventListener focused) {
      if (this.focused != null) {
         this.focused.setFocused(false);
      }

      if (focused != null) {
         focused.setFocused(true);
      }

      this.focused = focused;
   }

   @Nullable
   @Override
   public GuiEventListener getFocused() {
      return this.focused;
   }

   @Nullable
   public ComponentPath focusPathAtIndex(final FocusNavigationEvent navigationEvent, final int currentIndex) {
      if (this.children().isEmpty()) {
         return null;
      } else {
         ComponentPath componentPath = this.children().get(Math.min(currentIndex, this.children().size() - 1)).nextFocusPath(navigationEvent);
         return ComponentPath.path(this, componentPath);
      }
   }

   @Nullable
   @Override
   public ComponentPath nextFocusPath(final FocusNavigationEvent navigationEvent) {
      if (navigationEvent instanceof FocusNavigationEvent$ArrowNavigation arrowNavigation) {
         int delta = switch (arrowNavigation.direction()) {
            case LEFT -> -1;
            case RIGHT -> 1;
            case UP, DOWN -> 0;
         };
         if (delta == 0) {
            return null;
         }

         int index = Mth.clamp(delta + this.children().indexOf(this.getFocused()), 0, this.children().size() - 1);

         for (int i = index; i >= 0 && i < this.children().size(); i += delta) {
            GuiEventListener child = this.children().get(i);
            ComponentPath componentPath = child.nextFocusPath(navigationEvent);
            if (componentPath != null) {
               return ComponentPath.path(this, componentPath);
            }
         }
      }

      return ContainerEventHandler.super.nextFocusPath(navigationEvent);
   }

   public abstract List<? extends NarratableEntry> narratables();

   void updateNarration(final NarrationElementOutput output) {
      List<? extends NarratableEntry> narratables = this.narratables();
      Screen$NarratableSearchResult result = Screen.findNarratableWidget(narratables, this.lastNarratable);
      if (result != null) {
         if (result.priority().isTerminal()) {
            this.lastNarratable = result.entry();
         }

         if (narratables.size() > 1) {
            output.add(NarratedElementType.POSITION, Component.translatable("narrator.position.object_list", result.index() + 1, narratables.size()));
         }

         result.entry().updateNarration(output.nest());
      }
   }
}
