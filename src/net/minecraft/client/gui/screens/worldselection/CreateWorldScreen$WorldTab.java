package net.minecraft.client.gui.screens.worldselection;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton$ValueListSupplier;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.CommonLayouts;
import net.minecraft.client.gui.layouts.GridLayout$RowHelper;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class CreateWorldScreen$WorldTab extends GridLayoutTab {
   private static final Component TITLE = Component.translatable("createWorld.tab.world.title");
   private static final Component AMPLIFIED_HELP_TEXT = Component.translatable("generator.minecraft.amplified.info");
   private static final Component GENERATE_STRUCTURES = Component.translatable("selectWorld.mapFeatures");
   private static final Component GENERATE_STRUCTURES_INFO = Component.translatable("selectWorld.mapFeatures.info");
   private static final Component BONUS_CHEST = Component.translatable("selectWorld.bonusItems");
   private static final Component SEED_LABEL = Component.translatable("selectWorld.enterSeed");
   private static final Component SEED_EMPTY_HINT = Component.translatable("selectWorld.seedInfo");
   private static final int WORLD_TAB_WIDTH = 310;
   private final EditBox seedEdit;
   private final Button customizeTypeButton;

   private CreateWorldScreen$WorldTab(final CreateWorldScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(TITLE);
      GridLayout$RowHelper helper = this.layout.columnSpacing(10).rowSpacing(8).createRowHelper(2);
      CycleButton<WorldCreationUiState$WorldTypeEntry> typeButton = helper.addChild(
         CycleButton.builder(WorldCreationUiState$WorldTypeEntry::describePreset, this$0x.uiState.getWorldType())
            .withValues(this.createWorldTypeValueSupplier())
            .create(0, 0, 150, 20, Component.translatable("selectWorld.mapType"), (button, newPreset) -> this$0x.uiState.setWorldType(newPreset))
      );
      typeButton.setValue(this$0x.uiState.getWorldType());
      this$0x.uiState.addListener(data -> {
         WorldCreationUiState$WorldTypeEntry worldType = data.getWorldType();
         typeButton.setValue(worldType);
         if (worldType.isAmplified()) {
            typeButton.setTooltip(Tooltip.create(AMPLIFIED_HELP_TEXT));
         } else {
            typeButton.setTooltip(null);
         }

         typeButton.active = this$0x.uiState.getWorldType().preset() != null;
      });
      this.customizeTypeButton = helper.addChild(Button.builder(Component.translatable("selectWorld.customizeType"), b -> this.openPresetEditor()).build());
      this$0x.uiState.addListener(data -> this.customizeTypeButton.active = !data.isDebug() && data.getPresetEditor() != null);
      this.seedEdit = new CreateWorldScreen$WorldTab$1(this, CreateWorldScreen.access$300(this$0x), 308, 20, Component.translatable("selectWorld.enterSeed"));
      this.seedEdit.setHint(SEED_EMPTY_HINT);
      this.seedEdit.setValue(this$0x.uiState.getSeed());
      this.seedEdit.setResponder(value -> this$0x.uiState.setSeed(this.seedEdit.getValue()));
      helper.addChild(CommonLayouts.labeledElement(CreateWorldScreen.access$400(this$0x), this.seedEdit, SEED_LABEL), 2);
      SwitchGrid$Builder switchGridBuilder = SwitchGrid.builder(310);
      switchGridBuilder.addSwitch(GENERATE_STRUCTURES, this$0x.uiState::isGenerateStructures, this$0x.uiState::setGenerateStructures)
         .withIsActiveCondition(() -> !this$0x.uiState.isDebug())
         .withInfo(GENERATE_STRUCTURES_INFO);
      switchGridBuilder.addSwitch(BONUS_CHEST, this$0x.uiState::isBonusChest, this$0x.uiState::setBonusChest)
         .withIsActiveCondition(() -> !this$0x.uiState.isHardcore() && !this$0x.uiState.isDebug());
      SwitchGrid switchGrid = switchGridBuilder.build();
      helper.addChild(switchGrid.layout(), 2);
      this$0x.uiState.addListener(d -> switchGrid.refreshStates());
   }

   private void openPresetEditor() {
      PresetEditor editor = this.this$0.uiState.getPresetEditor();
      if (editor != null) {
         CreateWorldScreen.access$500(this.this$0).gui.setScreen(editor.createEditScreen(this.this$0, this.this$0.uiState.getSettings()));
      }
   }

   private CycleButton$ValueListSupplier<WorldCreationUiState$WorldTypeEntry> createWorldTypeValueSupplier() {
      return new CreateWorldScreen$WorldTab$2(this);
   }
}
