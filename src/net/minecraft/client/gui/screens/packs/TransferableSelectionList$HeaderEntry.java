package net.minecraft.client.gui.screens.packs;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class TransferableSelectionList$HeaderEntry extends TransferableSelectionList$Entry {
   private final Font font;
   private final Component text;

   public TransferableSelectionList$HeaderEntry(final TransferableSelectionList this$0, final Font font, final Component text) {
      Objects.requireNonNull(this$0);
      super(this$0);
      this.font = font;
      this.text = text;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.centeredText(this.font, this.text, this.getX() + this.getWidth() / 2, this.getContentYMiddle() - 9 / 2, -1);
   }

   @Override
   public Component getNarration() {
      return this.text;
   }

   @Override
   public String getPackId() {
      return "";
   }
}
