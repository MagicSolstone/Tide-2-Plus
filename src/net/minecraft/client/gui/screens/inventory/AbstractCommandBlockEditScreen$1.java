package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
class AbstractCommandBlockEditScreen$1 extends EditBox {
   AbstractCommandBlockEditScreen$1(
      final AbstractCommandBlockEditScreen this$0, final Font font, final int x, final int y, final int width, final int height, final Component narration
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(font, x, y, width, height, narration);
   }

   @Override
   protected MutableComponent createNarrationMessage() {
      return super.createNarrationMessage().append(this.this$0.commandSuggestions.getNarrationMessage());
   }
}
