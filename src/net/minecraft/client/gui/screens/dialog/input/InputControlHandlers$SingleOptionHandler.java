package net.minecraft.client.gui.screens.dialog.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton$Builder;
import net.minecraft.client.gui.components.CycleButton$DisplayState;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.action.Action$ValueGetter;
import net.minecraft.server.dialog.input.SingleOptionInput;
import net.minecraft.server.dialog.input.SingleOptionInput$Entry;

@Environment(EnvType.CLIENT)
class InputControlHandlers$SingleOptionHandler implements InputControlHandler<SingleOptionInput> {
   private InputControlHandlers$SingleOptionHandler() {
   }

   public void addControl(final SingleOptionInput input, final Screen screen, final InputControlHandler$Output output) {
      SingleOptionInput$Entry initial = input.initial().orElse(input.entries().getFirst());
      CycleButton$Builder<SingleOptionInput$Entry> controlBuilder = CycleButton.builder(SingleOptionInput$Entry::displayOrDefault, initial)
         .withValues(input.entries())
         .displayState(!input.labelVisible() ? CycleButton$DisplayState.VALUE : CycleButton$DisplayState.NAME_AND_VALUE);
      CycleButton<SingleOptionInput$Entry> control = controlBuilder.create(0, 0, input.width(), 20, input.label());
      output.accept(control, Action$ValueGetter.of(() -> control.getValue().id()));
   }
}
