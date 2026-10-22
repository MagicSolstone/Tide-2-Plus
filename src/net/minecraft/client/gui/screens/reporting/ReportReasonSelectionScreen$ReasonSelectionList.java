package net.minecraft.client.gui.screens.reporting;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.multiplayer.chat.report.ReportReason;

@Environment(EnvType.CLIENT)
public class ReportReasonSelectionScreen$ReasonSelectionList extends ObjectSelectionList<ReportReasonSelectionScreen$ReasonSelectionList$Entry> {
   public ReportReasonSelectionScreen$ReasonSelectionList(final ReportReasonSelectionScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(minecraft, this$0.width, this$0.listHeight(), this$0.layout.getHeaderHeight(), 18);

      for (ReportReason reason : ReportReason.values()) {
         if (!ReportReason.getIncompatibleCategories(this$0.reportType).contains(reason)) {
            this.addEntry(new ReportReasonSelectionScreen$ReasonSelectionList$Entry(this, reason));
         }
      }
   }

   public ReportReasonSelectionScreen$ReasonSelectionList$Entry findEntry(final ReportReason reason) {
      return this.children().stream().filter(entry -> entry.reason == reason).findFirst().orElse(null);
   }

   @Override
   public int getRowWidth() {
      return 320;
   }

   public void setSelected(final ReportReasonSelectionScreen$ReasonSelectionList$Entry selected) {
      super.setSelected(selected);
      this.this$0.currentlySelectedReason = selected != null ? selected.getReason() : null;
   }
}
