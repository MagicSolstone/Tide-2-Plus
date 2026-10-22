package net.minecraft.client.gui.screens.reporting;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record ChatSelectionScreen$ChatSelectionList$Heading(UUID sender, ChatSelectionScreen$ChatSelectionList$Entry entry) {
   public boolean canCombine(final ChatSelectionScreen$ChatSelectionList$Heading other) {
      return other.sender.equals(this.sender);
   }
}
