package net.minecraft.client.gui.screens.worldselection;

import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record SwitchGrid$LabeledSwitch(CycleButton<Boolean> button, BooleanSupplier stateSupplier, @Nullable BooleanSupplier isActiveCondition) {
   public void refreshState() {
      this.button.setValue(this.stateSupplier.getAsBoolean());
      if (this.isActiveCondition != null) {
         this.button.active = this.isActiveCondition.getAsBoolean();
      }
   }
}
