package net.minecraft.client.gui.screens.achievement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.LoadingTab;
import net.minecraft.client.gui.components.tabs.MenuTabBar;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket$Action;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StatsScreen extends Screen {
   private static final Component TITLE = Component.translatable("gui.stats");
   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
   private static final Identifier HEADER_SPRITE = Identifier.withDefaultNamespace("statistics/header");
   private static final Identifier SORT_UP_SPRITE = Identifier.withDefaultNamespace("statistics/sort_up");
   private static final Identifier SORT_DOWN_SPRITE = Identifier.withDefaultNamespace("statistics/sort_down");
   private static final Component PENDING_TEXT = Component.translatable("multiplayer.downloadingStats");
   private static final Component NO_VALUE_DISPLAY = Component.translatable("stats.none");
   private static final Component GENERAL_BUTTON = Component.translatable("stat.generalButton");
   private static final Component ITEMS_BUTTON = Component.translatable("stat.itemsButton");
   private static final Component MOBS_BUTTON = Component.translatable("stat.mobsButton");
   protected final Screen lastScreen;
   private static final int LIST_WIDTH = 280;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private final TabManager tabManager = new TabManager(x$0 -> this.addRenderableWidget(x$0), x$0 -> this.removeWidget(x$0));
   @Nullable
   private MenuTabBar tabNavigationBar;
   private final StatsCounter stats;
   private boolean isLoading = true;

   public StatsScreen(final Screen lastScreen, final StatsCounter stats) {
      super(TITLE);
      this.lastScreen = lastScreen;
      this.stats = stats;
   }

   @Override
   protected void init() {
      Component loadingTitle = PENDING_TEXT;
      this.tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width)
         .addTabs(
            new LoadingTab(this.getFont(), GENERAL_BUTTON, loadingTitle),
            new LoadingTab(this.getFont(), ITEMS_BUTTON, loadingTitle),
            new LoadingTab(this.getFont(), MOBS_BUTTON, loadingTitle)
         )
         .build();
      this.addRenderableWidget(this.tabNavigationBar);
      this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
      this.tabNavigationBar.setTabActiveState(0, true);
      this.tabNavigationBar.setTabActiveState(1, false);
      this.tabNavigationBar.setTabActiveState(2, false);
      this.layout.visitWidgets(button -> {
         button.setTabOrderGroup(1);
         this.addRenderableWidget(button);
      });
      this.tabNavigationBar.selectTab(0, false);
      this.repositionElements();
      this.minecraft.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket$Action.REQUEST_STATS));
   }

   public void onStatsUpdated() {
      if (this.isLoading) {
         if (this.tabNavigationBar != null) {
            this.removeWidget(this.tabNavigationBar);
         }

         this.tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width)
            .addTabs(
               new StatsScreen$StatisticsTab(this, GENERAL_BUTTON, new StatsScreen$GeneralStatisticsList(this, this.minecraft)),
               new StatsScreen$StatisticsTab(this, ITEMS_BUTTON, new StatsScreen$ItemStatisticsList(this, this.minecraft)),
               new StatsScreen$StatisticsTab(this, MOBS_BUTTON, new StatsScreen$MobsStatisticsList(this, this.minecraft))
            )
            .build();
         this.setFocused(this.tabNavigationBar);
         this.addRenderableWidget(this.tabNavigationBar);
         this.setTabActiveStateAndTooltip(1);
         this.setTabActiveStateAndTooltip(2);
         this.tabNavigationBar.selectTab(0, false);
         this.repositionElements();
         this.isLoading = false;
      }
   }

   private void setTabActiveStateAndTooltip(final int index) {
      if (this.tabNavigationBar != null) {
         boolean active = this.tabNavigationBar.getTabs().get(index) instanceof StatsScreen$StatisticsTab statsTab && !statsTab.list.children().isEmpty();
         this.tabNavigationBar.setTabActiveState(index, active);
         if (active) {
            this.tabNavigationBar.setTabTooltip(index, null);
         } else {
            this.tabNavigationBar.setTabTooltip(index, Tooltip.create(Component.translatable("gui.stats.none_found")));
         }
      }
   }

   @Override
   protected void repositionElements() {
      if (this.tabNavigationBar != null) {
         this.tabNavigationBar.arrangeElements(this.width);
         int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
         ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
         this.tabNavigationBar.getTabs().forEach(tab -> tab.visitChildren(child -> child.setHeight(tabArea.height())));
         this.tabManager.setTabArea(tabArea);
         this.layout.setHeaderHeight(tabAreaTop);
         this.layout.arrangeElements();
      }
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      return this.tabNavigationBar != null && this.tabNavigationBar.keyPressed(event) ? true : super.keyPressed(event);
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int xm, final int ym, final float a) {
      super.extractRenderState(graphics, xm, ym, a);
      graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight(), 0.0F, 0.0F, this.width, 2, 32, 2);
   }

   @Override
   protected void extractMenuBackground(final GuiGraphicsExtractor graphics) {
      graphics.blit(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
      this.extractMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.lastScreen);
   }

   private static String getTranslationKey(final Stat<Identifier> stat) {
      return "stat." + stat.getValue().toString().replace(':', '.');
   }
}
