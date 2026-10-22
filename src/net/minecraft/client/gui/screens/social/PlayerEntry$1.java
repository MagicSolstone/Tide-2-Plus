package net.minecraft.client.gui.screens.social;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button$OnPress;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
class PlayerEntry$1 extends ImageButton {
   PlayerEntry$1(
      final PlayerEntry this$0,
      final int x,
      final int y,
      final int width,
      final int height,
      final WidgetSprites sprites,
      final Button$OnPress onPress,
      final Component message
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(x, y, width, height, sprites, onPress, message);
   }

   @Override
   protected MutableComponent createNarrationMessage() {
      return this.this$0.getEntryNarationMessage(super.createNarrationMessage());
   }
}
