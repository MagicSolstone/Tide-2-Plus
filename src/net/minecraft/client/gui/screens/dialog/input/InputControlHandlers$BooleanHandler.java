package net.minecraft.client.gui.screens.dialog.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.input.BooleanInput;

@Environment(EnvType.CLIENT)
class InputControlHandlers$BooleanHandler implements InputControlHandler<BooleanInput> {
   private InputControlHandlers$BooleanHandler() {
   }

   public void addControl(final BooleanInput input, final Screen screen, final InputControlHandler$Output output) {
      Font font = screen.getFont();
      Checkbox control = Checkbox.builder(input.label(), font).selected(input.initial()).build();
      output.accept(control, new InputControlHandlers$BooleanHandler$1(this, control, input));
   }
}
