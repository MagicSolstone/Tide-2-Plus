package net.minecraft.client.gui.screens.reporting;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ChatSelectionScreen$ChatSelectionList$DividerEntry extends ChatSelectionScreen$ChatSelectionList$Entry {
   private final Component text;

   public ChatSelectionScreen$ChatSelectionList$DividerEntry(final ChatSelectionScreen$ChatSelectionList this$1, final Component text) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.text = text;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int centerY = this.getContentYMiddle();
      int rowRight = this.getContentRight() - 8;
      int textWidth = ChatSelectionScreen.access$1100(this.this$1.this$0).width(this.text);
      int textLeft = (this.getContentX() + rowRight - textWidth) / 2;
      int textTop = centerY - 9 / 2;
      graphics.text(ChatSelectionScreen.access$1300(this.this$1.this$0), this.text, textLeft, textTop, -6250336);
   }

   @Override
   public Component getNarration() {
      return this.text;
   }
}
