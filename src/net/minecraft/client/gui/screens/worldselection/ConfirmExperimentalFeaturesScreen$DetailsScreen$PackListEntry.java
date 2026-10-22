package net.minecraft.client.gui.screens.worldselection;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class ConfirmExperimentalFeaturesScreen$DetailsScreen$PackListEntry
   extends ObjectSelectionList$Entry<ConfirmExperimentalFeaturesScreen$DetailsScreen$PackListEntry> {
   private final Component packId;
   private final Component message;
   private final MultiLineLabel splitMessage;

   private ConfirmExperimentalFeaturesScreen$DetailsScreen$PackListEntry(
      final ConfirmExperimentalFeaturesScreen$DetailsScreen this$1, final Component packId, final Component message, final MultiLineLabel splitMessage
   ) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.packId = packId;
      this.message = message;
      this.splitMessage = splitMessage;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      ActiveTextCollector textRenderer = graphics.textRenderer();
      graphics.text(ConfirmExperimentalFeaturesScreen$DetailsScreen.access$100(this.this$1).font, this.packId, this.getContentX(), this.getContentY(), -1);
      this.splitMessage.visitLines(TextAlignment.LEFT, this.getContentX(), this.getContentY() + 12, 9, textRenderer);
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", CommonComponents.joinForNarration(this.packId, this.message));
   }
}
