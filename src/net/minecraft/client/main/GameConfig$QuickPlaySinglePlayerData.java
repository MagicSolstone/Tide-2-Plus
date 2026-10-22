package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record GameConfig$QuickPlaySinglePlayerData(@Nullable String worldId) implements GameConfig$QuickPlayVariant {
   @Override
   public boolean isEnabled() {
      return true;
   }
}
