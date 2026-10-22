package net.minecraft.client.gui.screens.achievement;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class StatsScreen$StatisticsTab extends GridLayoutTab {
   protected final AbstractSelectionList<?> list;

   public StatsScreen$StatisticsTab(final Component this$0, final AbstractSelectionList<?> title, final AbstractSelectionList list) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(title);
      this.layout.addChild(list, 1, 1);
      this.list = list;
   }

   @Override
   public void doLayout(final ScreenRectangle screenRectangle) {
      this.list.updateSizeAndPosition(this.this$0.width, this.this$0.layout.getContentHeight(), this.this$0.layout.getHeaderHeight());
      super.doLayout(screenRectangle);
   }
}
