package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class Button$Builder {
   private final Component message;
   private final Button$OnPress onPress;
   @Nullable
   private Tooltip tooltip;
   private int x;
   private int y;
   private int width = 150;
   private int height = 20;
   private Button$CreateNarration createNarration = Button.DEFAULT_NARRATION;

   public Button$Builder(final Component message, final Button$OnPress onPress) {
      this.message = message;
      this.onPress = onPress;
   }

   public Button$Builder pos(final int x, final int y) {
      this.x = x;
      this.y = y;
      return this;
   }

   public Button$Builder width(final int width) {
      this.width = width;
      return this;
   }

   public Button$Builder size(final int width, final int height) {
      this.width = width;
      this.height = height;
      return this;
   }

   public Button$Builder bounds(final int x, final int y, final int width, final int height) {
      return this.pos(x, y).size(width, height);
   }

   public Button$Builder tooltip(@Nullable final Tooltip tooltip) {
      this.tooltip = tooltip;
      return this;
   }

   public Button$Builder createNarration(final Button$CreateNarration createNarration) {
      this.createNarration = createNarration;
      return this;
   }

   public Button build() {
      Button button = new Button$Plain(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration);
      button.setTooltip(this.tooltip);
      return button;
   }
}
