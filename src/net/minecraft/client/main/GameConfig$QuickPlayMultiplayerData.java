package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringUtil;

@Environment(EnvType.CLIENT)
public record GameConfig$QuickPlayMultiplayerData(String serverAddress) implements GameConfig$QuickPlayVariant {
   @Override
   public boolean isEnabled() {
      return !StringUtil.isBlank(this.serverAddress);
   }
}
