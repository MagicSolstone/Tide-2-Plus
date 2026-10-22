package net.minecraft.client.gui.components;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.GuiMessage$Line;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;

@Environment(EnvType.CLIENT)
class ChatComponent$1 implements ChatComponent$LineConsumer {
   private boolean hoveredOverCurrentMessage;

   ChatComponent$1(
      final ChatComponent this$0,
      final int val$chatBottom,
      final int val$entryHeight,
      final int val$entryBottomToMessageY,
      final ChatComponent$ChatGraphicsAccess val$graphics,
      final float val$textOpacity,
      final int val$messageHeight
   ) {
      this.val$chatBottom = val$chatBottomx;
      this.val$entryHeight = val$entryHeightx;
      this.val$entryBottomToMessageY = val$entryBottomToMessageYx;
      this.val$graphics = val$graphicsx;
      this.val$textOpacity = val$textOpacityx;
      this.val$messageHeight = val$messageHeightx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void accept(final GuiMessage$Line line, final int lineIndex, final float alpha) {
      int entryBottom = this.val$chatBottom - lineIndex * this.val$entryHeight;
      int entryTop = entryBottom - this.val$entryHeight;
      int textTop = entryBottom - this.val$entryBottomToMessageY;
      boolean hoveredOverCurrentLine = this.val$graphics.handleMessage(textTop, alpha * this.val$textOpacity, line.content());
      this.hoveredOverCurrentMessage |= hoveredOverCurrentLine;
      boolean forceIconRendering;
      if (line.endOfEntry()) {
         forceIconRendering = this.hoveredOverCurrentMessage;
         this.hoveredOverCurrentMessage = false;
      } else {
         forceIconRendering = false;
      }

      GuiMessageTag tag = line.tag();
      if (tag != null) {
         this.val$graphics.handleTag(-4, entryTop, -2, entryBottom, alpha * this.val$textOpacity, tag);
         if (tag.icon() != null) {
            int iconLeft = line.getTagIconLeft(this.this$0.minecraft.font);
            int textBottom = textTop + this.val$messageHeight;
            this.val$graphics.handleTagIcon(iconLeft, textBottom, forceIconRendering, tag, tag.icon());
         }
      }
   }
}
