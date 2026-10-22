package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringUtil;

@Environment(EnvType.CLIENT)
public record GameConfig$QuickPlayRealmsData(String realmId) implements GameConfig$QuickPlayVariant {
   @Override
   public boolean isEnabled() {
      return !StringUtil.isBlank(this.realmId);
   }
}
