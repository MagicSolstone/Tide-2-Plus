package net.minecraft.client.gui.screens.options.controls;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping$Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

@Environment(EnvType.CLIENT)
public class KeyBindsList extends ContainerObjectSelectionList<KeyBindsList$Entry> {
   private static final int ITEM_HEIGHT = 20;
   private final KeyBindsScreen keyBindsScreen;
   private int maxNameWidth;

   public KeyBindsList(final KeyBindsScreen keyBindsScreen, final Minecraft minecraft) {
      super(minecraft, keyBindsScreen.width, keyBindsScreen.layout.getContentHeight(), keyBindsScreen.layout.getHeaderHeight(), 20);
      this.keyBindsScreen = keyBindsScreen;
      KeyMapping[] keyMappings = (KeyMapping[])ArrayUtils.clone(minecraft.options.keyMappings);
      Arrays.sort((Object[])keyMappings);
      KeyMapping$Category previousCategory = null;

      for (KeyMapping key : keyMappings) {
         KeyMapping$Category category = key.getCategory();
         if (category != previousCategory) {
            previousCategory = category;
            this.addEntry(new KeyBindsList$CategoryEntry(this, category));
         }

         Component name = Component.translatable(key.getName());
         int width = minecraft.font.width(name);
         if (width > this.maxNameWidth) {
            this.maxNameWidth = width;
         }

         this.addEntry(new KeyBindsList$KeyEntry(this, key, name));
      }
   }

   public void resetMappingAndUpdateButtons() {
      KeyMapping.resetMapping();
      this.refreshEntries();
   }

   public void refreshEntries() {
      this.children().forEach(KeyBindsList$Entry::refreshEntry);
   }

   @Override
   public int getRowWidth() {
      return 340;
   }
}
