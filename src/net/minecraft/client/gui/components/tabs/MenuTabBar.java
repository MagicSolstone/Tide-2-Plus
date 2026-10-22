package net.minecraft.client.gui.components.tabs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class MenuTabBar extends TabNavigationBar {
   private static final int HEIGHT = 24;
   private static final int MAX_WIDTH = 400;
   private static final int MARGIN = 14;

   public MenuTabBar(
      final int x,
      final int y,
      final int width,
      final int height,
      final TabManager tabManager,
      final ImmutableList<TabButton> tabButtons,
      final ImmutableList<Tab> tabs
   ) {
      super(x, y, width, height, tabManager, tabButtons, tabs);
   }

   public static MenuTabBar$Builder builder(final TabManager tabManager, final int width) {
      return new MenuTabBar$Builder(tabManager, width);
   }

   @Override
   protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.blit(
         RenderPipelines.GUI_TEXTURED,
         Screen.HEADER_SEPARATOR,
         0,
         this.layout.getY() + this.layout.getHeight() - 2,
         0.0F,
         0.0F,
         ((TabButton)this.tabButtons.getFirst()).getX(),
         2,
         32,
         2
      );
      int afterLastTab = ((TabButton)this.tabButtons.get(this.tabButtons.size() - 1)).getRight();
      graphics.blit(
         RenderPipelines.GUI_TEXTURED,
         Screen.HEADER_SEPARATOR,
         afterLastTab,
         this.layout.getY() + this.layout.getHeight() - 2,
         0.0F,
         0.0F,
         this.width,
         2,
         32,
         2
      );
      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public void arrangeElements(final int width) {
      this.width = width;
      int tabsWidth = Math.min(400, width) - 28;
      int tabWidth = Mth.roundToward(tabsWidth / this.tabs.size(), 2);
      UnmodifiableIterator var4 = this.tabButtons.iterator();

      while (var4.hasNext()) {
         TabButton button = (TabButton)var4.next();
         button.setWidth(tabWidth);
      }

      this.layout.arrangeElements();
      this.layout.setX(Mth.roundToward((width - tabsWidth) / 2, 2));
      this.layout.setY(0);
   }
}
