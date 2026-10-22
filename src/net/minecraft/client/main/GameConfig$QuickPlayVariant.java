package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public sealed interface GameConfig$QuickPlayVariant
   permits GameConfig$QuickPlaySinglePlayerData,
   GameConfig$QuickPlayMultiplayerData,
   GameConfig$QuickPlayRealmsData,
   GameConfig$QuickPlayDisabled {
   GameConfig$QuickPlayVariant DISABLED = new GameConfig$QuickPlayDisabled();

   boolean isEnabled();
}
