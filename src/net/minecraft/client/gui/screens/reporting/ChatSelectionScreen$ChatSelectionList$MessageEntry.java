package net.minecraft.client.gui.screens.reporting;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.multiplayer.chat.GuiMessageTag$Icon;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ChatSelectionScreen$ChatSelectionList$MessageEntry extends ChatSelectionScreen$ChatSelectionList$Entry {
   private static final int CHECKMARK_WIDTH = 9;
   private static final int CHECKMARK_HEIGHT = 8;
   private static final int INDENT_AMOUNT = 11;
   private static final int TAG_MARGIN_LEFT = 4;
   private final int chatId;
   private final FormattedText text;
   private final Component narration;
   @Nullable
   private final List<FormattedCharSequence> hoverText;
   @Nullable
   private final GuiMessageTag$Icon tagIcon;
   @Nullable
   private final List<FormattedCharSequence> tagHoverText;
   private final boolean canReport;
   private final boolean playerMessage;

   public ChatSelectionScreen$ChatSelectionList$MessageEntry(
      final ChatSelectionScreen$ChatSelectionList this$1,
      final int chatId,
      final Component text,
      @Nullable final Component narration,
      final GuiMessageTag tag,
      final boolean canReport,
      final boolean playerMessage
   ) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.chatId = chatId;
      this.tagIcon = Optionull.map(tag, GuiMessageTag::icon);
      this.tagHoverText = tag != null && tag.text() != null ? ChatSelectionScreen.access$100(this$1.this$0).split(tag.text(), this$1.getRowWidth()) : null;
      this.canReport = canReport;
      this.playerMessage = playerMessage;
      FormattedText shortText = ChatSelectionScreen.access$300(this$1.this$0)
         .substrByWidth(text, this.getMaximumTextWidth() - ChatSelectionScreen.access$200(this$1.this$0).width(CommonComponents.ELLIPSIS));
      if (text != shortText) {
         this.text = FormattedText.composite(shortText, CommonComponents.ELLIPSIS);
         this.hoverText = ChatSelectionScreen.access$400(this$1.this$0).split(text, this$1.getRowWidth());
      } else {
         this.text = text;
         this.hoverText = null;
      }

      this.narration = narration;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      if (this.isSelected() && this.canReport) {
         this.extractSelectedCheckmark(graphics, this.getContentY(), this.getContentX(), this.getContentHeight());
      }

      int textX = this.getContentX() + this.getTextIndent();
      int textY = this.getContentY() + 1 + (this.getContentHeight() - 9) / 2;
      graphics.text(
         ChatSelectionScreen.access$600(this.this$1.this$0), Language.getInstance().getVisualOrder(this.text), textX, textY, this.canReport ? -1 : -1593835521
      );
      if (this.hoverText != null && hovered) {
         graphics.setTooltipForNextFrame(this.hoverText, mouseX, mouseY);
      }

      int textWidth = ChatSelectionScreen.access$700(this.this$1.this$0).width(this.text);
      this.extractTag(graphics, textX + textWidth + 4, this.getContentY(), this.getContentHeight(), mouseX, mouseY);
   }

   private void extractTag(final GuiGraphicsExtractor graphics, final int iconLeft, final int rowTop, final int rowHeight, final int mouseX, final int mouseY) {
      if (this.tagIcon != null) {
         int iconTop = rowTop + (rowHeight - this.tagIcon.height) / 2;
         this.tagIcon.extractRenderState(graphics, iconLeft, iconTop);
         if (this.tagHoverText != null
            && mouseX >= iconLeft
            && mouseX <= iconLeft + this.tagIcon.width
            && mouseY >= iconTop
            && mouseY <= iconTop + this.tagIcon.height) {
            graphics.setTooltipForNextFrame(this.tagHoverText, mouseX, mouseY);
         }
      }
   }

   private void extractSelectedCheckmark(final GuiGraphicsExtractor graphics, final int rowTop, final int rowLeft, final int rowHeight) {
      int top = rowTop + (rowHeight - 8) / 2;
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ChatSelectionScreen.CHECKMARK_SPRITE, rowLeft, top, 9, 8);
   }

   private int getMaximumTextWidth() {
      int tagMargin = this.tagIcon != null ? this.tagIcon.width + 4 : 0;
      return this.this$1.getRowWidth() - this.getTextIndent() - 4 - tagMargin;
   }

   private int getTextIndent() {
      return this.playerMessage ? 11 : 0;
   }

   @Override
   public Component getNarration() {
      return (Component)(this.isSelected() ? Component.translatable("narrator.select", this.narration) : this.narration);
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.this$1.setSelected(null);
      return this.toggleReport();
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      return event.isSelection() ? this.toggleReport() : false;
   }

   @Override
   public boolean isSelected() {
      return this.this$1.this$0.report.isReported(this.chatId);
   }

   @Override
   public boolean canSelect() {
      return true;
   }

   @Override
   public boolean canReport() {
      return this.canReport;
   }

   private boolean toggleReport() {
      if (this.canReport) {
         this.this$1.this$0.report.toggleReported(this.chatId);
         this.this$1.this$0.updateConfirmSelectedButton();
         return true;
      } else {
         return false;
      }
   }
}
