package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record GameConfig$QuickPlayDisabled() implements GameConfig$QuickPlayVariant {
   @Override
   public boolean isEnabled() {
      return false;
   }
}
