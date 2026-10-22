package net.minecraft.client.gui.screens.worldselection;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout$RowHelper;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class CreateWorldScreen$MoreTab extends GridLayoutTab {
   private static final Component TITLE = Component.translatable("createWorld.tab.more.title");
   private static final Component GAME_RULES_LABEL = Component.translatable("selectWorld.gameRules");
   private static final Component DATA_PACKS_LABEL = Component.translatable("selectWorld.dataPacks");

   private CreateWorldScreen$MoreTab(final CreateWorldScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(TITLE);
      GridLayout$RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
      helper.addChild(Button.builder(GAME_RULES_LABEL, b -> this.openGameRulesScreen()).width(210).build());
      helper.addChild(
         Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, b -> this$0x.openExperimentsScreen(this$0x.uiState.getSettings().dataConfiguration()))
            .width(210)
            .build()
      );
      helper.addChild(
         Button.builder(DATA_PACKS_LABEL, b -> this$0x.openDataPackSelectionScreen(this$0x.uiState.getSettings().dataConfiguration())).width(210).build()
      );
   }

   private void openGameRulesScreen() {
      CreateWorldScreen.access$700(this.this$0)
         .gui
         .setScreen(
            new WorldCreationGameRulesScreen(
               this.this$0.uiState.getGameRules().copy(this.this$0.uiState.getSettings().dataConfiguration().enabledFeatures()), gameRules -> {
                  CreateWorldScreen.access$600(this.this$0).gui.setScreen(this.this$0);
                  gameRules.ifPresent(this.this$0.uiState::setGameRules);
               }
            )
         );
   }
}
