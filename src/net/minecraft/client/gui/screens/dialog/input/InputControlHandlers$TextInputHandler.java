package net.minecraft.client.gui.screens.dialog.input;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.layouts.CommonLayouts;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.server.dialog.input.TextInput;
import net.minecraft.server.dialog.input.TextInput$MultilineOptions;

@Environment(EnvType.CLIENT)
class InputControlHandlers$TextInputHandler implements InputControlHandler<TextInput> {
   private InputControlHandlers$TextInputHandler() {
   }

   public void addControl(final TextInput input, final Screen screen, final InputControlHandler$Output output) {
      Font font = screen.getFont();
      LayoutElement control;
      Supplier<String> getter;
      if (input.multiline().isPresent()) {
         TextInput$MultilineOptions multiline = input.multiline().get();
         int computedHeight = multiline.height().orElseGet(() -> {
            int lineCountToFit = multiline.maxLines().orElse(4);
            return Math.min(9 * lineCountToFit + 8, 512);
         });
         MultiLineEditBox editBox = MultiLineEditBox.builder().build(font, input.width(), computedHeight, CommonComponents.EMPTY);
         editBox.setCharacterLimit(input.maxLength());
         multiline.maxLines().ifPresent(editBox::setLineLimit);
         editBox.setValue(input.initial());
         control = editBox;
         getter = editBox::getValue;
      } else {
         EditBox editBox = new EditBox(font, input.width(), 20, input.label());
         editBox.setMaxLength(input.maxLength());
         editBox.setValue(input.initial());
         control = editBox;
         getter = editBox::getValue;
      }

      LayoutElement wrappedControl = (LayoutElement)(input.labelVisible() ? CommonLayouts.labeledElement(font, control, input.label()) : control);
      output.accept(wrappedControl, new InputControlHandlers$TextInputHandler$1(this, getter));
   }
}
