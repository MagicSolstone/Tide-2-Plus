package net.minecraft.client.gui.components.tabs;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MenuTabBar$Builder extends TabNavigationBar$Builder {
   private MenuTabBar$Builder(final TabManager tabManager, final int width) {
      super(tabManager, 0, 0, width, 24);
   }

   public MenuTabBar$Builder addTab(final Tab tab) {
      super.addTab(new MenuTabBar$MenuTabButton(this.tabManager, tab, 0, this.height), tab);
      return this;
   }

   public MenuTabBar$Builder addTabs(final Tab... tabs) {
      for (Tab tab : tabs) {
         this.addTab(tab);
      }

      return this;
   }

   public MenuTabBar build() {
      return new MenuTabBar(this.x, this.y, this.width, this.height, this.tabManager, ImmutableList.copyOf(this.tabButtons), ImmutableList.copyOf(this.tabs));
   }
}
