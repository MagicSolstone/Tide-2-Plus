package net.minecraft.client.gui.screens.reporting;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.chat.report.ReportReason;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ReportReasonSelectionScreen$ReasonSelectionList$Entry extends ObjectSelectionList$Entry<ReportReasonSelectionScreen$ReasonSelectionList$Entry> {
   private final ReportReason reason;

   public ReportReasonSelectionScreen$ReasonSelectionList$Entry(final ReportReasonSelectionScreen$ReasonSelectionList this$1, final ReportReason reason) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.reason = reason;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int textX = this.getContentX() + 1;
      int textY = this.getContentY() + (this.getContentHeight() - 9) / 2 + 1;
      graphics.text(ReportReasonSelectionScreen.access$100(this.this$1.this$0), this.reason.title(), textX, textY, -1);
   }

   @Override
   public Component getNarration() {
      return Component.translatable("gui.abuseReport.reason.narration", this.reason.title(), this.reason.description());
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.this$1.setSelected(this);
      return super.mouseClicked(event, doubleClick);
   }

   public ReportReason getReason() {
      return this.reason;
   }
}
