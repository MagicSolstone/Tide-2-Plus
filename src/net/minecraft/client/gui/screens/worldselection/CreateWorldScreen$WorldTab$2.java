package net.minecraft.client.gui.screens.worldselection;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton$ValueListSupplier;

@Environment(EnvType.CLIENT)
class CreateWorldScreen$WorldTab$2 implements CycleButton$ValueListSupplier<WorldCreationUiState$WorldTypeEntry> {
   CreateWorldScreen$WorldTab$2(final CreateWorldScreen$WorldTab this$1) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
   }

   @Override
   public List<WorldCreationUiState$WorldTypeEntry> getSelectedList() {
      return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean()
         ? this.this$1.this$0.uiState.getAltPresetList()
         : this.this$1.this$0.uiState.getNormalPresetList();
   }

   @Override
   public List<WorldCreationUiState$WorldTypeEntry> getDefaultList() {
      return this.this$1.this$0.uiState.getNormalPresetList();
   }
}
