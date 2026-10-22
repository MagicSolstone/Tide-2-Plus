package net.minecraft.client.gui.components.tabs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor$HoveredTextEffects;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class MenuTabBar$MenuTabButton extends TabButton {
   private static final WidgetSprites SPRITES = new WidgetSprites(
      Identifier.withDefaultNamespace("widget/tab_selected"),
      Identifier.withDefaultNamespace("widget/tab"),
      Identifier.withDefaultNamespace("widget/tab_selected_highlighted"),
      Identifier.withDefaultNamespace("widget/tab_highlighted")
   );
   private static final int SELECTED_OFFSET = 3;
   private static final int TEXT_MARGIN = 1;
   private static final int UNDERLINE_HEIGHT = 1;
   private static final int UNDERLINE_MARGIN_X = 4;
   private static final int UNDERLINE_MARGIN_BOTTOM = 2;

   public MenuTabBar$MenuTabButton(final TabManager tabManager, final Tab tab, final int width, final int height) {
      super(tabManager, tab, width, height);
   }

   @Override
   protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.blitSprite(
         RenderPipelines.GUI_TEXTURED, SPRITES.get(this.isSelected(), this.isHoveredOrFocused()), this.getX(), this.getY(), this.width, this.height
      );
      Font font = Minecraft.getInstance().font;
      int underlineColor = this.active ? -1 : -6250336;
      if (this.isSelected()) {
         this.renderMenuBackground(graphics, this.getX() + 2, this.getY() + 2, this.getRight() - 2, this.getBottom());
         this.renderFocusUnderline(graphics, font, underlineColor);
      }

      this.renderLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor$HoveredTextEffects.NONE));
      this.handleCursor(graphics);
   }

   protected void renderMenuBackground(final GuiGraphicsExtractor graphics, final int x0, final int y0, final int x1, final int y1) {
      Screen.extractMenuBackgroundTexture(graphics, Screen.MENU_BACKGROUND, x0, y0, 0.0F, 0.0F, x1 - x0, y1 - y0);
   }

   private void renderLabel(final ActiveTextCollector output) {
      int left = this.getX() + 1;
      int top = this.getY() + (this.isSelected() ? 0 : 3);
      int right = this.getX() + this.getWidth() - 1;
      int bottom = this.getY() + this.getHeight();
      output.acceptScrollingWithDefaultCenter(this.getMessage(), left, right, top, bottom);
   }

   private void renderFocusUnderline(final GuiGraphicsExtractor graphics, final Font font, final int color) {
      int width = Math.min(font.width(this.getMessage()), this.getWidth() - 4);
      int left = this.getX() + (this.getWidth() - width) / 2;
      int top = this.getY() + this.getHeight() - 2;
      graphics.fill(left, top, left + width, top + 1, color);
   }
}
