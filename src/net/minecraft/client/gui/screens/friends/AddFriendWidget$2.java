package net.minecraft.client.gui.screens.friends;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button$OnPress;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
class AddFriendWidget$2 extends PlainTextButton {
   AddFriendWidget$2(
      final AddFriendWidget this$0,
      final int x,
      final int y,
      final int width,
      final int height,
      final Component message,
      final Button$OnPress onPress,
      final Font font,
      final Component val$profileNameComponent
   ) {
      this.val$profileNameComponent = val$profileNameComponentx;
      Objects.requireNonNull(this$0);
      super(x, y, width, height, message, onPress, font);
   }

   @Override
   protected MutableComponent createNarrationMessage() {
      return wrapDefaultNarrationMessage(Component.translatable("gui.friends.my_profile_name.narration", this.val$profileNameComponent));
   }
}
