package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.client.CommandHistory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ChatScreen$ChatConstructor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessage$Line;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.ClickEvent$Custom;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent$ShowText;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.ArrayListDeque;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ChatComponent {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int MAX_CHAT_HISTORY = 100;
   private static final int MESSAGE_INDENT = 4;
   private static final int BOTTOM_MARGIN = 40;
   private static final int TOOLTIP_MAX_WIDTH = 210;
   private static final int TIME_BEFORE_MESSAGE_DELETION = 60;
   private static final Component DELETED_CHAT_MESSAGE = Component.translatable("chat.deleted_marker").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
   public static final int MESSAGE_BOTTOM_TO_MESSAGE_TOP = 8;
   public static final Identifier QUEUE_EXPAND_ID = Identifier.withDefaultNamespace("internal/expand_chat_queue");
   private static final Style QUEUE_EXPAND_TEXT_STYLE = Style.EMPTY
      .withClickEvent(new ClickEvent$Custom(QUEUE_EXPAND_ID, Optional.empty()))
      .withHoverEvent(new HoverEvent$ShowText(Component.translatable("chat.queue.tooltip")));
   public static final Identifier GO_TO_RESTRICTIONS_SCREEN = Identifier.withDefaultNamespace("internal/go_to_restrictions_screen");
   private static final Component RESTRICTED_CHAT_MESSAGE = Component.translatable("chat_screen.restricted")
      .withStyle(
         Style.EMPTY.withColor(ChatFormatting.RED).withUnderlined(true).withClickEvent(new ClickEvent$Custom(GO_TO_RESTRICTIONS_SCREEN, Optional.empty()))
      );
   private static final Component RESTRICTED_CHAT_MESSAGE_WITH_HOVER = ComponentUtils.mergeStyles(
      RESTRICTED_CHAT_MESSAGE, Style.EMPTY.withHoverEvent(new HoverEvent$ShowText(Component.translatable("chat_screen.restricted")))
   );
   private final Minecraft minecraft;
   private final CommandHistory commandHistory;
   private final ArrayListDeque<String> recentChat = new ArrayListDeque<>(100);
   private final List<GuiMessage> allMessages = Lists.newArrayList();
   private final List<GuiMessage$Line> trimmedMessages = Lists.newArrayList();
   private int chatScrollbarPos;
   private boolean newMessageSinceScroll;
   @Nullable
   private ChatComponent$Draft latestDraft;
   @Nullable
   private ChatScreen preservedScreen;
   private final List<ChatComponent$DelayedMessageDeletion> messageDeletionQueue = new ArrayList<>();
   private Predicate<GuiMessage> visibleMessageFilter = var0 -> true;

   public ChatComponent(final Minecraft minecraft) {
      this.minecraft = minecraft;
      this.commandHistory = new CommandHistory(minecraft.gameDirectory.toPath());
      this.recentChat.addAll(this.commandHistory.history());
   }

   public void tick() {
      if (!this.messageDeletionQueue.isEmpty()) {
         this.processMessageDeletionQueue();
      }
   }

   public void setVisibleMessageFilter(final Predicate<GuiMessage> visibleMessageFilter) {
      this.visibleMessageFilter = visibleMessageFilter;
      this.refreshTrimmedMessages();
   }

   private int forEachLine(final ChatComponent$AlphaCalculator alphaCalculator, final ChatComponent$LineConsumer lineConsumer) {
      int perPage = this.getLinesPerPage();
      int count = 0;

      for (int i = Math.min(this.trimmedMessages.size() - this.chatScrollbarPos, perPage) - 1; i >= 0; i--) {
         int messageIndex = i + this.chatScrollbarPos;
         GuiMessage$Line message = this.trimmedMessages.get(messageIndex);
         float alpha = alphaCalculator.calculate(message);
         if (alpha > 1.0E-5F) {
            count++;
            lineConsumer.accept(message, i, alpha);
         }
      }

      return count;
   }

   public void extractRenderState(
      final GuiGraphicsExtractor graphics,
      final Font font,
      final int ticks,
      final int mouseX,
      final int mouseY,
      final ChatComponent$DisplayMode displayMode,
      final boolean changeCursorOnInsertions
   ) {
      graphics.pose().pushMatrix();
      this.extractRenderState(
         (ChatComponent$ChatGraphicsAccess)(displayMode.foreground
            ? new ChatComponent$DrawingFocusedGraphicsAccess(graphics, font, mouseX, mouseY, changeCursorOnInsertions)
            : new ChatComponent$DrawingBackgroundGraphicsAccess(graphics)),
         graphics.guiHeight(),
         ticks,
         displayMode
      );
      graphics.pose().popMatrix();
   }

   public void captureClickableText(
      final ActiveTextCollector activeTextCollector, final int screenHeight, final int ticks, final ChatComponent$DisplayMode displayMode
   ) {
      this.extractRenderState(new ChatComponent$ClickableTextOnlyGraphicsAccess(activeTextCollector), screenHeight, ticks, displayMode);
   }

   private void extractRenderState(
      final ChatComponent$ChatGraphicsAccess graphics, final int screenHeight, final int ticks, final ChatComponent$DisplayMode displayMode
   ) {
      boolean isForeground = displayMode.foreground;
      boolean isRestricted = displayMode.showRestrictedPrompt;
      int total = this.trimmedMessages.size();
      if (total > 0 || isRestricted) {
         ProfilerFiller profiler = Profiler.get();
         profiler.push("chat");
         float scale = (float)this.getScale();
         int maxWidth = Mth.ceil(this.getWidth() / scale);
         int chatBottom = Mth.floor((screenHeight - 40) / scale);
         float textOpacity = this.minecraft.options.chatOpacity().get().floatValue() * 0.9F + 0.1F;
         float backgroundOpacity = this.minecraft.options.textBackgroundOpacity().get().floatValue();
         int messageHeight = 9;
         int messageBottomToMessageTop = 8;
         double chatLineSpacing = this.minecraft.options.chatLineSpacing().get();
         int entryHeight = (int)(messageHeight * (chatLineSpacing + 1.0));
         int entryBottomToMessageY = (int)Math.round(8.0 * (chatLineSpacing + 1.0) - 4.0 * chatLineSpacing);
         long queueSize = this.minecraft.gui.chatListener().queueSize();
         ChatComponent$AlphaCalculator alphaCalculator = isForeground
            ? ChatComponent$AlphaCalculator.FULLY_VISIBLE
            : ChatComponent$AlphaCalculator.timeBased(ticks);
         graphics.updatePose(pose -> {
            pose.scale(scale, scale);
            pose.translate(4.0F, 0.0F);
         });
         int count = this.forEachLine(alphaCalculator, (var5x, lineIndex, alphax) -> {
            int entryBottom = chatBottom - lineIndex * entryHeight;
            int entryTop = entryBottom - entryHeight;
            graphics.fill(-4, entryTop, maxWidth + 4 + 4, entryBottom, ARGB.black(alphax * backgroundOpacity));
         });
         int lineAboveMessagesY = chatBottom - (count + 1) * entryHeight;
         if (queueSize > 0L) {
            graphics.fill(-2, chatBottom, maxWidth + 4, chatBottom + messageHeight, ARGB.black(backgroundOpacity));
         }

         if (isRestricted) {
            graphics.fill(-2, lineAboveMessagesY, maxWidth + 4 + 4, lineAboveMessagesY + entryHeight, ARGB.black(backgroundOpacity));
         }

         this.forEachLine(alphaCalculator, new ChatComponent$1(this, chatBottom, entryHeight, entryBottomToMessageY, graphics, textOpacity, messageHeight));
         if (queueSize > 0L) {
            int queueLineBottom = chatBottom + messageHeight;
            Component queueMessage = Component.translatable("chat.queue", queueSize).setStyle(QUEUE_EXPAND_TEXT_STYLE);
            graphics.handleMessage(queueLineBottom - 8, 0.5F * textOpacity, queueMessage.getVisualOrderText());
         }

         if (isRestricted) {
            int restrictedMessageWidth = this.minecraft.font.width(RESTRICTED_CHAT_MESSAGE);
            FormattedCharSequence restrictedMessage = restrictedMessageWidth > maxWidth
               ? ComponentRenderUtils.clipText(RESTRICTED_CHAT_MESSAGE_WITH_HOVER, this.minecraft.font, maxWidth)
               : RESTRICTED_CHAT_MESSAGE.getVisualOrderText();
            graphics.handleMessage(lineAboveMessagesY + entryHeight - entryBottomToMessageY - 1, textOpacity, restrictedMessage);
         }

         if (total > 0 && isForeground) {
            int chatHeight = count * entryHeight;
            int virtualHeight = total * entryHeight;
            int y = this.chatScrollbarPos * chatHeight / total - chatBottom;
            int height = chatHeight * chatHeight / virtualHeight;
            if (virtualHeight != chatHeight) {
               int alpha = y > 0 ? 170 : 96;
               int color = this.newMessageSinceScroll ? 13382451 : 3355562;
               int scrollBarStartX = maxWidth + 4;
               graphics.fill(scrollBarStartX, -y, scrollBarStartX + 2, -y - height, ARGB.color(alpha, color));
               graphics.fill(scrollBarStartX + 2, -y, scrollBarStartX + 1, -y - height, ARGB.color(alpha, 13421772));
            }
         }

         profiler.pop();
      }
   }

   public void clearMessages(final boolean history) {
      this.minecraft.gui.chatListener().flushQueue();
      this.messageDeletionQueue.clear();
      this.trimmedMessages.clear();
      this.allMessages.clear();
      if (history) {
         this.recentChat.clear();
         this.recentChat.addAll(this.commandHistory.history());
      }
   }

   public void addClientSystemMessage(final Component message) {
      this.addMessage(message, null, GuiMessageSource.SYSTEM_CLIENT, GuiMessageTag.systemSinglePlayer());
   }

   public void addServerSystemMessage(final Component message) {
      this.addMessage(message, null, GuiMessageSource.SYSTEM_SERVER, GuiMessageTag.systemSinglePlayer());
   }

   public void addPlayerMessage(final Component message, @Nullable final MessageSignature signature, @Nullable final GuiMessageTag tag) {
      this.addMessage(message, signature, GuiMessageSource.PLAYER, tag);
   }

   private void addMessage(
      final Component contents, @Nullable final MessageSignature signature, final GuiMessageSource source, @Nullable final GuiMessageTag tag
   ) {
      GuiMessage message = new GuiMessage(this.minecraft.gui.hud.getGuiTicks(), contents, signature, source, tag);
      if (this.visibleMessageFilter.test(message)) {
         this.logChatMessage(message);
         this.addMessageToDisplayQueue(message);
         this.addMessageToQueue(message);
      }
   }

   private void logChatMessage(final GuiMessage message) {
      String messageString = message.content().getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
      String logTag = Optionull.map(message.tag(), GuiMessageTag::logTag);
      if (logTag != null) {
         LOGGER.info("[{}] [CHAT] {}", logTag, messageString);
      } else {
         LOGGER.info("[CHAT] {}", messageString);
      }
   }

   private void addMessageToDisplayQueue(final GuiMessage message) {
      int maxWidth = Mth.floor(this.getWidth() / this.getScale());
      List<FormattedCharSequence> lines = message.splitLines(this.minecraft.font, maxWidth);
      boolean chatting = this.isChatFocused();

      for (int i = 0; i < lines.size(); i++) {
         FormattedCharSequence line = lines.get(i);
         if (chatting && this.chatScrollbarPos > 0) {
            this.newMessageSinceScroll = true;
            this.scrollChat(1);
         }

         boolean endOfEntry = i == lines.size() - 1;
         this.trimmedMessages.addFirst(new GuiMessage$Line(message, line, endOfEntry));
      }

      while (this.trimmedMessages.size() > 100) {
         this.trimmedMessages.removeLast();
      }
   }

   private void addMessageToQueue(final GuiMessage message) {
      this.allMessages.addFirst(message);

      while (this.allMessages.size() > 100) {
         this.allMessages.removeLast();
      }
   }

   private void processMessageDeletionQueue() {
      int time = this.minecraft.gui.hud.getGuiTicks();
      this.messageDeletionQueue.removeIf(entry -> time >= entry.deletableAfter() ? this.deleteMessageOrDelay(entry.signature()) == null : false);
   }

   public void deleteMessage(final MessageSignature signature) {
      ChatComponent$DelayedMessageDeletion delayedMessage = this.deleteMessageOrDelay(signature);
      if (delayedMessage != null) {
         this.messageDeletionQueue.add(delayedMessage);
      }
   }

   @Nullable
   private ChatComponent$DelayedMessageDeletion deleteMessageOrDelay(final MessageSignature signature) {
      int time = this.minecraft.gui.hud.getGuiTicks();
      ListIterator<GuiMessage> iterator = this.allMessages.listIterator();

      while (iterator.hasNext()) {
         GuiMessage message = iterator.next();
         if (signature.equals(message.signature())) {
            int deletableAfter = message.addedTime() + 60;
            if (time >= deletableAfter) {
               iterator.set(createDeletedMarker(message));
               this.refreshTrimmedMessages();
               return null;
            }

            return new ChatComponent$DelayedMessageDeletion(signature, deletableAfter);
         }
      }

      return null;
   }

   private static GuiMessage createDeletedMarker(final GuiMessage message) {
      return new GuiMessage(message.addedTime(), DELETED_CHAT_MESSAGE, null, GuiMessageSource.SYSTEM_SERVER, GuiMessageTag.system());
   }

   public void rescaleChat() {
      this.resetChatScroll();
      this.refreshTrimmedMessages();
   }

   private void refreshTrimmedMessages() {
      this.trimmedMessages.clear();

      for (GuiMessage message : Lists.reverse(this.allMessages)) {
         if (this.visibleMessageFilter.test(message)) {
            this.addMessageToDisplayQueue(message);
         }
      }
   }

   public ArrayListDeque<String> getRecentChat() {
      return this.recentChat;
   }

   public void addRecentChat(final String message) {
      if (!message.equals(this.recentChat.peekLast())) {
         if (this.recentChat.size() >= 100) {
            this.recentChat.removeFirst();
         }

         this.recentChat.addLast(message);
      }

      if (message.startsWith("/")) {
         this.commandHistory.addCommand(message);
      }
   }

   public void resetChatScroll() {
      this.chatScrollbarPos = 0;
      this.newMessageSinceScroll = false;
   }

   public void scrollChat(final int dir) {
      this.chatScrollbarPos += dir;
      int max = this.trimmedMessages.size();
      if (this.chatScrollbarPos > max - this.getLinesPerPage()) {
         this.chatScrollbarPos = max - this.getLinesPerPage();
      }

      if (this.chatScrollbarPos <= 0) {
         this.chatScrollbarPos = 0;
         this.newMessageSinceScroll = false;
      }
   }

   public boolean isChatFocused() {
      return this.minecraft.gui.screen() instanceof ChatScreen;
   }

   private int getWidth() {
      return getWidth(this.minecraft.options.chatWidth().get());
   }

   private int getHeight() {
      return getHeight(this.isChatFocused() ? this.minecraft.options.chatHeightFocused().get() : this.minecraft.options.chatHeightUnfocused().get());
   }

   private double getScale() {
      return this.minecraft.options.chatScale().get();
   }

   public static int getWidth(final double pct) {
      int max = 320;
      int min = 40;
      return Mth.floor(pct * 280.0 + 40.0);
   }

   public static int getHeight(final double pct) {
      int max = 180;
      int min = 20;
      return Mth.floor(pct * 160.0 + 20.0);
   }

   public static double defaultUnfocusedPct() {
      int max = 180;
      int min = 20;
      return 70.0 / (getHeight(1.0) - 20);
   }

   public int getLinesPerPage() {
      return this.getHeight() / this.getLineHeight();
   }

   private int getLineHeight() {
      return (int)(9.0 * (this.minecraft.options.chatLineSpacing().get() + 1.0));
   }

   public void saveAsDraft(final String text) {
      boolean isCommand = text.startsWith("/");
      this.latestDraft = new ChatComponent$Draft(text, isCommand ? ChatComponent$ChatMethod.COMMAND : ChatComponent$ChatMethod.MESSAGE);
   }

   public void discardDraft() {
      this.latestDraft = null;
   }

   public <T extends ChatScreen> T createScreen(final ChatComponent$ChatMethod chatMethod, final ChatScreen$ChatConstructor<T> chat) {
      return this.latestDraft != null && chatMethod.isDraftRestorable(this.latestDraft)
         ? chat.create(this.latestDraft.text(), true)
         : chat.create(chatMethod.prefix(), false);
   }

   public void openScreen(final ChatComponent$ChatMethod chatMethod, final ChatScreen$ChatConstructor<?> chat) {
      this.minecraft.gui.setScreen(this.createScreen(chatMethod, (ChatScreen$ChatConstructor<Screen>)chat));
   }

   public void preserveCurrentChatScreen() {
      if (this.minecraft.gui.screen() instanceof ChatScreen chatScreen) {
         this.preservedScreen = chatScreen;
      }
   }

   @Nullable
   public ChatScreen restoreChatScreen() {
      ChatScreen restoredScreen = this.preservedScreen;
      this.preservedScreen = null;
      return restoredScreen;
   }

   public ChatComponent$State storeState() {
      return new ChatComponent$State(List.copyOf(this.allMessages), List.copyOf(this.recentChat), List.copyOf(this.messageDeletionQueue));
   }

   public void restoreState(final ChatComponent$State state) {
      this.recentChat.clear();
      this.recentChat.addAll(state.history);
      this.messageDeletionQueue.clear();
      this.messageDeletionQueue.addAll(state.delayedMessageDeletions);
      this.allMessages.clear();
      this.allMessages.addAll(state.messages);
      this.refreshTrimmedMessages();
   }
}
