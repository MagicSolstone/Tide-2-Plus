package net.minecraft.client.gui.screens.reporting;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class AbstractReportScreen$DiscardReportWarningScreen extends WarningScreen {
   private static final Component TITLE = Component.translatable("gui.abuseReport.discard.title").withStyle(ChatFormatting.BOLD);
   private static final Component MESSAGE = Component.translatable("gui.abuseReport.discard.content");
   private static final Component RETURN = Component.translatable("gui.abuseReport.discard.return");
   private static final Component DRAFT = Component.translatable("gui.abuseReport.discard.draft");
   private static final Component DISCARD = Component.translatable("gui.abuseReport.discard.discard");

   protected AbstractReportScreen$DiscardReportWarningScreen(final AbstractReportScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(TITLE, MESSAGE, MESSAGE);
   }

   @Override
   protected Layout addFooterButtons() {
      LinearLayout footer = LinearLayout.vertical().spacing(8);
      footer.defaultCellSetting().alignHorizontallyCenter();
      LinearLayout firstFooterRow = footer.addChild(LinearLayout.horizontal().spacing(8));
      firstFooterRow.addChild(Button.builder(RETURN, button -> this.onClose()).build());
      firstFooterRow.addChild(Button.builder(DRAFT, button -> {
         this.this$0.saveDraft();
         this.minecraft.gui.setScreen(this.this$0.lastScreen);
      }).build());
      footer.addChild(Button.builder(DISCARD, button -> {
         this.this$0.clearDraft();
         this.minecraft.gui.setScreen(this.this$0.lastScreen);
      }).build());
      return footer;
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.this$0);
   }

   @Override
   public boolean shouldCloseOnEsc() {
      return false;
   }
}
