package net.minecraft.client.main;

import com.mojang.blaze3d.platform.DisplayData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GameConfig {
   public final GameConfig$UserData user;
   public final DisplayData display;
   public final GameConfig$FolderData location;
   public final GameConfig$GameData game;
   public final GameConfig$QuickPlayData quickPlay;

   public GameConfig(
      final GameConfig$UserData userData,
      final DisplayData displayData,
      final GameConfig$FolderData folderData,
      final GameConfig$GameData gameData,
      final GameConfig$QuickPlayData quickPlayData
   ) {
      this.user = userData;
      this.display = displayData;
      this.location = folderData;
      this.game = gameData;
      this.quickPlay = quickPlayData;
   }
}
