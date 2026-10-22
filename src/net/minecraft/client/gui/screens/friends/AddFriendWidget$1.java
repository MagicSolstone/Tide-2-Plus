package net.minecraft.client.gui.screens.friends;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class AddFriendWidget$1 extends EditBox {
   AddFriendWidget$1(final AddFriendWidget this$0, final Font font, final int width, final int height, final Component narration) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(font, width, height, narration);
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      boolean enterPressed = event.shortcutKey() == 13 || event.shortcutKey() == 1073741912;
      boolean elementsActive = this.isActive() && this.this$0.addButton.active;
      if (elementsActive && this.isFocused() && enterPressed) {
         this.this$0.addButton.playDownSound(this.this$0.minecraft.getSoundManager());
         this.this$0.addButton.onPress(event);
         return true;
      } else {
         return super.keyPressed(event);
      }
   }
}
