package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
public abstract class Button extends AbstractButton {
   public static final int SMALL_WIDTH = 120;
   public static final int DEFAULT_WIDTH = 150;
   public static final int BIG_WIDTH = 200;
   public static final int DEFAULT_HEIGHT = 20;
   public static final int DEFAULT_SPACING = 8;
   protected static final Button$CreateNarration DEFAULT_NARRATION = defaultNarrationSupplier -> defaultNarrationSupplier.get();
   protected final Button$OnPress onPress;
   protected final Button$CreateNarration createNarration;

   public static Button$Builder builder(final Component message, final Button$OnPress onPress) {
      return new Button$Builder(message, onPress);
   }

   protected Button(
      final int x,
      final int y,
      final int width,
      final int height,
      final Component message,
      final Button$OnPress onPress,
      final Button$CreateNarration createNarration
   ) {
      super(x, y, width, height, message);
      this.onPress = onPress;
      this.createNarration = createNarration;
   }

   @Override
   public void onPress(final InputWithModifiers input) {
      this.onPress.onPress(this);
   }

   @Override
   protected MutableComponent createNarrationMessage() {
      return this.createNarration.createNarrationMessage(() -> super.createNarrationMessage());
   }

   @Override
   public void updateWidgetNarration(final NarrationElementOutput output) {
      this.defaultButtonNarrationText(output);
   }
}
