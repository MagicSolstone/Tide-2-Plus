package net.minecraft.client.gui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PopupScreen$Builder {
   @Nullable
   private final Screen backgroundScreen;
   private final Component title;
   private final List<Component> messages = new ArrayList<>();
   private int width = 250;
   @Nullable
   private Identifier image;
   private final List<PopupScreen$ButtonOption> buttons = new ArrayList<>();
   @Nullable
   private Runnable onClose = null;

   public PopupScreen$Builder(@Nullable final Screen backgroundScreen, final Component title) {
      this.backgroundScreen = backgroundScreen;
      this.title = title;
   }

   public PopupScreen$Builder setWidth(final int width) {
      this.width = width;
      return this;
   }

   public PopupScreen$Builder setImage(final Identifier image) {
      this.image = image;
      return this;
   }

   public PopupScreen$Builder addMessage(final Component message) {
      this.messages.add(message);
      return this;
   }

   public PopupScreen$Builder addButton(final Component message, final Consumer<PopupScreen> action) {
      this.buttons.add(new PopupScreen$ButtonOption(message, action));
      return this;
   }

   public PopupScreen$Builder onClose(final Runnable onClose) {
      this.onClose = onClose;
      return this;
   }

   public PopupScreen build() {
      if (this.buttons.isEmpty()) {
         throw new IllegalStateException("Popup must have at least one button");
      } else {
         return new PopupScreen(this.backgroundScreen, this.width, this.image, this.title, this.messages, List.copyOf(this.buttons), this.onClose);
      }
   }
}
