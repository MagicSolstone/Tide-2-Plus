package net.minecraft.client.multiplayer.chat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record GuiMessage$Line(GuiMessage parent, FormattedCharSequence content, boolean endOfEntry) {
   public int getTagIconLeft(final Font font) {
      return font.width(this.content) + 4;
   }

   @Nullable
   public GuiMessageTag tag() {
      return this.parent.tag;
   }

   public int addedTime() {
      return this.parent.addedTime;
   }
}
