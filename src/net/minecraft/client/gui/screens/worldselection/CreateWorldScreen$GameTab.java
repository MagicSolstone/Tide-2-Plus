package net.minecraft.client.gui.screens.worldselection;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.CommonLayouts;
import net.minecraft.client.gui.layouts.GridLayout$RowHelper;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;

@Environment(EnvType.CLIENT)
class CreateWorldScreen$GameTab extends GridLayoutTab {
   private static final Component TITLE = Component.translatable("createWorld.tab.game.title");
   private static final Component ALLOW_COMMANDS = Component.translatable("selectWorld.allowCommands");
   private final EditBox nameEdit;

   private CreateWorldScreen$GameTab(final CreateWorldScreen this$0) {
      Objects.requireNonNull(this$0);
      super(TITLE);
      GridLayout$RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
      LayoutSettings buttonLayoutSettings = helper.newCellSettings();
      this.nameEdit = new EditBox(CreateWorldScreen.access$000(this$0), 208, 20, Component.translatable("selectWorld.enterName"));
      this.nameEdit.setValue(this$0.uiState.getName());
      this.nameEdit.setResponder(this$0.uiState::setName);
      this$0.uiState
         .addListener(
            uiState -> this.nameEdit
               .setTooltip(
                  Tooltip.create(
                     Component.translatable("selectWorld.targetFolder", Component.literal(uiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC))
                  )
               )
         );
      CreateWorldScreen.access$100(this$0, this.nameEdit);
      helper.addChild(
         CommonLayouts.labeledElement(CreateWorldScreen.access$200(this$0), this.nameEdit, CreateWorldScreen.NAME_LABEL),
         helper.newCellSettings().alignHorizontallyCenter()
      );
      CycleButton<WorldCreationUiState$SelectedGameMode> gameModeButton = helper.addChild(
         CycleButton.<WorldCreationUiState$SelectedGameMode>builder(selectedGameMode -> selectedGameMode.displayName, this$0.uiState.getGameMode())
            .withValues(
               WorldCreationUiState$SelectedGameMode.SURVIVAL, WorldCreationUiState$SelectedGameMode.HARDCORE, WorldCreationUiState$SelectedGameMode.CREATIVE
            )
            .create(0, 0, 210, 20, CreateWorldScreen.GAME_MODEL_LABEL, (button, gameMode) -> this$0.uiState.setGameMode(gameMode)),
         buttonLayoutSettings
      );
      this$0.uiState.addListener(data -> {
         gameModeButton.setValue(data.getGameMode());
         gameModeButton.active = !data.isDebug();
         gameModeButton.setTooltip(Tooltip.create(data.getGameMode().getInfo()));
      });
      CycleButton<Difficulty> difficultyButton = helper.addChild(
         CycleButton.builder(Difficulty::getDisplayName, this$0.uiState.getDifficulty())
            .withValues(Difficulty.values())
            .create(0, 0, 210, 20, Component.translatable("options.difficulty"), (button, value) -> this$0.uiState.setDifficulty(value)),
         buttonLayoutSettings
      );
      this$0.uiState.addListener(d -> {
         difficultyButton.setValue(this$0.uiState.getDifficulty());
         difficultyButton.active = !this$0.uiState.isHardcore();
         difficultyButton.setTooltip(Tooltip.create(this$0.uiState.getDifficulty().getInfo()));
      });
      CycleButton<Boolean> allowCommandsButton = helper.addChild(
         CycleButton.onOffBuilder(this$0.uiState.isAllowCommands())
            .withTooltip(state -> Tooltip.create(CreateWorldScreen.ALLOW_COMMANDS_INFO))
            .create(0, 0, 210, 20, ALLOW_COMMANDS, (b, state) -> this$0.uiState.setAllowCommands(state))
      );
      this$0.uiState.addListener(d -> {
         allowCommandsButton.setValue(this$0.uiState.isAllowCommands());
         allowCommandsButton.active = !this$0.uiState.isDebug() && !this$0.uiState.isHardcore();
      });
      if (!SharedConstants.getCurrentVersion().stable()) {
         helper.addChild(
            Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, button -> this$0.openExperimentsScreen(this$0.uiState.getSettings().dataConfiguration()))
               .width(210)
               .build()
         );
      }
   }
}
