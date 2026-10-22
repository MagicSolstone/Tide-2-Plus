package net.minecraft.client.gui.screens.reporting;

import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
import net.minecraft.client.multiplayer.chat.report.ChatReport$Builder;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ChatSelectionScreen extends Screen {
   private static final Identifier CHECKMARK_SPRITE = Identifier.withDefaultNamespace("icon/checkmark");
   private static final Component TITLE = Component.translatable("gui.chatSelection.title");
   private static final Component CONTEXT_INFO = Component.translatable("gui.chatSelection.context");
   @Nullable
   private final Screen lastScreen;
   private final ReportingContext reportingContext;
   private Button confirmSelectedButton;
   private MultiLineLabel contextInfoLabel;
   private ChatSelectionScreen$ChatSelectionList chatSelectionList;
   private final ChatReport$Builder report;
   private final Consumer<ChatReport$Builder> onSelected;
   private ChatSelectionLogFiller chatLogFiller;

   public ChatSelectionScreen(
      @Nullable final Screen lastScreen,
      final ReportingContext reportingContext,
      final ChatReport$Builder report,
      final Consumer<ChatReport$Builder> onSelected
   ) {
      super(TITLE);
      this.lastScreen = lastScreen;
      this.reportingContext = reportingContext;
      this.report = report.copy();
      this.onSelected = onSelected;
   }

   @Override
   protected void init() {
      this.chatLogFiller = new ChatSelectionLogFiller(this.reportingContext, this::canReport);
      this.contextInfoLabel = MultiLineLabel.create(this.font, CONTEXT_INFO, this.width - 16);
      this.chatSelectionList = this.addRenderableWidget(
         new ChatSelectionScreen$ChatSelectionList(this, this.minecraft, (this.contextInfoLabel.getLineCount() + 1) * 9)
      );
      this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, b -> this.onClose()).bounds(this.width / 2 - 155, this.height - 32, 150, 20).build());
      this.confirmSelectedButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> {
         this.onSelected.accept(this.report);
         this.onClose();
      }).bounds(this.width / 2 - 155 + 160, this.height - 32, 150, 20).build());
      this.updateConfirmSelectedButton();
      this.extendLog();
      this.chatSelectionList.setScrollAmount(this.chatSelectionList.maxScrollAmount());
   }

   private boolean canReport(final LoggedChatMessage message) {
      return message.canReport(this.report.reportedProfileId());
   }

   private void extendLog() {
      int pageSize = this.chatSelectionList.getMaxVisibleEntries();
      this.chatLogFiller.fillNextPage(pageSize, this.chatSelectionList);
   }

   private void onReachedScrollTop() {
      this.extendLog();
   }

   private void updateConfirmSelectedButton() {
      this.confirmSelectedButton.active = !this.report.reportedMessages().isEmpty();
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      ActiveTextCollector textRenderer = graphics.textRenderer();
      graphics.centeredText(this.font, this.title, this.width / 2, 10, -1);
      AbuseReportLimits reportLimits = this.reportingContext.sender().reportLimits();
      int messageCount = this.report.reportedMessages().size();
      int maxMessageCount = reportLimits.maxReportedMessageCount();
      Component selectedText = Component.translatable("gui.chatSelection.selected", messageCount, maxMessageCount);
      graphics.centeredText(this.font, selectedText, this.width / 2, 26, -1);
      int topY = this.chatSelectionList.getFooterTop();
      this.contextInfoLabel.visitLines(TextAlignment.CENTER, this.width / 2, topY, 9, textRenderer);
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.lastScreen);
   }

   @Override
   public Component getNarrationMessage() {
      return CommonComponents.joinForNarration(super.getNarrationMessage(), CONTEXT_INFO);
   }
}
