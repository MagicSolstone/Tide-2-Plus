package net.minecraft.client.gui.screens.reporting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public abstract class ChatSelectionScreen$ChatSelectionList$Entry extends ObjectSelectionList$Entry<ChatSelectionScreen$ChatSelectionList$Entry> {
   @Override
   public Component getNarration() {
      return CommonComponents.EMPTY;
   }

   public boolean isSelected() {
      return false;
   }

   public boolean canSelect() {
      return false;
   }

   public boolean canReport() {
      return this.canSelect();
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      return this.canSelect();
   }
}
