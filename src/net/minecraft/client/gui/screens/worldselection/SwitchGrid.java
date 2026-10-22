package net.minecraft.client.gui.screens.worldselection;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.Layout;

@Environment(EnvType.CLIENT)
class SwitchGrid {
   private static final int DEFAULT_SWITCH_BUTTON_WIDTH = 44;
   private final List<SwitchGrid$LabeledSwitch> switches;
   private final Layout layout;

   private SwitchGrid(final List<SwitchGrid$LabeledSwitch> switches, final Layout layout) {
      this.switches = switches;
      this.layout = layout;
   }

   public Layout layout() {
      return this.layout;
   }

   public void refreshStates() {
      this.switches.forEach(SwitchGrid$LabeledSwitch::refreshState);
   }

   public static SwitchGrid$Builder builder(final int width) {
      return new SwitchGrid$Builder(width);
   }
}
