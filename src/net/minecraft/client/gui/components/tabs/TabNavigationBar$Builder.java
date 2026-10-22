package net.minecraft.client.gui.components.tabs;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.TabButton;

@Environment(EnvType.CLIENT)
public class TabNavigationBar$Builder {
   protected final int x;
   protected final int y;
   protected final int width;
   protected final int height;
   protected final TabManager tabManager;
   protected final List<TabButton> tabButtons = new ArrayList<>();
   protected final List<Tab> tabs = new ArrayList<>();

   protected TabNavigationBar$Builder(final TabManager tabManager, final int x, final int y, final int width, final int height) {
      this.tabManager = tabManager;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public TabNavigationBar$Builder addTab(final TabButton button, final Tab tab) {
      this.tabButtons.add(button);
      this.tabs.add(tab);
      return this;
   }

   public TabNavigationBar build() {
      return new TabNavigationBar(
         this.x, this.y, this.width, this.height, this.tabManager, ImmutableList.copyOf(this.tabButtons), ImmutableList.copyOf(this.tabs)
      );
   }
}
