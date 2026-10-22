package net.minecraft.client.gui.screens.reporting;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage$Player;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ChatSelectionScreen$ChatSelectionList
   extends ObjectSelectionList<ChatSelectionScreen$ChatSelectionList$Entry>
   implements ChatSelectionLogFiller$Output {
   public static final int ITEM_HEIGHT = 16;
   @Nullable
   private ChatSelectionScreen$ChatSelectionList$Heading previousHeading;

   public ChatSelectionScreen$ChatSelectionList(final ChatSelectionScreen this$0, final Minecraft minecraft, final int upperMargin) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(minecraft, this$0.width, this$0.height - upperMargin - 80, 40, 16);
   }

   @Override
   public void setScrollAmount(final double scrollAmount) {
      double prevScrollAmount = this.scrollAmount();
      super.setScrollAmount(scrollAmount);
      if (this.maxScrollAmount() > 1.0E-5F && scrollAmount <= 1.0E-5F && !Mth.equal(scrollAmount, prevScrollAmount)) {
         this.this$0.onReachedScrollTop();
      }
   }

   @Override
   public void acceptMessage(final int id, final LoggedChatMessage$Player message) {
      boolean canReport = message.canReport(this.this$0.report.reportedProfileId());
      ChatTrustLevel trustLevel = message.trustLevel();
      GuiMessageTag tag = trustLevel.createTag(message.message());
      ChatSelectionScreen$ChatSelectionList$Entry entry = new ChatSelectionScreen$ChatSelectionList$MessageEntry(
         this, id, message.toContentComponent(), message.toNarrationComponent(), tag, canReport, true
      );
      this.addEntryToTop(entry);
      this.updateHeading(message, canReport);
   }

   private void updateHeading(final LoggedChatMessage$Player message, final boolean canReport) {
      ChatSelectionScreen$ChatSelectionList$Entry entry = new ChatSelectionScreen$ChatSelectionList$MessageHeadingEntry(
         this, message.profile(), message.toHeadingComponent(), canReport
      );
      this.addEntryToTop(entry);
      ChatSelectionScreen$ChatSelectionList$Heading heading = new ChatSelectionScreen$ChatSelectionList$Heading(message.profileId(), entry);
      if (this.previousHeading != null && this.previousHeading.canCombine(heading)) {
         this.removeEntryFromTop(this.previousHeading.entry());
      }

      this.previousHeading = heading;
   }

   @Override
   public void acceptDivider(final Component text) {
      this.addEntryToTop(new ChatSelectionScreen$ChatSelectionList$PaddingEntry());
      this.addEntryToTop(new ChatSelectionScreen$ChatSelectionList$DividerEntry(this, text));
      this.addEntryToTop(new ChatSelectionScreen$ChatSelectionList$PaddingEntry());
      this.previousHeading = null;
   }

   @Override
   public int getRowWidth() {
      return Math.min(350, this.width - 50);
   }

   public int getMaxVisibleEntries() {
      return Mth.positiveCeilDiv(this.height, 16);
   }

   protected void extractItem(
      final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a, final ChatSelectionScreen$ChatSelectionList$Entry entry
   ) {
      if (this.shouldHighlightEntry(entry)) {
         boolean selected = this.getSelected() == entry;
         int outlineColor = this.isFocused() && selected ? -1 : -8355712;
         this.extractSelection(graphics, entry, outlineColor);
      }

      entry.extractContent(graphics, mouseX, mouseY, this.getHovered() == entry, a);
   }

   private boolean shouldHighlightEntry(final ChatSelectionScreen$ChatSelectionList$Entry entry) {
      if (entry.canSelect()) {
         boolean entrySelected = this.getSelected() == entry;
         boolean nothingSelected = this.getSelected() == null;
         boolean entryHovered = this.getHovered() == entry;
         return entrySelected || nothingSelected && entryHovered && entry.canReport();
      } else {
         return false;
      }
   }

   @Nullable
   protected ChatSelectionScreen$ChatSelectionList$Entry nextEntry(final ScreenDirection dir) {
      return this.nextEntry(dir, ChatSelectionScreen$ChatSelectionList$Entry::canSelect);
   }

   public void setSelected(@Nullable final ChatSelectionScreen$ChatSelectionList$Entry selected) {
      super.setSelected(selected);
      ChatSelectionScreen$ChatSelectionList$Entry entry = this.nextEntry(ScreenDirection.UP);
      if (entry == null) {
         this.this$0.onReachedScrollTop();
      }
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      ChatSelectionScreen$ChatSelectionList$Entry selected = this.getSelected();
      return selected != null && selected.keyPressed(event) ? true : super.keyPressed(event);
   }

   public int getFooterTop() {
      return this.getBottom() + 9;
   }
}
