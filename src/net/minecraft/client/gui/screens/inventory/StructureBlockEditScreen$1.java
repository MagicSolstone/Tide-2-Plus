package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class StructureBlockEditScreen$1 extends EditBox {
   StructureBlockEditScreen$1(
      final StructureBlockEditScreen this$0, final Font font, final int x, final int y, final int width, final int height, final Component narration
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(font, x, y, width, height, narration);
   }

   @Override
   public boolean charTyped(final CharacterEvent event) {
      return !StructureBlockEditScreen.access$000(this.this$0, this.getValue(), event.codepoint(), this.getCursorPosition()) ? false : super.charTyped(event);
   }
}
