package net.minecraft.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;

@Environment(EnvType.CLIENT)
public enum WorldCreationUiState$SelectedGameMode {
   SURVIVAL("survival", GameType.SURVIVAL),
   HARDCORE("hardcore", GameType.SURVIVAL),
   CREATIVE("creative", GameType.CREATIVE),
   DEBUG("spectator", GameType.SPECTATOR);

   public final GameType gameType;
   public final Component displayName;
   private final Component info;

   private WorldCreationUiState$SelectedGameMode(final String name, final GameType gameType) {
      this.gameType = gameType;
      this.displayName = Component.translatable("selectWorld.gameMode." + name);
      this.info = Component.translatable("selectWorld.gameMode." + name + ".info");
   }

   public Component getInfo() {
      return this.info;
   }
}
