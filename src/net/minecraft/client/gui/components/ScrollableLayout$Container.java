package net.minecraft.client.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ScrollableLayout$Container extends AbstractContainerWidget {
   private final Minecraft minecraft;
   private final List<AbstractWidget> children;

   public ScrollableLayout$Container(
      final ScrollableLayout this$0, final Minecraft minecraft, final int width, final int height, final AbstractScrollArea$ScrollbarSettings scrollbarSettings
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(0, 0, width, height, CommonComponents.EMPTY, scrollbarSettings);
      this.children = new ArrayList<>();
      this.minecraft = minecraft;
      this.refreshChildren();
   }

   public void refreshChildren() {
      this.children.clear();
      this.this$0.content.visitWidgets(this.children::add);
   }

   @Override
   protected int contentHeight() {
      return this.this$0.content.getHeight();
   }

   @Override
   protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height);

      for (AbstractWidget child : this.children) {
         child.extractRenderState(graphics, mouseX, mouseY, a);
      }

      graphics.disableScissor();
      this.extractScrollbar(graphics, mouseX, mouseY);
   }

   @Override
   protected void updateWidgetNarration(final NarrationElementOutput output) {
   }

   @Override
   public ScreenRectangle getBorderForArrowNavigation(final ScreenDirection opposite) {
      GuiEventListener focused = this.getFocused();
      return focused != null
         ? focused.getBorderForArrowNavigation(opposite)
         : new ScreenRectangle(this.getX(), this.getY(), this.width, this.contentHeight()).getBorder(opposite);
   }

   @Override
   public void setFocused(@Nullable final GuiEventListener focused) {
      super.setFocused(focused);
      if (focused != null && this.minecraft.getLastInputType().isKeyboard()) {
         ScreenRectangle area = this.getRectangle();
         ScreenRectangle focusedRect = focused.getRectangle();
         int topDelta = focusedRect.top() - area.top();
         int bottomDelta = focusedRect.bottom() - area.bottom();
         double scrollRate = this.scrollRate();
         if (topDelta < 0) {
            this.setScrollAmount(this.scrollAmount() + topDelta - scrollRate);
         } else if (bottomDelta > 0) {
            this.setScrollAmount(this.scrollAmount() + bottomDelta + scrollRate);
         }
      }
   }

   @Override
   public void setX(final int x) {
      super.setX(x);
      this.this$0.content.setX(x + (this.this$0.reserveStrategy == ScrollableLayout$ReserveStrategy.BOTH ? this.scrollbarReserve() : 0));
   }

   @Override
   public void setY(final int y) {
      super.setY(y);
      this.this$0.content.setY(y - (int)this.scrollAmount());
   }

   private int scrollbarReserve() {
      return this.this$0.scrollbarSpacing + this.scrollbarWidth();
   }

   @Override
   public void setScrollAmount(final double scrollAmount) {
      super.setScrollAmount(scrollAmount);
      this.this$0.content.setY(this.getRectangle().top() - (int)this.scrollAmount());
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return this.children;
   }

   @Override
   public Collection<? extends NarratableEntry> getNarratables() {
      return this.children;
   }
}
