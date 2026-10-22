package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.LockIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.EqualSpacingLayout$Orientation;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
record WorldOptionsScreen$DifficultyButtons(LayoutElement layout, CycleButton<Difficulty> difficultyButton, LockIconButton lockButton, Level level) {
   private static final Component DIFFICULTY_TITLE = Component.translatable("options.difficulty");
   private static final Tooltip DIFFICULTY_DISABLED_HARDCORE_TOOLTIP = Tooltip.create(
      Component.translatable("options.worldOptions.difficulty.disabled.hardcore.tooltip")
   );
   private static final Tooltip DIFFICULTY_DISABLED_LOCKED_TOOLTIP = Tooltip.create(
      Component.translatable("options.worldOptions.difficulty.disabled.locked.tooltip")
   );
   private static final Tooltip DIFFICULTY_DISABLED_OPERATOR_TOOLTIP = Tooltip.create(
      Component.translatable("options.worldOptions.difficulty.disabled.operator.tooltip")
   );

   public static WorldOptionsScreen$DifficultyButtons create(final Minecraft minecraft, final Level level, final WorldOptionsScreen screen) {
      screen.wantedDifficulty = level.getDifficulty();
      screen.initialDifficulty = screen.wantedDifficulty;
      CycleButton<Difficulty> difficultyButton = CycleButton.builder(Difficulty::getDisplayName, level.getDifficulty())
         .withValues(Difficulty.values())
         .create(0, 0, 150, 20, DIFFICULTY_TITLE, (var1, value) -> {
            screen.wantedDifficulty = value;
            screen.updateApplyChangesActiveState();
         });
      screen.wantedDifficultyLocked = isDifficultyLocked(level);
      screen.initialDifficultyLocked = screen.wantedDifficultyLocked;
      LockIconButton lockButton = new LockIconButton(0, 0, button -> {
         if (button instanceof LockIconButton lockIconButton) {
            lockIconButton.setLocked(!screen.wantedDifficultyLocked);
         }

         screen.wantedDifficultyLocked = !screen.wantedDifficultyLocked;
         screen.updateApplyChangesActiveState();
      });
      difficultyButton.setWidth(difficultyButton.getWidth() - lockButton.getWidth());
      lockButton.setLocked(isDifficultyLocked(level));
      updateDifficultyButtonsState(minecraft, level, difficultyButton, lockButton);
      EqualSpacingLayout linearLayout = new EqualSpacingLayout(150, 0, EqualSpacingLayout$Orientation.HORIZONTAL);
      linearLayout.addChild(difficultyButton);
      linearLayout.addChild(lockButton);
      return new WorldOptionsScreen$DifficultyButtons(linearLayout, difficultyButton, lockButton, level);
   }

   private void refresh(final Minecraft minecraft, final WorldOptionsScreen worldOptionsScreen) {
      this.refresh(minecraft, worldOptionsScreen, false);
   }

   private void refresh(final Minecraft minecraft, final WorldOptionsScreen worldOptionsScreen, final boolean forceLevelDifficulty) {
      this.difficultyButton
         .setValue(
            forceLevelDifficulty
               ? this.level.getDifficulty()
               : (worldOptionsScreen.wantedDifficulty != null ? worldOptionsScreen.wantedDifficulty : this.level.getDifficulty())
         );
      this.lockButton.setLocked(worldOptionsScreen.wantedDifficultyLocked != null ? worldOptionsScreen.wantedDifficultyLocked : isDifficultyLocked(this.level));
      updateDifficultyButtonsState(minecraft, this.level, this.difficultyButton, this.lockButton);
   }

   private static void updateDifficultyButtonsState(
      final Minecraft minecraft, final Level level, final CycleButton<Difficulty> difficultyButton, final LockIconButton lockButton
   ) {
      if (minecraft.player != null
         && (
            minecraft.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)
               || minecraft.getSingleplayerServer() != null && minecraft.getSingleplayerServer().isSingleplayerOwner(minecraft.player.nameAndId())
         )) {
         if (level.getLevelData().isDifficultyLocked()) {
            lockButton.active = false;
            difficultyButton.active = false;
            difficultyButton.setTooltip(DIFFICULTY_DISABLED_LOCKED_TOOLTIP);
         } else if (level.getLevelData().isHardcore()) {
            lockButton.active = false;
            difficultyButton.active = false;
            difficultyButton.setTooltip(DIFFICULTY_DISABLED_HARDCORE_TOOLTIP);
         } else {
            lockButton.active = true;
            difficultyButton.active = true;
            difficultyButton.setTooltip(null);
         }
      } else {
         lockButton.active = false;
         difficultyButton.active = false;
         difficultyButton.setTooltip(DIFFICULTY_DISABLED_OPERATOR_TOOLTIP);
      }
   }

   private static boolean isDifficultyLocked(final Level level) {
      return level.getLevelData().isDifficultyLocked() || level.getLevelData().isHardcore();
   }
}
